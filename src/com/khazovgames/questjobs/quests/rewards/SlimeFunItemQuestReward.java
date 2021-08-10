package com.khazovgames.questjobs.quests.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.khazovgames.questjobs.utils.SerializeField;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class SlimeFunItemQuestReward extends QuestReward {

	private static final long serialVersionUID = -6446425245006592577L;
	
	@SerializeField(name="Slime Fun Item Name")
	private String itemName;
	
	public SlimeFunItemQuestReward(int amount, String itemName) {
		super(amount);
		this.itemName = itemName;
	}

	@Override
	public boolean GiveReward(Player player) {
		ItemStack item = GetSlimeItem();
		if(item != null)
		{
			item.setAmount((int)amount);
			player.getInventory().addItem(GetSlimeItem());
			return true;
		}
		return false;
	}
	
	public ItemStack GetSlimeItem() {
		if(Slimefun.listIDs().contains(itemName.toUpperCase()))
		{
			SlimefunItem slimeItem = SlimefunItem.getByName(itemName.toUpperCase());
			return slimeItem.getItem();
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		ItemStack item = GetSlimeItem();
		return amount + " x " + (item == null ? "Unknown Item" : itemName);
	}

}
