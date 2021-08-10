package com.khazovgames.questjobs.externals;

import org.bukkit.plugin.Plugin;

import com.khazovgames.questjobs.QuestJobs;

public abstract class ExternalPluginHandler {

	private String pluginName;
	
	protected Plugin plugin;
	
	public ExternalPluginHandler(String pluginName) {
		this.pluginName = pluginName;
	}
	
	public boolean Initialize(QuestJobs questJobs) {
		plugin = questJobs.getServer().getPluginManager().getPlugin(pluginName);
		if(plugin == null)
			return false;
		
		return true;
	}
}
