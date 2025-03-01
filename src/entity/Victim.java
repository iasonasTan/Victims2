package entity;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

import main.Context;
import main.GamePanel;

public final class Victim extends MovableEntity {
	private boolean reportable = true;
	private boolean burnable;
	private Runnable moveCommand;

	public Victim(Context c) {
		super(c, 1);
		
		setDefaultValues();
		reloadResources();
		
		int randInt = (int)(Math.random()*6);
		if (randInt == 1) {
			context.getEnergyManager().addEnergy(this);
		}
		
		generateMoveCommand();
		super.update();
	}

	@Override
	public void update() {
		moveCommand.run();

		if (!GamePanel.rect.intersects(rect) && reportable && burnable) {
			context.gameOver("normal");
			reportable = false;
		}

		updateRect();
	}

	@Override
	public void setDefaultValues() {
		direction = Direction.random();
		int randInt = (int)(Math.random()*2);
		burnable = randInt==1 ? true : false;
		if (!burnable)
			reportable = burnable;
		
		width = 100;
		height = width;
		
		rect = new Rectangle(x, y, width, height);
		
		do {
			randomLocation();
		} while (collides(context.getPlayer()));
		
		updateRect();
	}
	
	public void generateMoveCommand () {
		switch (direction) {
			case UP: moveCommand = () -> y -= speed; break;
			case DOWN: moveCommand = () -> y += speed; break;
			case LEFT: moveCommand = () -> x -= speed; break;
			case RIGHT: moveCommand = () -> x += speed; break;
		}
	}
	
	public boolean isBurnable () {
		return burnable;
	}
	
	public void burn() {
		if (burnable) {
			reportable = false;
			context.getPlayer().increaseScore();
			context.getSoundManager().playFile("/sound/score_increased.wav", false);
		} else {
			context.getPlayer().decreaseScore();
			context.getSoundManager().playFile("/sound/score_decreased.wav", false);
		}
		context.getVictimManager().removeEntity(this);
	}

	@Override
	public void reloadResources() {
		super.reloadResources();
		
		String resourceName;
		if (burnable) {
			resourceName = context.isNewGraphics() ? "victim_new.png" : "victim.png";
		} else {
			resourceName = context.isNewGraphics() ? "victim_wrong_new.png" : "victim_wrong.png";
		}
		image = new ImageIcon(getClass().getResource("/graphics/"+resourceName)).getImage();
	}

	@Override
	public void collect() {
		dash(2);
		
	}

}
