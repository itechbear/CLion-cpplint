package com.github.itechbear.util;

import com.jetbrains.cidr.cpp.CPPToolchains;
import com.jetbrains.cidr.cpp.toolchains.Cygwin;

import java.io.File;

/**
 * Created by HD on 2015/1/3.
 */
public class CygwinUtil {
    public static boolean isCygwinEnvironment() {
        Cygwin cygwin = CPPToolchains.getInstance().getCygwin();

        return null != cygwin && (cygwin.isCygwin() || cygwin.isCygwin64());
    }

    public static String toCygwinPath(String path) {
        Cygwin cygwin = CPPToolchains.getInstance().getCygwin();

        if (cygwin == null) {
            return path;
        }

        return Cygwin.toCygwinPath(path, cygwin);
    }

    /**
     * Get the root path of cygwin.
     * @return String like C:\Cygwin
     */
    public static String getCygwinRoot() {
        Cygwin cygwin = CPPToolchains.getInstance().getCygwin();

        if (null == cygwin) {
            return null;
        }

        return cygwin.getHomePath();
    }

    /**
     * Get the path of the bash executable
     * @return /usr/bin/bash
     */
    public static String getBathPath() {
        if (isCygwinEnvironment()) {
            return getCygwinRoot() + "\\bin\\bash.exe";
        }

        String[] paths = { "/bin/bash", "/usr/bin/bash" };
        for (String path : paths) {
            if (new File(path).exists()) {
              return path;
            }
        }

        return paths[0];
    }
}
