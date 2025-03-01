package entity.manager;

import entity.Victim;
import main.Context;

public final class VictimManager extends EntityManager<Victim> {
	private int counter = 0;
	
	public VictimManager(Context c) {
		super(c);
	}
	
	public void addVictim () {
		addEntity(new Victim(context));
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
