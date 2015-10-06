package com.github.itechbear.clion.cpplint.QuickFixes;

import com.intellij.codeInspection.LocalQuickFixBase;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by HD on 2015/1/3.
 */
public class AddHeaderGuardFix extends LocalQuickFixBase {
  protected AddHeaderGuardFix() {
    super(AddHeaderGuardFix.class.getSimpleName());
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
    Document document = QuickFixesManager.getDocument(project, problemDescriptor);
    if (document == null) {
      return;
    }
    String filepath = problemDescriptor.getPsiElement().getContainingFile().getVirtualFile().getCanonicalPath();
    if (filepath == null) {
      return;
    }
    String root = project.getBasePath();
    String relative_path = FileUtil.getRelativePath(new File(root), new File(filepath));
    if (relative_path == null) {
      return;
    }
    String guard = relative_path.replaceAll("[^\\w]", "_").toUpperCase() + "_";
    insertGuard(document, guard);
    FileDocumentManager.getInstance().saveDocument(document);
  }

  private void insertGuard(Document document, String guard) {
    String head_guard = "#ifndef " + guard + "\n#define " + guard + " 1\n";
    document.insertString(0, head_guard);
    String tail_guard = "\n#endif  // " + guard + "\n";
    document.insertString(document.getTextLength(), tail_guard);
  }
}
