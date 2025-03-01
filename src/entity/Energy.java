package entity;

import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.Optional;

import javax.swing.ImageIcon;

import entity.manager.EntityManager;
import main.Context;

public class Energy extends Entity {
	private Victim parent;
	private boolean burnable = true;

	public Energy(Context c, Victim parent) {
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
		if (!burnable) {
			return;
		}
		
		for (MovableEntity v: context.getVictimManager().getList())
			collectIfCollides(v);
		
		collectIfCollides(context.getEnemy());
		collectIfCollides(context.getPlayer());
		
	}
	
	private void collectIfCollides (MovableEntity me) {
		if (collides(me) && !me.equals(parent)) {
			burn();
			me.collect();
		}
	}
	
	@Override
	public void draw (Graphics g) {
		// open/closed principle
		g.drawImage(image, x, y, width, height, null);
	}
	
	public boolean isBurnable () {
		return burnable;
	}
	
	public void burn () {
		context.getEnergyManager().removeEntity(this);
		burnable = false;
	}

	@Override
	public void setDefaultValues() {
		width = 30;
		height = width;
		
		updateRect();
	}

}
