package com.github.itechbear.clion.cpplint;

import java.util.List;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by HD on 2015/1/1.
 */
public class CpplintInspection extends LocalInspectionTool {
    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        // Determine whether this file is a C/C++ file.
        if (!CpplintLanguageType.isCFamily(file)) {
            return new ProblemDescriptor[0];
        }

        final Document document = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
        if (null == document) {
            return new ProblemDescriptor[0];
        }
        final List<ProblemDescriptor> descriptors = CpplintRunner.lint(file, manager, document);

        return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
    }
}
