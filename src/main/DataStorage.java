package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public abstract class DataStorage { // final
	private static String home;
	private static File dataFolder;
	private static final String dataFolderName = "game_data_0000";
	
	static {
		home = System.getProperty("user.home");
		try {
			dataFolder = new File(home, dataFolderName);
			if (!dataFolder.exists()||!dataFolder.isDirectory()) {
				dataFolder.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class ScoreStorage {
		private static final String scoreFileName = "best_score.txt";
		private static File scoreFile;
		
		static {
			try {
				scoreFile = new File(dataFolder, scoreFileName);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		public static int loadScoreFromDisk () {
			try {
				FileInputStream fis = new FileInputStream(scoreFile);
				byte[] scoreStrBytes = fis.readAllBytes();
				fis.close();
				String scoreStr = new String(scoreStrBytes);
				int score = Integer.parseInt(scoreStr.strip());
				return score;
			} catch (FileNotFoundException e) {
				createFile(scoreFile);
			}catch (Exception e) {
				System.out.println(e);
			}
			return 1;
		}
		
		public static void writeScoreToDisk (int scoreToWrite) {
			try {
				FileOutputStream fos = new FileOutputStream(scoreFile);
				fos.write((scoreToWrite+"").getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				createFile(scoreFile);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	};
	
	public static class SettingsStorage {
		private static final String settingsFileName = "settings.properties";
		private static File settingsFile;

		static {
			try {
				settingsFile = new File(dataFolder, settingsFileName);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		public static Properties loadPropertiesFromDisk() {
			try {
				FileInputStream fis = new FileInputStream(settingsFile);
				Properties inProperties = new Properties();
				inProperties.load(fis);
				fis.close();
				if (inProperties.isEmpty())
					return getDefaultProperties();
				return inProperties;
			} catch (FileNotFoundException e) {
				createFile(settingsFile);
			} catch (Exception e) {
				System.out.println(e);
			}
			return getDefaultProperties();
		}

		public static boolean writePropertiesToDisk(Properties outProperties) {
			try {
				FileOutputStream fos = new FileOutputStream(settingsFile);
				Date d = new Date();
				outProperties.store(fos, "Settings updated on " + d.getTime());
				fos.close();
				return true;
			} catch (FileNotFoundException e) {
				createFile(settingsFile);
				writePropertiesToDisk(outProperties);
				return true;
			} catch (Exception e) {
				System.out.println(e);
				return false;
			}
		}

		public static Properties getDefaultProperties() {
			Properties defaultP = new Properties();
			defaultP.setProperty("newGraphics", "false");
			defaultP.setProperty("music", "true");
			defaultP.setProperty("movementMethod", "key-based");

			return defaultP;
		}

	}
	
	private static void createFile(File f) {
		try {
			f.createNewFile();
			System.out.println("Created file at "+f.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Could not create file! " + e);
		}
	}

}
