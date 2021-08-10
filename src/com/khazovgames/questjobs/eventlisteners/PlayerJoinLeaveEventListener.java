package com.khazovgames.questjobs.eventlisteners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.khazovgames.questjobs.quests.QuestInventoryIO;
import com.khazovgames.questjobs.quests.QuestInventoryManager;
import com.khazovgames.questjobs.utils.PlayerExtension;

public class PlayerJoinLeaveEventListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!QuestInventoryManager.GetInstance().InventoryExists(event.getPlayer())) 
			QuestInventoryManager.GetInstance().AddInventory(event.getPlayer(), QuestInventoryIO.LoadInventoryFromConfig(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if(QuestInventoryManager.GetInstance().InventoryExists(event.getPlayer())) {
			QuestInventoryIO.SaveInventory(QuestInventoryManager.GetInstance().GetInventory(event.getPlayer()));
			QuestInventoryManager.GetInstance().RemoveInventory(event.getPlayer());
		}
		
		PlayerExtension.RemoveAllAttributes(event.getPlayer());
	}
}
