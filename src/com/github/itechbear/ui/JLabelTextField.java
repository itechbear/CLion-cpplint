package com.github.itechbear.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by HD on 2015/1/2.
 */
public class JLabelTextField extends JPanel {
  private JLabel label;
  private JTextField textField;

  public JLabelTextField(String label) {
    setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    // creates the GUI
    this.label = new JLabel(label);

    textField = new JTextField(10);

    add(this.label);
    add(textField);
  }

  public JLabel getLabel() {
    return label;
  }

  public JTextField getTextField() {
    return textField;
  }
}
