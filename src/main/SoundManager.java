package main;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public final class SoundManager {
	private ClipHandler music;
	
	public void setMusic (String file) {
		music = new ClipHandler(file, true);
	}
	
	public void playFile (String path) {
		new ClipHandler(path, false).start();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void start () {
			clip.start();
			clip.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP) {
					close();
				}
			});
		}
		public void close () {
			clip.close();
			try {
				ais.close();
			} catch (IOException e) {
				// ignore
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
