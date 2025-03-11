package entity;

import java.awt.Rectangle;

import entity.manager.Removable;
import main.GamePanel;

public abstract class AbstractVictim extends MovableEntity implements Removable {
	private Runnable moveCommand;
	protected boolean killed = false;

	protected AbstractVictim(GamePanel c, int defaultSpeed) {
		super(c, defaultSpeed);
	}
	
	abstract public void kill(); // when hits player
	abstract public void burn(); // when gets out of frame
	
	@Override
	public boolean remove () {
		return killed;
	}

	@Override
	public void update() {
		super.update();
		moveCommand.run();
		updateRect();
		
		if (!context.isInFrame(this, 0)) {
			burn();
		}
	}

	@Override
	public void setDefaultValues() {
		direction = Direction.random();
		generateMoveCommand();
		
		width = 100;
		height = width;
		
		rect = new Rectangle(x, y, width, height);
		
		do {
			randomLocation();
		} while (collides(context.getPlayer()));
		
		updateRect();
	}
	
	private void generateMoveCommand () {
		switch (direction) {
			case UP: moveCommand = () -> y -= speed; break;
			case DOWN: moveCommand = () -> y += speed; break;
			case LEFT: moveCommand = () -> x -= speed; break;
			case RIGHT: moveCommand = () -> x += speed; break;
		}
	}

	@Override
	public void collect() { // when hits energy
		dash(2, 3);	
	}

}
