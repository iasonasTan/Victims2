package entity;

import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.manager.ProjectileManager;
import main.Context;

public class Enemy extends MovableEntity {
	private ProjectileManager projectileM;
	private int counter = 0;

	public Enemy(Context c) {
		super(c, 2);
		projectileM = new ProjectileManager(context, this);
		setDefaultValues();
		reloadResources();
	}
	
	@Override
	public void update () {
		counter++;
		projectileM.update();
		super.update();
		
		// follow player
		int diffX = x-context.getPlayer().x;
		int diffY = y-context.getPlayer().y;
		double distance = Math.sqrt(diffX*diffX + diffY*diffY);
		double directionX = 0;
		double directionY = 0;
		if (distance > 0) {
			directionX = diffX/distance;
			directionY = diffY/distance;
		}
		
		int x = (int)(speed*directionX);
		int y = (int)(speed*directionY);
		
		move(new Point(-x, -y));
		
		updateRect();
		
		if (counter%120==0) {
			projectileM.addProjectile();
		}
		
		if (collides(context.getPlayer())) {
			context.gameOver("force");
		}
	}
	
	@Override
	public void draw (Graphics g) {
		super.draw(g);
		
		projectileM.draw(g);
	}
	
	@Override
	public void reloadResources () {
		super.reloadResources();
		
		String res = context.isNewGraphics() ? "enemy_new.png" : "enemy.png";
		try {
			image = ImageIO.read(getClass().getResource("/graphics/"+res));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setDefaultValues() {
		width = 100;
		height = width;
		
		updateRect();

		do {
			randomLocation();
		} while (collides(context.getPlayer()) || distance(context.getPlayer()) < 200);
		
		updateRect();
	}

	@Override
	public void collect() {
		dash(2);
	}

}
