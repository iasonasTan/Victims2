package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import entity.manager.Removable;
import main.GamePanel;

public abstract class Entity {
	protected int x, y;
	protected int width, height;
	protected Rectangle rect;
	
	protected Image image;
	
	protected GamePanel context;

	public Entity (GamePanel c) {
		context = c;
	}
	
	public final void randomLocation () {
		final int X_MIN = width*3;
		final int X_MAX = context.getPreferredSize().width-width*3;
		final int Y_MIN = height*3;
		final int Y_MAX = context.getPreferredSize().height-height*3;
		
		x = (int)(Math.random()*X_MAX-X_MIN)+X_MIN;
		y = (int)(Math.random()*Y_MAX-Y_MIN)+Y_MIN;
		
		updateRect();
	}
	
	public void updateRect () {
		final int GAP = 5;
		rect = new Rectangle(x+GAP, y+GAP, 
				width-GAP*2, height-GAP*2);		
	}
	
	public final int distance (Entity e) {
		Rectangle er = e.getRect();
		int diffX = Math.abs(er.x-x);
		int diffY = Math.abs(er.y-y);
		return Math.abs(diffX*diffX + diffY*diffY);
	}
	
	abstract public void reloadResources();
	abstract public void update();
	abstract public void setDefaultValues();
	abstract public void draw(Graphics g);
	
	public final boolean collides (Entity e) {
		return e.rect.intersects(this.rect);
	}
	
	public Image getImage () {
		return image;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getRect() {
		return rect;
	}
	
	public Point getPoint () {
		return new Point(x, y);
	}
	
	public Point getCenter () {
		int cx = x+width/2;
		int cy = y+height/2;
		return new Point(cx, cy);
	}
	
}
