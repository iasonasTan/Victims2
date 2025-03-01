package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class SoundManager {
	private ClipHandler music;
	
	public void setMusic (String file) {
		music = new ClipHandler(file, true);
	}
	
	public void playFile (String path, boolean loop) {
		new ClipHandler(path, loop);
	}
	
	public static class ClipHandler {
		private Clip clip;
		private AudioInputStream ais;
		
		public ClipHandler (String path, boolean loop) {
			try {
				ais = AudioSystem.getAudioInputStream(getClass().getResource(path));
				clip = AudioSystem.getClip();
				clip.open(ais);
				if (loop)
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.start();
			} catch (Exception e) {
				System.out.println("Can't play sound, i'm sory, maybe "+
						"your pc needs a hug idk");
			}
		}

		public void stop() {
			clip.stop();
		}

		public void resume() {
			clip.start();
		}
	}

	public void stopMusic() {
		music.stop();
	}
	
	public void resumeMusic () {
		music.resume();
	}

}
