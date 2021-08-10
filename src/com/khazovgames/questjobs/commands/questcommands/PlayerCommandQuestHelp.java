package com.khazovgames.questjobs.commands.questcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.khazovgames.questjobs.commands.PlayerCommandExecutor;
import com.khazovgames.questjobs.utils.ChatUtils;

public class PlayerCommandQuestHelp extends PlayerCommandExecutor {

	@Override
	public boolean onPlayerCommand(Player player, Command command, String label, String[] args) {
		player.sendMessage(ChatUtils.GetTitle("Quest Help"));
		player.sendMessage(ChatUtils.GetCommandHelp("Quest Help", "Displays this help message"));
		player.sendMessage(ChatUtils.GetCommandHelp("Quest Show", "Opens up your quest log"));
		player.sendMessage(ChatUtils.GetCommandHelp("Quest Show Complete", "Opens up your quest log including all complete quests"));
		player.sendMessage(ChatUtils.GetCommandHelp("Quest Abandon [ID/Name]", "Abandons the quest with ID or Name"));
		player.sendMessage(ChatUtils.GetCommandHelp("Quest Clear", "Abandons all quests and clears your quest log"));
		return true;
	}
}
