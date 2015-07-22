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

 - Sometimes Clion would report exceptions with this plugin. Those exceptions are caused by a delay between the end of the cpplint.py execution and the Clion inspection system. For example, when cpplint.py was run by Clion, it finds a problem at line N. Suddenly, you delet all lines starting from N before the inspection system updates, then Clion would report an exception. I haven't found a good way to work around. If you do, please make a pull request.
 - If you are using cygwin, use cygwin python package and fill with unix-style paths in the option dialog(for example, /usr/bin/python and /home/tools/cpplint.py).

