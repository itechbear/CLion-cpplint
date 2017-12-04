# CLion-cpplint
The first C++ lint plugin for Clion.

Features
========

 - Run cpplint.py on the fly when you are editing C++ source code.
 - Highlight corresponding lines with messages about which cpplint.py complains.
 - Cygwin environment is supported as well.

Installation
==========

 - [jetbrains plugin repository](https://plugins.jetbrains.com/plugin/7871?pr=clion) 
 
Usage
=====

 - Install this plugin.
 - Go to File -> Settings -> Other Settings -> cpplint option, fill in the <b>*absolute*</b> paths of python and cpplint.py
 - The plugin should start working when you open/edit C++ files. Don't bother finding menus/actions to run this plugin, because there're not. Enjoy developing!

Please note
===========

 - For **Cygwin** users: use cygwin python package and fill with unix-style paths in the option dialog(for example, /usr/bin/python and /home/tools/cpplint.py).
 - For **MinGW** users: use windows-style paths of python and cpplint.py(for example, C:\Python27\python.exe and C:\Users\user\cpplint.py).

Change log
==========

 - 1.0.8 Code improvements. Refactored code.
 - 1.0.7 Bugfix. To be compatible with CLion 2017.3.
 - 1.0.6 Bugfix. Reverted a change that broke this plugin.
 - 1.0.5 Bugfixes. See github issues #10,#11,#16. Thanks johnthagen and timothyolt!
 - 1.0.4 Bugfixes. Thanks johnthagen and mherb!
 - 1.0.3 Add support for MinGW. Degrade to JDK 6.
 - 1.0.2 Fixed IndexOutOfBound exception.
 - 1.0.0 Initial commit.
