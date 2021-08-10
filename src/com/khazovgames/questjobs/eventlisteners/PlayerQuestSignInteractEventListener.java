package com.khazovgames.questjobs.eventlisteners;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.khazovgames.questjobs.packets.EnumChatAction;
import com.khazovgames.questjobs.packets.PacketChatData;
import com.khazovgames.questjobs.packets.PacketCreator;
import com.khazovgames.questjobs.packets.PacketSender;
import com.khazovgames.questjobs.quests.Quest;
import com.khazovgames.questjobs.quests.QuestIO;
import com.khazovgames.questjobs.quests.QuestInventory;
import com.khazovgames.questjobs.quests.QuestInventoryManager;
import com.khazovgames.questjobs.utils.QuestUtils;

import net.md_5.bungee.api.ChatColor;


public class PlayerQuestSignInteractEventListener implements Listener {
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
				Sign sign = (Sign)block.getState();
				int questID = QuestUtils.getQuestIDFromString(sign.getLine(0));
				if(questID == -1)
					return;
				
				QuestInventory questInventory = QuestInventoryManager.GetInstance().GetInventory(player);
				
				Quest quest = questInventory.GetQuestByID(questID);
				
				//If player does not have this quest in progress, try loading the info from config.
				if(quest == null)
					try { quest = QuestIO.LoadQuestFromConfig(player, questID);	}
					catch (IOException e) { }
				
				PacketChatData chatData = null;
				
				//If the quest failed to load from config as well, the quest doesn't exist, do nothing.
				if(quest == null) {
					chatData = new PacketChatData(ChatColor.GOLD + "A quest with ID " + questID + " was not created or could not be loaded.", EnumChatAction.ActionBar);
					PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
					return;
				}
				
				boolean addedSuccessfully = questInventory.AddQuest(quest, true);
				if(addedSuccessfully) 
				{
					chatData = new PacketChatData(ChatColor.YELLOW + "You can find the quest details in your quest log (/quest).", EnumChatAction.ActionBar);
				}
				else if(!questInventory.Contains(quest) && quest.DependsOnQuest())
				{
					chatData = new PacketChatData(ChatColor.GOLD + "You must complete the quest ID " + quest.GetDependencyQuestID() + " before accepting this one.", EnumChatAction.ActionBar);
				}
				else if(quest.IsComplete() && !quest.IsRepeatable())
				{
					chatData = new PacketChatData(ChatColor.GOLD + "Quest already complete.", EnumChatAction.ActionBar);
				}
				else 
				{
					chatData = new PacketChatData(ChatColor.GOLD + "Quest in progress. See quest log (/quest).", EnumChatAction.ActionBar);
				}
				
				PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
			}
		}
	}
}
