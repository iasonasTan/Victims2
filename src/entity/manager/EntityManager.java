package entity.manager;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

import entity.Entity;
import main.Context;
import main.GamePanel;

public abstract class EntityManager <E extends Entity> {
	private ArrayList<E> entities = new ArrayList<>();
	protected Context context;
	private ArrayList<E> markedRemovable = new ArrayList<>();
	
	public EntityManager (Context c) {
		context = c;
	}
	
	protected synchronized void addEntity (E e) {
		entities.add(e);
	}
	
	public synchronized void removeEntity (E e) {
		markedRemovable.add(e);
	}
	
	public synchronized void update () {
//		entities.forEach(E::update);
		for (E e : entities) {
			e.update();
		}
		
		for (E ent : markedRemovable) {
			entities.remove(ent);
		}
		markedRemovable.clear();
		
		removeInvisibles();
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

	public boolean removeInvisibles() {
		Rectangle rect = new Rectangle(GamePanel.rect);
		final int BORDER_SIZE = 10;
		rect.x -= BORDER_SIZE;
		rect.y -= BORDER_SIZE;
		rect.width += BORDER_SIZE*2;
		rect.height += BORDER_SIZE*2;
		boolean out = false;
		for (int i=0; i<entities.size(); i++) {
			E e = entities.get(i);
			if (!rect.intersects(e.getRect())) {
				removeEntity(e);
				out = true;
			}
		}
		return out;
	}

}
