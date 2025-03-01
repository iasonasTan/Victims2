package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HowToPlayDialog extends JPanel {
	private ActionListener disposeEvent;
	private final String file = "/txt/how_to_play.txt";
	
	private final JTextArea text = new JTextArea();
	private final JButton closeBtn = new JButton("Close");

	public HowToPlayDialog(ActionListener closeFrame) {
		this.disposeEvent = closeFrame;
		initGui();
		
	}
	
	private void initGui () {
		setPreferredSize(new Dimension(400,300));
		setLayout(new VerticalFlowLayout(this));
		
		add(text);
		add(closeBtn);
		
		loadInstructionsFromDisk();
		
		closeBtn.addActionListener(disposeEvent);
	}
	
	public void loadInstructionsFromDisk () {
		try {
			var is = getClass().getResourceAsStream(file);
			String doc = new String(is.readAllBytes());
			text.setText(doc);
		} catch (Exception e) {
			String message = "Cannot read file "+file+", "+e.toString();
			text.setText(message);
			e.printStackTrace();
		}
	}
	
	public static void show (Component location) {
		JDialog frame = new JDialog();
		HowToPlayDialog htpd = new HowToPlayDialog(e -> {
			frame.dispose();
		});
		
		frame.setTitle("How to play");
		frame.setContentPane(htpd);
		frame.pack();
		frame.setLocationRelativeTo(location);
		frame.setVisible(true);
	}

}
