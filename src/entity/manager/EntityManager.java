package entity.manager;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;

import entity.Entity;
import main.GamePanel;
import main.GamePanel;

public abstract class EntityManager <E extends Entity & Removable> {
	private ArrayList<E> entities = new ArrayList<>();
	protected GamePanel context;
	
	public EntityManager (GamePanel c) {
		context = c;
	}
	
	protected synchronized void addEntity (E e) {
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
	
	public void draw (Graphics g) {
		entities.forEach(e -> {
			e.draw(g);
		});
	}
	
	public void forEach (Consumer<E> consumer) {
		entities.forEach(consumer);
	}
	
	public ArrayList<E> getList () {
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
