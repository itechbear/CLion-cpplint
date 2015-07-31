# CLion-cpplint
The first C++ lint plugin for Clion.

Features
========

 - Run cpplint.py on the fly when you are editing C++ source code.
 - Highlight corresponding lines with messages about which cpplint.py complains.
 - Cygwin environment is supported as well.
 
Usage
=====

 - Install this plugin.
 - Go to File -> Settings -> Other Settings -> cpplint option, fill in the <b>*absolute*</b> paths of python and cpplint.py
 - Enjoy developing!

Please note
===========

 - If you are using cygwin, use cygwin python package and fill with unix-style paths in the option dialog(for example, /usr/bin/python and /home/tools/cpplint.py).

Change log
==========

 - 1.0.2 Fixed IndexOutOfBound exception.
 - 1.0.0 Initial commit.
