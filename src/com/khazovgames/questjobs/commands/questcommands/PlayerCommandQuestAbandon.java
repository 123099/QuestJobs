package com.khazovgames.questjobs.commands.questcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.khazovgames.questjobs.commands.PlayerCommandExecutor;
import com.khazovgames.questjobs.config.QuestJobsConfig;
import com.khazovgames.questjobs.packets.EnumChatAction;
import com.khazovgames.questjobs.packets.PacketChatData;
import com.khazovgames.questjobs.packets.PacketCreator;
import com.khazovgames.questjobs.packets.PacketSender;
import com.khazovgames.questjobs.quests.Quest;
import com.khazovgames.questjobs.quests.QuestInventory;
import com.khazovgames.questjobs.quests.QuestInventoryManager;

import net.md_5.bungee.api.ChatColor;

public class PlayerCommandQuestAbandon extends PlayerCommandExecutor {

	@Override
	public boolean onPlayerCommand(Player player, Command command, String label, String[] args) {
		PacketChatData chatData = null;
		
		if(args.length < 2) {
			chatData = new PacketChatData(ChatColor.YELLOW + "You need to provide the quest ID or name to abandon.", EnumChatAction.ActionBar);
			PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
			return false;
		}
		
		if(!QuestInventoryManager.GetInstance().InventoryExists(player)) {
			player.sendMessage(ChatColor.RED + "You do not have any quests in progress.");
			PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
			return false;
		}
		
		QuestInventory questInventory = QuestInventoryManager.GetInstance().GetInventory(player);
		
		if(questInventory.IsEmpty()) {
			chatData = new PacketChatData(ChatColor.RED + "You do not have any quests in progress.", EnumChatAction.ActionBar);
			PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
			return false;
		}
		
		Quest quest;
		String questNameOrID = combineArgs(args, 1);
		try {
			quest = questInventory.GetQuestByID(Integer.parseInt(questNameOrID));
		} catch(Exception e) {
			quest = questInventory.GetQuestByName(questNameOrID);
		}
		
		if(quest == null) {
			chatData = new PacketChatData(ChatColor.RED + "You do not have any quests in progress.", EnumChatAction.ActionBar);
			PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
			return false;
		}
		
		if(quest.IsComplete() && !quest.IsRepeatable())
			if(!QuestJobsConfig.GetInstance().GetConfig().getBoolean(QuestJobsConfig.ClearComplete))
			{
				chatData = new PacketChatData(ChatColor.YELLOW + "You may not abandon quests that you have already completed!", EnumChatAction.ActionBar);
				PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
				return false;
			}
		
		questInventory.RemoveQuest(quest);
		
		chatData = new PacketChatData(ChatColor.GREEN + "You have successfully abandoned quest named " + quest.GetName() + "!", EnumChatAction.ActionBar);
		PacketSender.SendPacket(player, PacketCreator.createChatPacket(chatData));
		
		return true;
	}

	private String combineArgs(String[] args, int startIndex) {
		String combinedString = "";
		for(int i = startIndex; i < args.length; ++i)
			combinedString += args[i] + " ";
		
		return combinedString.substring(0, combinedString.length()-1);
	}
}
