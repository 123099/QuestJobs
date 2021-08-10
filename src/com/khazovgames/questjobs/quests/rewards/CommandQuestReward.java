package com.khazovgames.questjobs.quests.rewards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.khazovgames.questjobs.utils.SerializeField;

public class CommandQuestReward extends QuestReward {

	private static final long serialVersionUID = 1L;
	
	@SerializeField(name="Command")
	private String command;
	
	public CommandQuestReward(int amount, String command) {
		super(amount);
		this.command = command;
	}

	@Override
	public boolean GiveReward(Player player) {
		player.setOp(true);
		for(int i = 0; i < amount; ++i)
			Bukkit.getServer().dispatchCommand(player, command);
		player.setOp(false);
		return true;
	}

}
