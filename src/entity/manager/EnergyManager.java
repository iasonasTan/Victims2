package entity.manager;

import entity.Energy;
import entity.Victim;
import main.Context;

public final class EnergyManager extends EntityManager<Energy> {

	public EnergyManager(Context c) {
		super(c);
	}
	
	public void addEnergy (Victim parent) {
		addEntity(new Energy(context, parent));
	}

}
