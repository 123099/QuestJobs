package com.khazovgames.questjobs.config;

import org.bukkit.configuration.file.FileConfiguration;

import com.khazovgames.questjobs.QuestJobs;

public class QuestJobsConfig {

	public static final String ClearComplete = "clear complete";
	
	private static QuestJobsConfig instance;
	public static QuestJobsConfig GetInstance() {
		if(instance == null)
			instance = new QuestJobsConfig();
		return instance;
	}
	
	private QuestJobs questJobs;
	private FileConfiguration config;
	
	private QuestJobsConfig() {
		questJobs = QuestJobs.getPlugin(QuestJobs.class);
		config = questJobs.getConfig();
	}
	
	public FileConfiguration GetConfig() {
		return config;
	}
	
	public void SetDefaultConfig() {
		config.addDefault(ClearComplete, true);
		config.options().copyDefaults(true);
		questJobs.saveConfig();
	}
}
