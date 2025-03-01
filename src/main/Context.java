package main;

import entity.Enemy;
import entity.Player;
import entity.manager.EnergyManager;
import entity.manager.VictimManager;
import gui.GuiManager;

public interface Context {
	Player getPlayer();
	Enemy getEnemy();
	
	EnergyManager getEnergyManager();
	VictimManager getVictimManager();
	
	GamePanel getPanel();
	KeyHandler getKeyHandler();
	SoundManager getSoundManager();
	GuiManager getGuiManager();
	
	void gameOver(String mode);
	
	boolean isNewGraphics();
	boolean isMusicOn();
}
