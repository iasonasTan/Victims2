package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import entity.Enemy;
import entity.Entity;
import entity.Player;
import entity.manager.EnergyManager;
import entity.manager.VictimManager;
import gui.GuiManager;
import gui.TextView;

public final class GamePanel extends JPanel implements PanelWithProperties {
	
	// configuration-rules
	private boolean newGraphics = false;
	private final int DEFAULT_GAME_OVER_COUNTER = 4;
	private boolean musicOn;
	private Dimension SCREEN_SIZE;
	private Rectangle rect;
	private Properties properties;
	
	// status
	private boolean gameOver = false;
	private boolean paused;
	private int gameOverCounter = DEFAULT_GAME_OVER_COUNTER;
	
	// components
	private volatile Thread gameThread;
	private KeyHandler keyH;
	private GuiManager guiM;
	private SoundManager soundM;
	private Image backgroundImage;
	
	// entities
	private VictimManager victimM;
	private Player player;
	private EnergyManager energyM;
	private Enemy enemy;
	
	public GamePanel () {
		Toolkit tk = Toolkit.getDefaultToolkit();
		SCREEN_SIZE = tk.getScreenSize();
		setPreferredSize(SCREEN_SIZE);
		rect = new Rectangle(SCREEN_SIZE);
		
		loadSavedProperties();
		initGame();
		
		addKeyListener(keyH);
		setFocusable(true);
		requestFocus();
		
		soundM.setMusic("/sound/music.wav");
		if (!musicOn)
			soundM.stopMusic();
		guiM.clickESC();
	}
	
	public void setVisible(boolean visible, Class<?> clazz) {
		for (Component c: getComponents()) {
			if (clazz.isAssignableFrom(c.getClass()))
				c.setVisible(visible);
		}
	}
	
	public void loadSavedProperties() {
		properties = DataStorage.SettingsStorage.loadPropertiesFromDisk();
		newGraphics = properties.get("newGraphics").equals("true");
		musicOn = properties.get("music").equals("true");
	}
	
	public void initGame () {
		try {
			backgroundImage = ImageIO.read(getClass().getResource("/graphics/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		keyH = new KeyHandler();
		initGameEntities();
		soundM = new SoundManager();
		guiM = new GuiManager(this);
		guiM.initGui();
		keyH.setOnEscape(guiM::clickESC);
	}
	
	public void initGameEntities () {
		gameOverCounter = DEFAULT_GAME_OVER_COUNTER;
		victimM = new VictimManager(this);
		player = new Player(this);
		energyM = new EnergyManager(this);
		enemy = new Enemy(this);
	}
	
	public void restartGame () {
		stopGameThread();
		initGameEntities();
		startGame();		
	}
	
	public boolean isInFrame (Entity e) { // use delay
		Rectangle entityRect = new Rectangle(e.getRect());
		final int GAP = 10;
		entityRect.x -= GAP;
		entityRect.y -= GAP;
		entityRect.width+=GAP*2;
		entityRect.height+=GAP*2;
		if (!rect.intersects(entityRect)) {
			return false;
		}
		
		return true;
	}
	
	public void startGame () {
		if (gameOver) {
			initGameEntities();
		}
		paused = false;
		gameOver = false;
		
		gameThread = new Thread(this::loop);
		gameThread.start();
	}
	
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height, null);
		
		player.draw(g);
		victimM.draw(g);
		energyM.draw(g);
		enemy.draw(g);
		
		guiM.displayForgivable(gameOverCounter);
		
		if (paused || gameOver) {
			g.setColor(new Color(0, 0, 0, 80));
			g.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);
		}
		
		if (gameOver) {
			g.setColor(Color.YELLOW);
			g.drawString("Game Over!", 80, 80);
		}
	}
	
	public void update () {
		player.update();
		victimM.update();
		energyM.update();
		enemy.update();
		
		if (!rect.intersects(player.getRect())) {
			gameOver("normal");
			soundM.playFile("/sound/gameOver.wav");
			player.decreaseScore();
			player.setDefaultValues();
		}
	}
	
	public void gameOver (String mode) {
		if (mode.equals("normal")) {
			gameOverCounter--;
			
			if (gameOverCounter <= 0) {
				gameOver("force");
			}
		} else if (mode.equals("force")) {
			guiM.gameOver();
			gameOver=true;
			soundM.playFile("/sound/gameOver.wav");
			player.saveScore();
		} else {
			throw new IllegalArgumentException("Parameter "+mode+" is not valid");
		}
	}

	@Override
	public void updateProperties(Properties p) {
		this.properties = p;
		loadSavedProperties();
		
		victimM.reloadResources();
		player.reloadResources();
		energyM.reloadResources();
		enemy.reloadResources();
		
		if (!musicOn) {
			soundM.stopMusic();
		} else {
			soundM.resumeMusic();
		}
	}
	
	public void loop () {
		long previousTime;
		long currentTime;
		long diff;
		long wait;
		final long FPS = 60;
		final long SEC = 1000;
		
		while (!gameOver && Thread.currentThread()==gameThread) {
			if (paused) {
				continue;
			}
			
			previousTime = System.currentTimeMillis();
			
			update();
			repaint();
			
			currentTime = System.currentTimeMillis();
			diff = currentTime - previousTime;
			wait = SEC/FPS - diff;
			
			try {
				Thread.sleep(wait > 0 ? wait : 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save () {
		player.saveScore();
	}
	
	public void pause () {
		paused = true;
		player.saveScore();
	}
	
	public void resume () {
		paused = false;
		keyH.resetAll();
		startGame();
		super.requestFocus();
	}

	public void stopGameThread () {
		gameThread = null;
		player.saveScore();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public KeyHandler getKeyHandler() {
		return keyH;
	}
	
	public VictimManager getVictimManager() {
		return victimM;
	}
	
	public Enemy getEnemy () {
		return enemy;
	}
	
	public GamePanel getPanel () {
		return this;
	}

	public boolean isNewGraphics() {
		return newGraphics;
	}

	public EnergyManager getEnergyManager() {
		return energyM;
	}

	public boolean isMusicOn() {
		return musicOn;
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	@Override
	public Dimension getPreferredSize () {
		return SCREEN_SIZE;
	}
	
	public GuiManager getGuiManager() {
		return guiM;
	}
	
	public SoundManager getSoundManager() {
		return soundM;
	}
	
	public boolean isPaused () {
		return paused;
	}

}
