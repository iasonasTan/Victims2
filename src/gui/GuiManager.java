package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import main.DataStorage;
import main.GamePanel;
import main.PanelWithProperties;

public class GuiManager {
	private GamePanel panel;

	private final ButtonEvents beh = new ButtonEvents();
	private final SettingsPanel sp;
	
	private final Button restartBtn = new Button("Restart", false, beh);
	private final Button exitBtn = new Button("Exit", false, beh);
	private final Button ESCBtn = new Button("ESC", true, beh);
	private final Button settingsBtn = new Button("Settings", false, beh);
	private final Button howToPlayBtn = new Button("How To Play?", false, beh);
	
	private final TextView scoreView = new TextView();
	private final TextView forgivesView = new TextView();
	private final TextView dashCountView = new TextView("dash(shift): 0");
	
	public GuiManager (GamePanel p) {
		panel = p;
		sp = new SettingsPanel(panel);
	}
	
	public static BufferedImage rotateImage (BufferedImage originalImage, int angle) {
		double radians = Math.toRadians(angle);
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		
		BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());
		Graphics2D g2d = rotatedImage.createGraphics();
		AffineTransform transform = AffineTransform.getRotateInstance(radians, width/2, height/2);
		g2d.setTransform(transform);
		g2d.drawImage(originalImage, 0, 0, null);
		g2d.dispose();
		
		return rotatedImage;
	}
	
	public void initGui () {
		panel.setLayout(new FlowLayout());
		
		sp.setVisible(false);
		
		panel.add(scoreView);
		panel.add(forgivesView);
		panel.add(dashCountView);

		panel.add(restartBtn);
		panel.add(ESCBtn);
		panel.add(exitBtn);
		panel.add(settingsBtn);
		panel.add(sp);
		panel.add(howToPlayBtn);
	}
	
	public void setSettingsVisible (boolean v) {
		sp.setVisible(v);
		for (Component c : panel.getComponents()) {
			if (c instanceof Button || c instanceof TextView) {
				c.setVisible(!v);
			}
		}
	}
	
	private class SettingsPanel extends JPanel {
		private final JCheckBox newGraphicsCheck = new JCheckBox("new graphics");
		private final JCheckBox musicCheck = new JCheckBox("Music");
		private final Button saveAndExitBtn = new Button("Save and Exit", true, new SaveAndExitListener());
		
		private PanelWithProperties pwp;
		
		public SettingsPanel (PanelWithProperties pwp) {
			this.pwp = pwp;
			
			setLSD();
			addComponents();
			loadSavedSettings();
		}
		
		private void loadSavedSettings () {
			Properties savedProperties = DataStorage.SettingsStorage.loadPropertiesFromDisk();
			newGraphicsCheck.setSelected(savedProperties.get("newGraphics").equals("true"));
			musicCheck.setSelected(savedProperties.get("music").equals("true"));
		}
		
		private class SaveAndExitListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				Properties p = new Properties();
				p.put("newGraphics", ""+newGraphicsCheck.isSelected());
				p.put("music", ""+musicCheck.isSelected());
				
				DataStorage.SettingsStorage.writePropertiesToDisk(p);
				pwp.updateProperties(p);
				
				setSettingsVisible(false);
			}
		}
		
		private void addComponents () {
			add(newGraphicsCheck);
			add(musicCheck);
			
			add(saveAndExitBtn);
		}
		
		private void setLSD () {
			setBackground(new Color(0,0,0,60));
			setPreferredSize(new Dimension(200,200));
		}
		
		@Override
		public void setVisible (boolean visible) {
			super.setVisible(visible);
			Component[] components = getComponents();
			for (Component c: components) {
				c.setVisible(visible);
			}
		}
		
	};
	
	private class ButtonEvents implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Button bSource = (Button)e.getSource();
			if (bSource.equals(ESCBtn)) {
				showHideMenu();
			} else if (bSource.equals(exitBtn)) {
				panel.save();
				System.exit(0);
			} else if (bSource.equals(restartBtn)) {
				restartGame();
			} else if (bSource.equals(settingsBtn)) {
				setSettingsVisible(true);
			} else if (bSource.equals(howToPlayBtn)) {
				HowToPlayDialog.show(panel);
			}
			else {
				throw new RuntimeException("unhandled event "+bSource.getText());
			}
		}
		
		public void restartGame () {
			panel.restartGame();
			
			restartBtn.setVisible(false);
			exitBtn.setVisible(false);
			settingsBtn.setVisible(false);
			howToPlayBtn.setVisible(false);
			
			ESCBtn.setVisible(true);
			ESCBtn.setText("Pause Game");
			dashCountView.setText("dash(shift): 0");
		}
		
		public void showHideMenu () {
			boolean buttonsVisible = false;
			if (!panel.isPaused()) {
				buttonsVisible = true;
				ESCBtn.setText("Resume Game");
				panel.pause();
			} else {
				ESCBtn.setText("Pause Game");
				panel.resume();
			}
			exitBtn.setVisible(buttonsVisible);
			restartBtn.setVisible(buttonsVisible);
			settingsBtn.setVisible(buttonsVisible);
			howToPlayBtn.setVisible(buttonsVisible);
			panel.repaint();
		}
		
	};
	
	public void displayForgivable (int val) {
		forgivesView.setText("forgivable: "+val);
	}
	
	public void displayScore (String score) {
		scoreView.setText("score: "+score);
	}
	
	public void clickESC () {
		ESCBtn.doClick();
	}

	public void gameOver() {
		restartBtn.setVisible(true);
		exitBtn.setVisible(true);
		ESCBtn.setVisible(false);
	}

	public void displayDashCount(int dashCount) {
		dashCountView.setText("dash (shift): "+dashCount);
		
	}

}
