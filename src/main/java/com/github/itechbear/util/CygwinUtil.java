package com.github.itechbear.util;

import java.io.File;
import java.util.List;

import com.jetbrains.cidr.cpp.toolchains.CPPToolchains;
import com.jetbrains.cidr.cpp.toolchains.Cygwin;

/**
 * Created by HD on 2015/1/3.
 */
public class CygwinUtil {
    public static boolean isCygwinEnvironment() {
        final List<CPPToolchains.Toolchain> toolchainList = CPPToolchains.getInstance().getToolchains();
        for (CPPToolchains.Toolchain toolchain : toolchainList) {
            final Cygwin cygwin = toolchain.getCygwin();
            if (null != cygwin && (cygwin.isCygwin() || cygwin.isCygwin64())) {
                return true;
            }
        }
        return false;
    }

    public static String toCygwinPath(String path) {
        final List<CPPToolchains.Toolchain> toolchainList = CPPToolchains.getInstance().getToolchains();
        for (CPPToolchains.Toolchain toolchain : toolchainList) {
            final Cygwin cygwin = toolchain.getCygwin();
            if (null != cygwin) {
                return Cygwin.toCygwinPath(path, cygwin);
            }
        }
        return path;
    }

    /**
     * Get the root path of cygwin.
     *
     * @return String like C:\Cygwin
     */
    public static String getCygwinRoot() {
        final List<CPPToolchains.Toolchain> toolchainList = CPPToolchains.getInstance().getToolchains();
        for (CPPToolchains.Toolchain toolchain : toolchainList) {
            final Cygwin cygwin = toolchain.getCygwin();
            if (null != cygwin) {
                return cygwin.getHomePath();
            }
        }
        return null;
    }

    /**
     * Get the path of the bash executable
     *
     * @return /usr/bin/bash
     */
    public static String getBashPath() {
        if (isCygwinEnvironment()) {
            return getCygwinRoot() + "\\bin\\bash.exe";
        }

        String[] paths = {"/bin/bash", "/usr/bin/bash"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return path;
            }
        }

        return paths[0];
    }
}
