package com.github.itechbear.clion.cpplint;

import com.github.itechbear.clion.cpplint.QuickFixes.QuickFixesManager;
import com.github.itechbear.util.CygwinUtil;
import com.github.itechbear.util.MinGWUtil;
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

    // Determine whether this file is a C/C++ file.
    if (!CpplintLanguageType.isCFamily(file)) {
      return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
    }

    // Format the path of the project root. Otherwise, cpplint would keep reporting header guard errors.
    String projectRoot = file.getProject().getBaseDir().getCanonicalPath();
    if (CygwinUtil.isCygwinEnvironment()) {
      projectRoot = CygwinUtil.toCygwinPath(projectRoot);
    }

    // Don't pass project root
    String flag = "";
    if (!MinGWUtil.isMinGWEnvironment()) {
      flag += "--root=" + projectRoot;
    }
    String cppFilePath = file.getVirtualFile().getCanonicalPath();
    if (CygwinUtil.isCygwinEnvironment()) {
      cppFilePath = CygwinUtil.toCygwinPath(cppFilePath);
    }
    Scanner scanner = null;
    try {
      String message = CpplintCommand.execute(file.getProject(), flag, cppFilePath);
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
        int lineNumber = Integer.parseInt(matcher.group(1), 10);
        int line_count = document.getLineCount();
        if (0 == line_count) {
          continue;
        }
        lineNumber = (lineNumber >= line_count) ? (line_count - 1) : lineNumber;
        lineNumber = (lineNumber > 0) ? (lineNumber - 1) : 0;
        String errorMessage = "cpplint: " + matcher.group(2);
        String ruleName = matcher.group(3);
        int warning_level = Integer.parseInt(matcher.group(4), 10);
        int line_start_offset = document.getLineStartOffset(lineNumber);
        int line_end_offset = document.getLineEndOffset(lineNumber);
        LocalQuickFixBase fix = QuickFixesManager.get(ruleName);
        ProblemDescriptor problemDescriptor = manager.createProblemDescriptor(file, TextRange.create(line_start_offset, line_end_offset), errorMessage, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true, fix);
        descriptors.add(problemDescriptor);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }

    return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
  }
}
