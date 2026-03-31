package entity;

import javax.swing.ImageIcon;

import main.GamePanel;

public class FakeVictim extends AbstractVictim {

	public FakeVictim(GamePanel c) {
		super(c, 2);
		
		setDefaultValues();
		reloadResources();
		
		int randInt = (int)(Math.random()*6);
		if (randInt == 1) {
			context.getEnergyManager().addEntity(this);
		}
	}
	
	@Override
	public void burn () {
		killed = true;
	}
	
	@Override
	public void reloadResources() {
		super.reloadResources();
		String resourceName = context.isNewGraphics() ? "victim_wrong_new.png" : "victim_wrong.png";
		image = new ImageIcon(getClass().getResource("/graphics/"+resourceName)).getImage();
	}

	@Override
	public void kill() {
		context.getPlayer().decreaseScore();
		context.getSoundManager().playFile("/sound/score_decreased.wav");
		killed = true;
	}

}
