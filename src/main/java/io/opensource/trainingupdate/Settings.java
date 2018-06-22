package io.opensource.trainingupdate;

import java.io.FileInputStream;
import java.util.Properties;

public class Settings {
	public static final String CLASS_SESSIONS = "training.api.class-sessions";
	
	public enum Setting {
		CLASS_SESSIONS("training.api.class-sessions"),
		JENKINS_EMAIL_TO("jenkinsemail.to"),
		JENKINS_EMAIL_FROM("jenkinsemail.from"),
		JENKINS_EMAIL_RECIPIENT_NAME("jenkinsemail.recipientname"),
		JENKINS_EMAIL_SENDER_NAME("jenkinsemail.sendername");
		
		private String key;
		
		Setting(String key) {
			this.key = key;
		}
	};

	private static Properties settings;
	
	private static void loadSettings() {
		if (settings == null) {
			try {
				settings = new Properties();
				settings.load(new FileInputStream("app.properties"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getSetting(final Setting setting) {
		return getSetting(setting, null);
	}
	
	public static String getSetting(final Setting setting, final String defaultValue) {
		loadSettings();
		return settings.getProperty(setting.key, defaultValue);
	}

}
