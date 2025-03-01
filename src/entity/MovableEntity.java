package entity;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import gui.GuiManager;
import main.Context;

public abstract class MovableEntity extends Entity implements Collector {
	protected int defaultSpeed, speed;
	private boolean onDash = false;
	protected Direction direction = Direction.DOWN;
	protected BufferedImage dashImage;

	public MovableEntity(Context c, int defaultSpeed) {
		super(c);
		this.defaultSpeed = defaultSpeed;
		speed = defaultSpeed;
	}
	
	public void reloadResources() {
		String resource = context.isNewGraphics() ? "dash_new.png" : "dash.png";
		try {
			dashImage = ImageIO.read(getClass().getResource("/graphics/"+resource));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		if (onDash) {
			final int DIFF = 40;
			g.drawImage(dashImage, x-DIFF, y-DIFF, width+DIFF*2, height+DIFF*2, null);
		}
		g.drawImage(image, x, y, width, height, null);
	}
	
	public void move (Point steps) {
		if (Math.abs(steps.x) > Math.abs(steps.y))
			direction = steps.x > 0 ? Direction.RIGHT : Direction.LEFT;
		else
			direction = steps.y > 0 ? Direction.DOWN : Direction.UP;
		
		this.x += steps.x;
		this.y += steps.y;
	}

	@Override
	public void update() {
		reloadResources(); // reset image
				// default image points on left
				// i know it's wrong :-/
		
		int degrees = 0;
		switch (direction) {
			case UP:
				degrees = 90;
				break;
			case DOWN:
				degrees = 270;
				break;
			case RIGHT:
				degrees = 180;
				break;
			case LEFT:
				degrees = 0;
				break;
		}
		
		dashImage = GuiManager.rotateImage(dashImage, degrees);
	}
	
	@Override abstract public void setDefaultValues();
	
	public void dash (int secs) {
		speed+=3;
		onDash = true;
		
		Runnable reset = () -> {
			onDash = false;
			this.speed = defaultSpeed;
		};
		
		var ses = Executors.newSingleThreadScheduledExecutor();
		ses.schedule(reset, secs, TimeUnit.SECONDS);
		ses.shutdown();
	}
	
	public static enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT;
		
		public static Direction opposite (Direction d) {
			switch (d) {
				case UP: return DOWN;
				case DOWN: return UP;
				case LEFT: return RIGHT;
				default: return LEFT;
			}
		}
		
		public static Direction random () {
			final int LEN = 4;
			int randInt = (int)(Math.random()*LEN);
			
			switch (randInt) {
				case 0: return UP;
				case 1: return DOWN;
				case 2: return RIGHT;
				case 3: return LEFT;
				default: return DOWN;
			}
		}
	}
	
	public boolean isOnDash () {
		return onDash;
	}

}
