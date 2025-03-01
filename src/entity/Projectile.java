package entity;

import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import main.Context;

public class Projectile extends MovableEntity {
	private double directionX = 0;
	private double directionY = 0;

	public Projectile(Context c, Point locationCenter, Point targetCenter) {
		super(c, 7);
		
		setDefaultValues();
		x = locationCenter.x-width/2;
		y = locationCenter.y-height/2;
		updateRect();
		reloadResources();
		calculateDirection(targetCenter);
	}
	
	public void calculateDirection (Point target) {
		double diffX = target.x - x;
		double diffY = target.y - y;
		double distance = Math.sqrt(diffX*diffX + diffY*diffY);
		if (distance!=0) {
			directionX = diffX/distance;
			directionY = diffY/distance;
		}
	}
	
	@Override
	public void update () {
		super.update();
		
		int dX = (int)(speed*directionX);
		int dY = (int)(speed*directionY);
		move(new Point(dX, dY));
		
		updateRect();
		
		if (collides(context.getPlayer())) {
			context.gameOver("force");
		}
	}

	@Override
	public void setDefaultValues() {
		width = 40;
		height = width;
	}
	
	@Override
	public void reloadResources () {
		super.reloadResources();
		
		String res = context.isNewGraphics() ? "projectile_new.png" : "projectile.png";
		image = new ImageIcon(getClass().getResource("/graphics/"+res)).getImage();
//		try {
//			image = ImageIO.read(getClass().getResource("/graphics/"+res));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void collect() {
		dash(2);
		
	}

}
