package com.khazovgames.questjobs.commands.questcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.khazovgames.questjobs.commands.PlayerCommandExecutor;
import com.khazovgames.questjobs.config.QuestJobsConfig;
import com.khazovgames.questjobs.packets.EnumChatAction;
import com.khazovgames.questjobs.packets.PacketChatData;
import com.khazovgames.questjobs.packets.PacketCreator;
import com.khazovgames.questjobs.packets.PacketSender;
import com.khazovgames.questjobs.quests.QuestInventory;
import com.khazovgames.questjobs.quests.QuestInventoryManager;

import net.md_5.bungee.api.ChatColor;

public class PlayerCommandQuestClear extends PlayerCommandExecutor {

	@Override
	public boolean onPlayerCommand(Player player, Command command, String label, String[] args) {
		if(QuestInventoryManager.GetInstance().InventoryExists(player)) 
		{
			QuestInventory questInventory = QuestInventoryManager.GetInstance().GetInventory(player);
			if(questInventory != null)
			{
				PacketChatData chatData = null;
				
				if(QuestJobsConfig.GetInstance().GetConfig().getBoolean(QuestJobsConfig.ClearComplete))
				{
					chatData = new PacketChatData(ChatColor.GREEN + "Quest log cleared successfully!", EnumChatAction.ActionBar);
					questInventory.ClearAll();
				}
				else
				{
					chatData = new PacketChatData(ChatColor.GREEN + "Active quest log cleared successfully!", EnumChatAction.ActionBar);
					questInventory.ClearActive();
				}
				
				PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
				return true;
			}
		}
		
		return false;
	}

}
