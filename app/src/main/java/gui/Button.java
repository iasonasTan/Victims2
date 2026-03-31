package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button extends JButton {
	public Button (String text, boolean visible, ActionListener al) {
		super(text);
		setVisible(visible);
		addActionListener(al);
		setPreferredSize(new Dimension(130, 40));
		setBackground(GuiManager.COMPONENT_BACKGROUND);
		setForeground(GuiManager.COMPONENT_FOREGROUND);
	}

}
