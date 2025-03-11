package main;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame f = new JFrame("Survival 2D Game");
		
		GamePanel gp = new GamePanel();
		
		var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		var gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(f);
		
		f.setContentPane(gp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}

}
