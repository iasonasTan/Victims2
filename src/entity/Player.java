package entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import main.DataStorage;
import main.GamePanel;
import main.KeyHandler;

public final class Player extends MovableEntity {
	// status
	private int score = 0;
	private int winCounter = 0;
	private int counter = 0;
	private int collectingFrameIdx = 0;
	private int dashCount = 0;
	
	// data
	private final int BEST_SCORE;
	private Image imageWin;
	private Image[] collectingFrames;
	private AbstractVictim collider;
	private String movementMethod;

	public Player(GamePanel c, String mm) {
		super(c, 8);
		this.movementMethod = mm;
		
		BEST_SCORE = DataStorage.ScoreStorage.loadScoreFromDisk();
		
		setDefaultValues();
		reloadResources();
	}
	
	public void setMovementMethod(String movementMethod) {
		this.movementMethod = movementMethod;
	}

	public void saveScore () {
		if (score > BEST_SCORE) {
			DataStorage.ScoreStorage.writeScoreToDisk(score);
		}
	}
	
	public int getScore () {
		return score;
	}
	
	public void decreaseScore () {
		score-=9;
	}
	
	public void increaseScore () {
		score+=8;
	}

	@Override
	public void update() {
		counter++;
		super.update();
		
		final KeyHandler keyH = context.getKeyHandler();
		
		
		int stepsX = 0;
		int stepsY = 0;

		if (movementMethod.equals("key-based")) {
			stepsX += keyH.left ? -speed : keyH.right ? speed : 0;
			stepsY += keyH.up ? -speed : keyH.down ? speed : 0;
		} else if (movementMethod.equals("mouse-based")) {
			Point mousePossition = context.getMousePosition();
			PointDb dire = MovableEntity.getDirection(getPoint(), mousePossition);
			stepsX = -(int)(speed*dire.x());
			stepsY = -(int)(speed*dire.y());
		} else {
			throw new IllegalArgumentException("unknown movement method "+movementMethod);
		}
		
		move(new Point(stepsX, stepsY));
		
		updateRect();
		
		if (keyH.shift) {
			dash(2, 3);
			keyH.shift = false;
		}
		
		Optional<AbstractVictim> collider_opt = context.getVictimManager().getCollider(this);
		if (collider_opt.isPresent()) {
			collider = collider_opt.get();
			winCounter = 20;
			if (context.getKeyHandler().space && collider != null) {
				collider.kill();
			}
		}
		
		if (counter%17==0) {
			collectingFrameIdx++;
			
			if (collectingFrameIdx >= collectingFrames.length) {
				collectingFrameIdx = 0;
			}
		}

	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		Image imageToDraw = image;
		if (collider!=null && collider instanceof FakeVictim) {
			imageToDraw = imageWin;
		}
		g.drawImage(imageToDraw, x, y, width, height, null);
		
		if (winCounter > 0 && collider != null) {
			g.drawImage(collider.getImage(), x+10, y-75, 60, 60, null);
			g.setColor(Color.YELLOW);
			g.drawString("Press SPACE to collect", x+10, y);
			winCounter--;
		}
		if (winCounter <= 0) {
			collider = null;
		}
		
		context.getGuiManager().displayScore(score+"/"+BEST_SCORE);
		
		if (context.getKeyHandler().space) {
			final int DIFF = width/2;
			g.drawImage(collectingFrames[collectingFrameIdx], x-DIFF, y-DIFF, width+DIFF*2, height+DIFF*2, null);
		}
	}
	
	@Override
	public void dash (int time, int diff) {
		if (dashCount <= 0)
			return;
		
		super.dash(time, diff);
		context.getSoundManager().playFile("/sound/dash_start.wav");
		var ses = Executors.newSingleThreadScheduledExecutor();
		ses.schedule(() -> {
			if (isOnDash())
				context.getSoundManager().playFile("/sound/dash_stop.wav");
		}, time*1000 - 10, TimeUnit.MILLISECONDS);
		ses.shutdown();
		dashCount--;
		
		context.getGuiManager().displayDashCount(dashCount);
	}
	
	@Override
	public void reloadResources() {
		super.reloadResources();
		
		String resource1Name = context.isNewGraphics() ? "player_new.png" : "player.png";
		image = new ImageIcon(getClass().getResource("/graphics/"+resource1Name)).getImage();
		
		String resource2Name = context.isNewGraphics() ? "player_win_new.png" : "player_win.png";
		imageWin = new ImageIcon(getClass().getResource("/graphics/"+resource2Name)).getImage();
		
		int filesSize = 3;
		collectingFrames = new Image[filesSize];
		for (int i=0; i<collectingFrames.length; i++) {
			collectingFrames[i] = new ImageIcon(getClass()
					.getResource("/graphics/collecting"+i+".png")).getImage();
		}
	}

	@Override
	public void collect() {
		dashCount++;
		context.getGuiManager().displayDashCount(dashCount);
	}
	
	@Override
	public void setDefaultValues() {
		width = 100;
		height = width;
		
		x = 200;
		y = 200;
		
		updateRect();
	}

}
