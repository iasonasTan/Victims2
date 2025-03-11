package entity.manager;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import entity.Entity;
import main.GamePanel;

public abstract class EntityManager <E extends Entity & Removable> {
	private List<E> entities = new ArrayList<>();
	protected GamePanel context;
	
	public EntityManager (GamePanel c) {
		context = c;
	}

	abstract public void addEntity(Object... params);
	
	protected synchronized void addEntityToList (E e) {
		entities.add(e);
	}
	
	public synchronized void update () {
		Iterator<E> iter = entities.iterator();
		while (iter.hasNext()) {
			E ent = iter.next();
			ent.update();
			boolean remove = ent.remove();
			if (remove) {
				iter.remove();
			}
		}
	}

	public void filter () {
        entities.removeIf(e -> !context.isInFrame(e, 100));
	}
	
	public void draw (Graphics g) {
		entities.forEach(e -> {
			e.draw(g);
		});
	}
	
	public void forEach (Consumer<E> consumer) {
		entities.forEach(consumer);
	}
	
	public List<E> getList () {
		return entities;
	}
	
	public void reloadResources () {
		for (E e : entities) {
			e.reloadResources();
		}
	}
	
	public Optional<E> getCollider (Entity collider) {
		for (E e : entities) {
			if (e.getRect()==null || e.getRect().equals(collider.getRect())) {
				continue;
			}
			
			if (e.getRect().intersects(collider.getRect())) {
				return Optional.of(e);
			}
		}
		return Optional.empty();
	}

}
