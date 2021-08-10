package com.khazovgames.questjobs.quests;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class QuestInventoryManager {
	
	private static QuestInventoryManager instance;
	
	public static QuestInventoryManager GetInstance() {
		if(instance == null)
			instance = new QuestInventoryManager();
		
		return instance;
	}
	
	private HashMap<Player, QuestInventory> questInventories;
	
	private QuestInventoryManager() {
		questInventories = new HashMap<>();
	}
	
	/**
	 * Gets the quest inventory list of the player.
	 * If the player did not have a list beforehand, a new one will be created for him and returned.
	 * @param player
	 * @return
	 */
	public QuestInventory GetInventory(Player player) {
		if(!InventoryExists(player)){
			QuestInventory questInventory = QuestInventoryIO.LoadInventoryFromConfig(player);
			AddInventory(player, questInventory);
		}
		
		return questInventories.get(player);
	}
	
	public boolean AddInventory(Player player, QuestInventory questInventory) {
		if(InventoryExists(player))
			return false;
		
		questInventories.put(player, questInventory);
		return true;
	}
	
	public boolean RemoveInventory(Player player) {
		if(!InventoryExists(player))
			return false;
		
		questInventories.remove(player);
		return true;
	}
	
	public boolean InventoryExists(Player player) {
		return questInventories.containsKey(player);
	}
}
