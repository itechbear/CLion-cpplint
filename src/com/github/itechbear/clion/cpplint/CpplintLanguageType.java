package com.github.itechbear.clion.cpplint;

import com.intellij.psi.PsiFile;
import com.jetbrains.cidr.lang.OCLanguageKind;
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl;

/**
 * Created by HD on 2015/1/1.
 */
public class CpplintLanguageType {
    public static boolean isCFamily(PsiFile file) {
        if (!(file instanceof OCFileImpl)) {
            return false;
        }

        OCLanguageKind ocLanguageKind = ((OCFileImpl) file).getKind();

        return ocLanguageKind.isCpp();
    }
}
