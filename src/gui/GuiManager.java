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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
	
	private class SettingsPanel extends JPanel {
		private final JCheckBox newGraphicsCheck = new JCheckBox("new graphics");
		private final JCheckBox musicCheck = new JCheckBox("Music");
		private final JComboBox<String> movementMethodBox = new JComboBox<>(
				new String[]{"mouse-based", "key-based"});
		private final Button saveAndExitBtn = new Button("Save and Exit", true, null);
		
		private PanelWithProperties pwp;
		
		public SettingsPanel (PanelWithProperties pwp) {
			this.pwp = pwp;
			
			initUI();
			loadSavedSettings();
		}
		
		private void initUI () {
			setBackground(new Color(0,0,0,60));
			setPreferredSize(new Dimension(300,400));
			setLayout(new VerticalFlowLayout(5));
			
			saveAndExitBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Properties p = new Properties();
					p.setProperty("newGraphics", ""+newGraphicsCheck.isSelected());
					p.setProperty("music", ""+musicCheck.isSelected());
					p.setProperty("movementMethod", (String)movementMethodBox.getSelectedItem());
					
					DataStorage.SettingsStorage.writePropertiesToDisk(p);
					pwp.updateProperties(p);
					
					setVisible(false);
				}
			});
			
			add(newGraphicsCheck);
			add(musicCheck);
			add(movementMethodBox);
			
			add(saveAndExitBtn);
		}
		
		private void loadSavedSettings () {
			Properties savedProperties = DataStorage.SettingsStorage.loadPropertiesFromDisk();
			newGraphicsCheck.setSelected(savedProperties.getProperty("newGraphics").equals("true"));
			musicCheck.setSelected(savedProperties.getProperty("music").equals("true"));
			movementMethodBox.setSelectedItem(savedProperties.getProperty("movementMethod"));
		}
		
		@Override
		public void setVisible (boolean visible) {
			super.setVisible(visible);
			Component[] components = getComponents();
			for (Component c: components) {
				c.setVisible(visible);
			}
			
			panel.setVisible(!visible, Button.class);
			panel.setVisible(!visible, TextView.class);
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
				sp.setVisible(true);
			} else if (bSource.equals(howToPlayBtn)) {
				HowToPlayDialog.show(panel);
			}
			else {
				throw new RuntimeException("unhandled event "+bSource.getText());
			}
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
			panel.setVisible(buttonsVisible, Button.class);
			ESCBtn.setVisible(true);
			panel.repaint();
		}
		
	};
	
	public void restartGame () {
		panel.restartGame();
		panel.setVisible(false, Button.class);
		
		ESCBtn.setVisible(true);
		ESCBtn.setText("Pause Game");
		dashCountView.setText("dash(shift): 0");
	}

	public void gameOver() {
		panel.setVisible(false, Button.class);
		restartBtn.setVisible(true);
		exitBtn.setVisible(true);
	}
	
	public void displayForgivable (int val) {
		forgivesView.setText("forgivable: "+val);
	}
	
	public void displayScore (String score) {
		scoreView.setText("score: "+score);
	}

	public void displayDashCount(int dashCount) {
		dashCountView.setText("dash (shift): "+dashCount);	
	}
	
	public void clickESC () {
		ESCBtn.doClick();
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

}
