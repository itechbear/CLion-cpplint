package com.github.itechbear.clion.cpplint;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.itechbear.clion.cpplint.QuickFixes.QuickFixesManager;
import com.github.itechbear.util.CygwinUtil;
import com.github.itechbear.util.MinGWUtil;
import com.google.common.base.Strings;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFixBase;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by HD on 2015/1/1.
 */
public class CpplintRunner {
    private static final Logger LOGGER = Logger.getInstance(CpplintRunner.class);

    private static final Pattern PATTERN = Pattern.compile("^.+:([0-9]+):\\s+(.+)\\s+\\[([^\\]]+)+\\]\\s+\\[([0-9]+)\\]$");

    private CpplintRunner() {
    }

    public static List<ProblemDescriptor> lint(@NotNull PsiFile file,
                                               @NotNull InspectionManager manager,
                                               @NotNull Document document) {
        final String cpplintpyPath = Settings.get(Option.OPTION_KEY_CPPLINT);
        String cpplintOptions = Settings.get(Option.OPTION_KEY_CPPLINT_OPTIONS);

        if (null == cpplintpyPath || cpplintpyPath.isEmpty()) {
            StatusBar.Info.set("Please set path of cpplint.py first!", file.getProject());
            return Collections.emptyList();
        }

        final VirtualFile baseDir = file.getProject().getBaseDir();
        if (null == baseDir) {
            LOGGER.error("No valid base directory found!");
            return Collections.emptyList();
        }
        final String canonicalPath = baseDir.getCanonicalPath();
        if (Strings.isNullOrEmpty(canonicalPath)) {
            LOGGER.error("Failed to get canonical path!");
            return Collections.emptyList();
        }

        // First time users will not have this Option set if they do not open the Settings
        // UI yet.
        if (null == cpplintOptions) {
            cpplintOptions = "";
        }

        final List<String> args = buildCommandLineArgs(cpplintpyPath, cpplintOptions, file);
        return runCpplint(file, manager, document, canonicalPath, args);
    }

    @NotNull
    private static List<ProblemDescriptor> runCpplint(@NotNull PsiFile file,
                                                      @NotNull InspectionManager manager,
                                                      @NotNull Document document,
                                                      @NotNull String canonicalPath,
                                                      @NotNull List<String> args) {
        final File cpplintWorkingDirectory = new File(canonicalPath);
        final ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(cpplintWorkingDirectory);
        final Process proc;
        try {
            proc = pb.start();
        } catch (IOException e) {
            LOGGER.error("Failed to run lint against file: " + file.getVirtualFile().getCanonicalPath(), e);
            return Collections.emptyList();
        }
        final List<ProblemDescriptor> problemDescriptors = new ArrayList<>();
        try (
                final BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                final BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            while (null != stdInput.readLine()) {
            }

            String line = null;
            while ((line = stdError.readLine()) != null) {
                final ProblemDescriptor problemDescriptor = parseLintResult(file, manager, document, line);
                if (null == problemDescriptor) {
                    continue;
                }
                problemDescriptors.add(problemDescriptor);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to run lint against file: " + file.getVirtualFile().getCanonicalPath(), e);
            return Collections.emptyList();
        }

        return problemDescriptors;
    }

    @NotNull
    private static List<String> buildCommandLineArgs(@NotNull String cpplint,
                                                     @NotNull String cpplintOptions,
                                                     @NotNull PsiFile file) {
        final String python = Settings.get(Option.OPTION_KEY_PYTHON);
        String cppFilePath = file.getVirtualFile().getCanonicalPath();
        if (CygwinUtil.isCygwinEnvironment()) {
            cppFilePath = CygwinUtil.toCygwinPath(cppFilePath);
        }
        final List<String> args = new ArrayList<>();
        if (MinGWUtil.isMinGWEnvironment()) {
            args.add(python);
            args.add(cpplint);
            Collections.addAll(args, cpplintOptions.split("\\s+"));
            Collections.addAll(args, cppFilePath);
        } else {
            args.add(CygwinUtil.getBashPath());
            args.add("-c");
            String joinedArgs;
            if (CygwinUtil.isCygwinEnvironment()) {
                joinedArgs = "\"\\\"" + python + "\\\" \\\"" + cpplint + "\\\" " + cpplintOptions + " ";
                joinedArgs += "\\\"" + cppFilePath + "\\\" ";
                joinedArgs += '\"';
            } else {
                joinedArgs = "\"" + python + "\" \"" + cpplint + "\" " + cpplintOptions + " ";
                joinedArgs += "\"" + cppFilePath + "\" ";
            }
            args.add(joinedArgs);
        }
        return args;
    }

    @Nullable
    private static ProblemDescriptor parseLintResult(@NotNull PsiFile file,
                                                     @NotNull InspectionManager manager,
                                                     @NotNull Document document,
                                                     @NotNull String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            return null;
        }
        int lineNumber = Integer.parseInt(matcher.group(1), 10);
        int lineCount = document.getLineCount();
        if (0 == lineCount) {
            return null;
        }
        lineNumber = (lineNumber >= lineCount) ? (lineCount - 1) : lineNumber;
        lineNumber = (lineNumber > 0) ? (lineNumber - 1) : 0;
        final String errorMessage = "cpplint: " + matcher.group(2);
        final String ruleName = matcher.group(3);
        final int confidenceScore = Integer.parseInt(matcher.group(4), 10);
        final int lineStartOffset = document.getLineStartOffset(lineNumber);
        final int lineEndOffset = document.getLineEndOffset(lineNumber);

        // Do not highlight empty whitespace prepended to lines.
        final String text = document.getImmutableCharSequence().subSequence(
                lineStartOffset, lineEndOffset).toString();

        final int numberOfPrependedSpaces = text.length() -
                text.replaceAll("^\\s+", "").length();

        final LocalQuickFixBase fix = QuickFixesManager.get(ruleName);
        return manager.createProblemDescriptor(
                file,
                TextRange.create(lineStartOffset + numberOfPrependedSpaces, lineEndOffset),
                errorMessage,
                ProblemHighlightType.WEAK_WARNING,
                true,
                fix);
    }
}