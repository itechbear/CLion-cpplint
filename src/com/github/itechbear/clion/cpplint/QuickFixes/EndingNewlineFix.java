package com.github.itechbear.clion.cpplint.QuickFixes;

import com.intellij.codeInspection.LocalQuickFixBase;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by HD on 2015/1/2.
 */
public class EndingNewlineFix extends LocalQuickFixBase {
  protected EndingNewlineFix() {
    super(EndingNewlineFix.class.getSimpleName());
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
    Document document = QuickFixesManager.getDocument(project, problemDescriptor);
    if (document == null) {
      return;
    }
    document.insertString(document.getTextLength(), "\n");
    FileDocumentManager.getInstance().saveDocument(document);
  }
}
