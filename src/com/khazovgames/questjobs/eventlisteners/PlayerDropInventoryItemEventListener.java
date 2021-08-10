package com.khazovgames.questjobs.eventlisteners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.khazovgames.questjobs.quests.Quest;
import com.khazovgames.questjobs.quests.QuestInventory;
import com.khazovgames.questjobs.quests.QuestInventoryManager;
import com.khazovgames.questjobs.utils.Constants;
import com.khazovgames.questjobs.utils.QuestUtils;

import net.md_5.bungee.api.ChatColor;

public class PlayerDropInventoryItemEventListener implements Listener{

	private ItemStack droppedItem;
	private Inventory droppedFromInventory;
	
	@EventHandler
	public void onPlayerDropInventoryItemEvent(InventoryClickEvent event) {
		if((event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.DROP_ALL_CURSOR) && event.getCursor().getType() != Material.AIR) 
		{
			droppedItem = event.getCursor();
			droppedFromInventory = event.getInventory();
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().equals(droppedItem)) 
		{
			if(droppedFromInventory.getName().contains(Constants.questLogInventoryName))
			{
				if(droppedItem.getItemMeta() instanceof BookMeta)
				{
					BookMeta bookMeta = (BookMeta) droppedItem.getItemMeta();
					int questID = QuestUtils.getQuestIDFromString(bookMeta.getTitle());
					if(questID != -1) {
						Player player = event.getPlayer();
						QuestInventory questInventory = QuestInventoryManager.GetInstance().GetInventory(player); 
						Quest quest = questInventory.GetQuestByID(questID);
						boolean success = questInventory.RemoveQuest(quest);
						if(success)
							player.sendMessage(ChatColor.GREEN + "Successfully abandoned the quest " + quest.GetName());
						else
							player.sendMessage(ChatColor.YELLOW + "You did not have this quest in progress. How did you drop it?");
					}
				}
			}
		}
	}
}
