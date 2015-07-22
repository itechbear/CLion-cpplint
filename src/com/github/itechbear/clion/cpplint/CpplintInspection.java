package com.github.itechbear.clion.cpplint;

import com.github.itechbear.clion.cpplint.QuickFixes.QuickFixesManager;
import com.github.itechbear.util.CygwinUtil;
import com.intellij.codeInspection.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HD on 2015/1/1.
 */
public class CpplintInspection extends LocalInspectionTool {
    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        List<ProblemDescriptor> descriptors = new ArrayList<ProblemDescriptor>();

        if (!CpplintLanguageType.isCFamily(file)) {
            return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
        }

        String project_root = file.getProject().getBaseDir().getCanonicalPath();
        if (CygwinUtil.isCygwinEnvironment()) {
            project_root = CygwinUtil.toCygwinPath(project_root);
        }
        String flag = "--root=" + project_root;
        String cpp_file_path = file.getVirtualFile().getCanonicalPath();
        if (CygwinUtil.isCygwinEnvironment()) {
            cpp_file_path = CygwinUtil.toCygwinPath(cpp_file_path);
        }
        Scanner scanner = null;
        try {
            String message = CpplintCommand.execute(flag + " " + cpp_file_path);
            if (message.isEmpty()) {
                return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
            }
            scanner = new Scanner(message);

            Document document = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
            if (document == null) {
                return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
            }
            Pattern pattern = Pattern.compile("^.+:([0-9]+):\\s+(.+)\\s+\\[([^\\]]+)+\\]\\s+\\[([0-9]+)\\]$");
            String line;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (!matcher.matches()) {
                    continue;
                }
                int line_number = Integer.parseInt(matcher.group(1), 10);
                line_number = line_number > 0 ? (line_number - 1) : 0;
                String error_message = "cpplint: " +  matcher.group(2);
                String rule_name = matcher.group(3);
                int warning_level = Integer.parseInt(matcher.group(4), 10);
                int line_start_offset = document.getLineStartOffset(line_number);
                int line_end_offset = document.getLineEndOffset(line_number);
                LocalQuickFixBase fix = QuickFixesManager.get(rule_name);
                ProblemDescriptor problemDescriptor = manager.createProblemDescriptor(file, TextRange.create(line_start_offset, line_end_offset), error_message, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true, fix);
                descriptors.add(problemDescriptor);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            String message = "Failed to execute cpplint.py!";
//            Notifications.Bus.notify(new Notification("ApplicationName", "CLion-MacroFormatter", message, NotificationType.WARNING));
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
    }
}
