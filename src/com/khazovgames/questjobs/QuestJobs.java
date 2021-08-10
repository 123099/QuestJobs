package com.khazovgames.questjobs;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.khazovgames.questjobs.commands.CommandNames;
import com.khazovgames.questjobs.commands.questcommands.PlayerCommandQuest;
import com.khazovgames.questjobs.config.QuestJobsConfig;
import com.khazovgames.questjobs.eventlisteners.PlayerDropInventoryItemEventListener;
import com.khazovgames.questjobs.eventlisteners.PlayerJoinLeaveEventListener;
import com.khazovgames.questjobs.eventlisteners.PlayerQuestSignInteractEventListener;
import com.khazovgames.questjobs.eventlisteners.SignEventListener;
import com.khazovgames.questjobs.externals.CitizensHandler;
import com.khazovgames.questjobs.externals.VaultHandler;
import com.khazovgames.questjobs.quests.QuestInventoryIO;
import com.khazovgames.questjobs.quests.QuestInventoryManager;

public class QuestJobs extends JavaPlugin {

	@Override
	public void onEnable() {
		QuestJobsConfig.GetInstance().SetDefaultConfig();
		
		registerCommands();
		registerEvents();
		
		initVault();
		initCitizens();
		
		loadQuestInventories();
	}
	
	@Override
	public void onDisable() {
		saveQuestInventories();
	}
	
	private void registerCommands()
	{
		getCommand(CommandNames.Quest).setExecutor(new PlayerCommandQuest());
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new SignEventListener(), this);
		pm.registerEvents(new PlayerQuestSignInteractEventListener(), this);
		pm.registerEvents(new PlayerJoinLeaveEventListener(), this);
		pm.registerEvents(new PlayerDropInventoryItemEventListener(), this);
	}
	
	private void loadQuestInventories() {
		for(Player player : getServer().getOnlinePlayers())
			QuestInventoryManager.GetInstance().AddInventory(player, QuestInventoryIO.LoadInventoryFromConfig(player));
	}
	
	private void saveQuestInventories() {
		for(Player player : getServer().getOnlinePlayers())
			if(QuestInventoryManager.GetInstance().InventoryExists(player)) {
				QuestInventoryIO.SaveInventory(QuestInventoryManager.GetInstance().GetInventory(player));
				QuestInventoryManager.GetInstance().RemoveInventory(player);
			}
	}
	
	private void initVault() {
		boolean success = VaultHandler.GetInstance().Initialize(this);
		if(!success)
			getLogger().log(Level.WARNING, "Vault or an Economy plugin not installed. Economy quest rewards will not work.");
	}
	
	private void initCitizens() {
		boolean success = CitizensHandler.GetInstance().Initialize(this);
		if(!success)
			getLogger().log(Level.WARNING, "Citizens plugin was not found. Some quest tasks will not work.");
	}
}
