package com.github.itechbear.clion.cpplint;

import com.github.itechbear.util.CygwinUtil;
import com.github.itechbear.util.MinGWUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by HD on 2015/1/1.
 */
public class CpplintCommand {
  public static String execute(Project project, String... arg) throws IOException {
    List<String> args = new ArrayList<String>();

    String python = Settings.get(Option.OPTION_KEY_PYTHON);
    String cpplint = Settings.get(Option.OPTION_KEY_CPPLINT);
    String cpplintOptions = Settings.get(Option.OPTION_KEY_CPPLINT_OPTIONS);

    if (null == cpplint || cpplint.isEmpty()) {
      StatusBar.Info.set("Please set path of cpplint.py first!", project);
      return "";
    }

    // First time users will not have this Option set if they do not open the Settings
    // UI yet.
    if (null == cpplintOptions) {
      cpplintOptions = "";
    }

    if (MinGWUtil.isMinGWEnvironment()) {
      args.add(python);
      args.add(cpplint);
      Collections.addAll(args, cpplintOptions.split("\\s+"));
      Collections.addAll(args, arg);
    }
    else
    {
      args.add(CygwinUtil.getBashPath());
      args.add("-c");
      String joinedArgs;
      if (CygwinUtil.isCygwinEnvironment()){
        joinedArgs = "\"\\\"" + python + "\\\" \\\"" + cpplint + "\\\" " + cpplintOptions + " ";
        for (String oneArg : arg)
          joinedArgs += "\\\"" + oneArg + "\\\" ";
        joinedArgs += '\"';
      }
      else {
        joinedArgs = "\"" + python + "\" \"" + cpplint + "\" " + cpplintOptions + " ";
        for (String oneArg : arg)
          joinedArgs += "\"" + oneArg + "\" ";
      }
      args.add(joinedArgs);
    }

    File cpplintWorkingDirectory = new File(project.getBaseDir().getCanonicalPath());
    final Process process = Runtime.getRuntime().exec(
        args.toArray(new String[args.size()]), null, cpplintWorkingDirectory);

    final StringBuilder outString = new StringBuilder();
    Thread outThread = new Thread(new Runnable() {
      @Override
      public void run() {
        BufferedReader outStream = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
        String line;
        try {
          while ((line = outStream.readLine()) != null) {
            outString.append(line + "\n");
          }
        } catch (IOException ex) {
          ex.printStackTrace();
        } finally {
          try {
            outStream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
    outThread.start();

    final StringBuilder errString = new StringBuilder();
    Thread errorThread = new Thread(new Runnable() {
      @Override
      public void run() {
        BufferedReader errStream = new BufferedReader(new
            InputStreamReader(process.getErrorStream()));
        String line;
        try {
          while ((line = errStream.readLine()) != null) {
            errString.append(line + "\n");
          }
        } catch (IOException ex) {
          ex.printStackTrace();
        } finally {
          try {
            errStream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
    errorThread.start();

    try {
      outThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      errorThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return errString.toString();
  }
}