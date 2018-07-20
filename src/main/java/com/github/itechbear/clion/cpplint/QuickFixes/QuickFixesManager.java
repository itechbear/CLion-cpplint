package com.github.itechbear.clion.cpplint.QuickFixes;

import java.util.HashMap;
import java.util.Map;

import com.intellij.codeInspection.LocalQuickFixBase;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * Created by HD on 2015/1/2.
 */
public class QuickFixesManager {
    private static Map<String, LocalQuickFixBase> fixes;
    private static QuickFixesManager INSTANCE = new QuickFixesManager();

    private QuickFixesManager() {
        fixes = new HashMap<String, LocalQuickFixBase>();

        fixes.put("build/class", null);
        fixes.put("build/c++11", null);
        fixes.put("build/deprecated", null);
        fixes.put("build/endif_comment", null);
        fixes.put("build/explicit_make_pair", null);
        fixes.put("build/forward_decl", null);
        fixes.put("build/header_guard", null);
        fixes.put("build/include", null);
        fixes.put("build/include_alpha", null);
        fixes.put("build/include_order", null);
        fixes.put("build/include_what_you_use", null);
        fixes.put("build/namespaces", null);
        fixes.put("build/printf_format", null);
        fixes.put("build/storage_class", null);
        fixes.put("legal/copyright", null);
        fixes.put("readability/alt_tokens", null);
        fixes.put("readability/braces", null);
        fixes.put("readability/casting", null);
        fixes.put("readability/check", null);
        fixes.put("readability/constructors", null);
        fixes.put("readability/fn_size", null);
        fixes.put("readability/function", null);
        fixes.put("readability/inheritance", null);
        fixes.put("readability/multiline_comment", null);
        fixes.put("readability/multiline_string", null);
        fixes.put("readability/namespace", null);
        fixes.put("readability/nolint", null);
        fixes.put("readability/nul", null);
        fixes.put("readability/strings", null);
        fixes.put("readability/todo", null);
        fixes.put("readability/utf8", null);
        fixes.put("runtime/arrays", null);
        fixes.put("runtime/casting", null);
        fixes.put("runtime/explicit", null);
        fixes.put("runtime/int", null);
        fixes.put("runtime/init", null);
        fixes.put("runtime/invalid_increment", null);
        fixes.put("runtime/member_string_references", null);
        fixes.put("runtime/memset", null);
        fixes.put("runtime/indentation_namespace", null);
        fixes.put("runtime/operator", null);
        fixes.put("runtime/printf", null);
        fixes.put("runtime/printf_format", null);
        fixes.put("runtime/references", null);
        fixes.put("runtime/string", null);
        fixes.put("runtime/threadsafe_fn", null);
        fixes.put("runtime/vlog", null);
        fixes.put("whitespace/blank_line", null);
        fixes.put("whitespace/braces", null);
        fixes.put("whitespace/comma", null);
        fixes.put("whitespace/comments", null);
        fixes.put("whitespace/empty_conditional_body", null);
        fixes.put("whitespace/empty_loop_body", null);
        fixes.put("whitespace/end_of_line", null);
        fixes.put("whitespace/ending_newline", null);
        fixes.put("whitespace/forcolon", null);
        fixes.put("whitespace/indent", null);
        fixes.put("whitespace/line_length", null);
        fixes.put("whitespace/newline", null);
        fixes.put("whitespace/operators", null);
        fixes.put("whitespace/parens", null);
        fixes.put("whitespace/semicolon", null);
        fixes.put("whitespace/tab", null);
        fixes.put("whitespace/todo", null);
    }

    synchronized public static void add(String name, LocalQuickFixBase fix) {
        if (fixes.containsKey(name)) {
            return;
        }
        fixes.put(name, fix);
    }

    synchronized public static LocalQuickFixBase get(String name) {
        return fixes.get(name);
    }

    public static Document getDocument(Project project, ProblemDescriptor problemDescriptor) {
        PsiElement psiElement = problemDescriptor.getEndElement();
        PsiFile psiFile = psiElement.getContainingFile();
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        return psiDocumentManager.getDocument(psiFile);
    }
}
