package entity.manager;

import entity.Entity;
import entity.Projectile;
import main.Context;

public class ProjectileManager extends EntityManager<Projectile> /* implements MouseListener */ {
	private Entity parent;

	public ProjectileManager(Context c, Entity parent) {
		super(c);
		this.parent = parent;
	}
	
	public void addProjectile () {
		addEntity(new Projectile(context, parent.getCenter(), context.getPlayer().getCenter()));
	}

}
