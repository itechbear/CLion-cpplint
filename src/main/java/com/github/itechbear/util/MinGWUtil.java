package com.github.itechbear.util;

import java.util.List;

import com.jetbrains.cidr.cpp.toolchains.CPPToolchains;
import com.jetbrains.cidr.cpp.toolchains.MinGW;

/**
 * Created by iTech on 2015/10/5.
 */
public class MinGWUtil {
    public static boolean isMinGWEnvironment() {
        final List<CPPToolchains.Toolchain> toolchainList = CPPToolchains.getInstance().getToolchains();
        for (CPPToolchains.Toolchain toolchain : toolchainList) {
            final MinGW minGW = toolchain.getMinGW();
            if (null != minGW && (minGW.isMinGW() || minGW.isMinGW64())) {
                return true;
            }
        }
        return false;
    }
}
