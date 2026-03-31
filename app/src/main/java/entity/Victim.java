package entity;

import javax.swing.ImageIcon;

import main.GamePanel;

public class Victim extends AbstractVictim {

	public Victim(GamePanel c) {
		super(c, 1);
		
		setDefaultValues();
		reloadResources();
	}
	
	@Override
	public void burn() {
		context.gameOver("normal");
		context.getSoundManager().playFile("/sound/score_decreased.wav");
		killed = true;
	}
	
	@Override
	public void reloadResources() {
		super.reloadResources();
		String resourceName = context.isNewGraphics() ? "victim_new.png" : "victim.png";
		image = new ImageIcon(getClass().getResource("/graphics/"+resourceName)).getImage();
	}

	@Override
	public void kill() {
		context.getPlayer().increaseScore();
		context.getSoundManager().playFile("/sound/score_increased.wav");
		killed = true;
	}
	

}
