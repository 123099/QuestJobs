package com.khazovgames.questjobs.quests.rewards;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.khazovgames.questjobs.utils.ChatUtils;
import com.khazovgames.questjobs.utils.SerializeField;

public class ItemQuestReward extends QuestReward {

	private static final long serialVersionUID = -7853182252620700328L;
	@SerializeField(name="Reward Item")
	private Material rewardMaterial;
	
	public ItemQuestReward(Material rewardMaterial, int amount) {
		super(amount);
		this.rewardMaterial = rewardMaterial;
	}
	
	public ItemStack GetRewardItem() {
		return new ItemStack(rewardMaterial, (int)amount);
	}

	@Override
	public boolean GiveReward(Player player) {
		boolean success = player.getInventory().addItem(GetRewardItem()).isEmpty();
		player.updateInventory();
		return success;
	}
	
	@Override
	public String toString() {
		return amount + " x " + ChatUtils.EnumToString(rewardMaterial);
	}
}
