package com.khazovgames.questjobs.quests.rewards;

import org.bukkit.entity.Player;

public class ExperienceQuestReward extends QuestReward{

	private static final long serialVersionUID = -6484792798453425747L;

	public ExperienceQuestReward(int amount) {
		super(amount);
	}

	@Override
	public boolean GiveReward(Player player) {
		player.giveExp((int)amount);
		return true;
	}

	@Override
	public String toString() {
		return amount + " Exp";
	}
}
