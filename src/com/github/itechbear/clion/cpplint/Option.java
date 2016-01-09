package com.github.itechbear.clion.cpplint;


import com.github.itechbear.ui.JFilePicker;
import com.github.itechbear.ui.JLabelTextField;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.panels.VerticalLayout;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by HD on 2015/1/1.
 */
public class Option implements Configurable {
  private boolean modified = false;
  private JFilePicker jFilePickerPython;
  private JFilePicker jFilePickerCpplint;
  private JTextField jTextCpplintOptions;
  private OptionModifiedListener listener = new OptionModifiedListener(this);

  public static final String OPTION_KEY_PYTHON = "python";
  public static final String OPTION_KEY_CPPLINT = "cpplint";
  public static final String OPTION_KEY_CPPLINT_OPTIONS = "cpplintOptions";

  @Nls
  @Override
  public String getDisplayName() {
    return "cpplint";
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return null;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    JPanel jPanel = new JPanel();

    VerticalLayout verticalLayout = new VerticalLayout(1, 2);
    jPanel.setLayout(verticalLayout);

    jFilePickerPython = new JFilePicker("Python path:", "...");
    jFilePickerCpplint = new JFilePicker("cpplint.py path:", "...");
    JLabel jLabelCpplintOptions = new JLabel("cpplint.py options:");
    jTextCpplintOptions = new JTextField("", 39);

    reset();

    jFilePickerPython.getTextField().getDocument().addDocumentListener(listener);
    jFilePickerCpplint.getTextField().getDocument().addDocumentListener(listener);
    jTextCpplintOptions.getDocument().addDocumentListener(listener);

    jPanel.add(jFilePickerPython);
    jPanel.add(jFilePickerCpplint);
    jPanel.add(jLabelCpplintOptions);
    jPanel.add(jTextCpplintOptions);

    return jPanel;
  }

  @Override
  public boolean isModified() {
    return modified;
  }

  public void setModified(boolean modified) {
    this.modified = modified;
  }

  @Override
  public void apply() throws ConfigurationException {
    Settings.set(OPTION_KEY_PYTHON, jFilePickerPython.getTextField().getText());
    Settings.set(OPTION_KEY_CPPLINT, jFilePickerCpplint.getTextField().getText());
    Settings.set(OPTION_KEY_CPPLINT_OPTIONS, jTextCpplintOptions.getText());
    modified = false;
  }

  @Override
  public void reset() {
    String python = Settings.get(OPTION_KEY_PYTHON);
    jFilePickerPython.getTextField().setText(python);

    String cpplint = Settings.get(OPTION_KEY_CPPLINT);
    jFilePickerCpplint.getTextField().setText(cpplint);

    String cpplintOptions = Settings.get(OPTION_KEY_CPPLINT_OPTIONS);
    jTextCpplintOptions.setText(cpplintOptions);
    modified = false;
  }

  @Override
  public void disposeUIResources() {
    jFilePickerPython.getTextField().getDocument().removeDocumentListener(listener);
    jFilePickerCpplint.getTextField().getDocument().removeDocumentListener(listener);
    jTextCpplintOptions.getDocument().removeDocumentListener(listener);
  }

  private static class OptionModifiedListener implements DocumentListener {
    private final Option option;

    public OptionModifiedListener(Option option) {
      this.option = option;
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
      option.setModified(true);
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
      option.setModified(true);
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
      option.setModified(true);
    }
  }
}
