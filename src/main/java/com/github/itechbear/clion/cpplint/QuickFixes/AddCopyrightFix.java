package com.github.itechbear.clion.cpplint.QuickFixes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.intellij.codeInspection.LocalQuickFixBase;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by HD on 2015/1/2.
 */
public class AddCopyrightFix extends LocalQuickFixBase {
    protected AddCopyrightFix() {
        super(AddCopyrightFix.class.getSimpleName());
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
//        Document document = QuickFixesManager.getDocument(project, problemDescriptor);
//        if (document == null) {
//            return;
//        }
//        String copyright = "/**\n" +
//                " * @copyright Copyright (c) 2011 " + getCorpName(project, problemDescriptor) + ". All rights reserved.\n" +
//                " * @file " + getFilename(project, problemDescriptor) + "\n" +
//                " * @author " + getAuthorName(project, problemDescriptor) + "\n" +
//                " * @date " + getDate(project, problemDescriptor) + "\n" +
//                " * @brief brief description\n" +
//                " *\n" +
//                " * detailed description.\n" +
//                " **/\n\n";
//        document.insertString(0, copyright);
//        FileDocumentManager.getInstance().saveDocument(document);
    }

    private String getFilename(Project project, ProblemDescriptor problemDescriptor) {
        return problemDescriptor.getPsiElement().getContainingFile().getVirtualFile().getName();
    }

//    private String getAuthorName(Project project, ProblemDescriptor problemDescriptor) {
//        return Settings.get(Option.OPTION_KEY_AUTHOR);
//    }

    private String getDate(Project project, ProblemDescriptor problemDescriptor) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

//    private String getCorpName(Project project, ProblemDescriptor problemDescriptor) {
//        return Settings.get(Option.OPTION_KEY_COPYRIGHT);
//    }
}
