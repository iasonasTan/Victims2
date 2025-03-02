package entity.manager;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class ProjectileManager extends EntityManager<Projectile> /* implements MouseListener */ {
	private Entity parent;

	public ProjectileManager(GamePanel c, Entity parent) {
		super(c);
		this.parent = parent;
	}
	
	public void addProjectile () {
		addEntity(new Projectile(context, parent.getCenter(), context.getPlayer().getCenter()));
	}

}
