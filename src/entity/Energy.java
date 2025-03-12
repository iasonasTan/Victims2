package entity;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import main.GamePanel;

public final class Energy extends Entity implements Removable {
	private AbstractVictim parent;
	private boolean used = false;

	public Energy(GamePanel c, AbstractVictim parent) {
		super(c);
		this.x = parent.x;
		this.y = parent.y;
		this.parent = parent;
		
		setDefaultValues();
		reloadResources();
	}

	@Override
	public void reloadResources() {
		String res1Name = context.isNewGraphics() ? "energy_new.png" : "energy.png";
		image = new ImageIcon(getClass().getResource("/graphics/"+res1Name)).getImage();
	}

	@Override
	public void update() {
		for (MovableEntity v: context.getVictimManager().getList())
			collectIfCollides(v);
		
		collectIfCollides(context.getEnemy());
		collectIfCollides(context.getPlayer());
		
	}
	
	private void collectIfCollides (MovableEntity me) {
		if (collides(me) && !me.equals(parent)) {
			used = true;
			me.collect();
		}
	}
	
	@Override
	public void draw (Graphics g) {
		// open/closed principle
		g.drawImage(image, x, y, width, height, null);
	}

	@Override
	public void setDefaultValues() {
		width = 30;
		height = width;
		
		updateRect();
	}

	@Override
	public boolean remove() {
		return used;
	}

}
