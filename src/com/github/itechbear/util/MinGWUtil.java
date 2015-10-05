package com.github.itechbear.util;

import com.jetbrains.cidr.cpp.CPPToolchains;
import com.jetbrains.cidr.cpp.toolchains.MinGW;

/**
 * Created by iTech on 2015/10/5.
 */
public class MinGWUtil {
    public static boolean isMinGWEnvironment() {
        MinGW minGW = CPPToolchains.getInstance().getMinGW();

        return null != minGW && (minGW.isMinGW() || minGW.isMinGW64());
    }
}
