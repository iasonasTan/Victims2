package entity;

import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Enemy extends MovableEntity {
	private EntityManager<Projectile> projectileM = new EntityManager<>(context) {
		@Override
		public void addEntity(Object... params) {
			addEntityToList(new Projectile(context, getCenter(), context.getPlayer().getCenter()));
		}
		@Override
		public void update(){
			super.update();
			super.filter();
		}
	};
	private int counter = 0;

	public Enemy(GamePanel c) {
		super(c, 2);
		setDefaultValues();
		reloadResources();
	}
	
	@Override
	public void update () {
		counter++;
		projectileM.update();
		super.update();
		
		Player player = context.getPlayer();
		PointDb direction = MovableEntity.getDirection(getPoint(), player.getPoint());
		
		int x = (int)(speed*direction.x());
		int y = (int)(speed*direction.y());
		
		move(new Point(-x, -y));
		
		updateRect();
		
		if (counter%120==0) {
			projectileM.addEntity();
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
		dash(2, 4);
	}

}
