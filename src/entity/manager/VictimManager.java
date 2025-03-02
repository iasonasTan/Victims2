package entity.manager;

import entity.AbstractVictim;
import entity.FakeVictim;
import entity.Victim;
import main.GamePanel;

public final class VictimManager extends EntityManager<AbstractVictim> {
	private int counter = 0;
	
	public VictimManager(GamePanel c) {
		super(c);
	}
	
	public void addVictim () {
		AbstractVictim victim;
		int randInt = (int)(Math.random()*100);
		
		if (randInt < 40) {
			victim = new Victim(context);
		} else {
			victim = new FakeVictim(context);
		}
		
		addEntity(victim);
	}
	
	@Override
	public void update () {
		super.update();
		//super.removeInvisibles();
		
		counter++;
		if (counter%65==0) {
			addVictim();
		}
	}
	
//	private class EnemyAdder implements Runnable {
//		private Thread thread;
//	
//		public void start () {
//			thread = new Thread(this);
//			thread.start();
//		}
//	
//		@Override
//		public void run() {
//			while (true) {
//				try {
//					Thread.sleep(3000);
//					addEnemy();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//	}
}
