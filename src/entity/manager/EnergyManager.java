package entity.manager;

import entity.AbstractVictim;
import entity.Energy;
import main.GamePanel;

public final class EnergyManager extends EntityManager<Energy> {

	public EnergyManager(GamePanel c) {
		super(c);
	}
	
	public void addEnergy (AbstractVictim parent) {
		addEntity(new Energy(context, parent));
	}

}
