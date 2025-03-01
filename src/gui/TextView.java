package gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;

public class TextView extends JTextField {

	public TextView() {
		this("");
	}

	public TextView(String text) {
		super(text);
		
		setFont(new Font("Serif", Font.BOLD, 16));
		setEditable(false);
		setPreferredSize(new Dimension(130, 40));
	}
}
