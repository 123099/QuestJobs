package com.khazovgames.questjobs.quests.tasks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.khazovgames.questjobs.utils.ChatUtils;
import com.khazovgames.questjobs.utils.InventoryUtils;
import com.khazovgames.questjobs.utils.MathExtension;
import com.khazovgames.questjobs.utils.SerializeField;


public class CollectQuestTask extends QuestTask {

	private static final long serialVersionUID = 2598119239905763868L;
	
	@SerializeField(name="Item to collect")
	private Material itemToCollect;
	
	public CollectQuestTask(Player taskOwner, Material itemToCollect, int amountRequired) {
		super(taskOwner, amountRequired);
		this.itemToCollect = itemToCollect;
	}
	
	private void checkAlreadyContains() {
		taskOwner.updateInventory();
		if(taskOwner.getInventory().contains(itemToCollect, amountRequired))
		{
			setAmountComplete(amountRequired);
			quest.CheckCompletion();
		}
	}

	@EventHandler
	private void onItemCollected(PlayerPickupItemEvent event) {
		ItemStack collectedItem = event.getItem().getItemStack();
		
		if(event.getPlayer() == taskOwner && collectedItem.getType() == itemToCollect) {
			setAmountComplete(MathExtension.Clamp(amountComplete + collectedItem.getAmount(), 0, amountRequired));
			quest.CheckCompletion();
		}
	} 
	
	@EventHandler
	private void onItemThrown(PlayerDropItemEvent event) {
		ItemStack thrownItem = event.getItemDrop().getItemStack();
		
		if(event.getPlayer() == taskOwner && thrownItem.getType() == itemToCollect) 
			setAmountComplete(MathExtension.Clamp(amountComplete - thrownItem.getAmount(), 0, amountRequired));
	}
	
	@Override
	public String toString() {
		return "Collect " + amountRequired + " " + getSimpleName();
	}
	
	@Override
	public String getSimpleName() {
		return ChatUtils.EnumToString(itemToCollect);
	}

	@Override
	public void onQuestEnd() {
		ItemStack collectedItems = new ItemStack(itemToCollect, amountRequired);
		taskOwner.getInventory().removeItem(collectedItems);
		taskOwner.updateInventory();
	}

	@Override
	public void onQuestStart() {
		setAmountComplete(MathExtension.Clamp(InventoryUtils.GetItemCount(taskOwner.getInventory(), itemToCollect), 0, amountRequired));
		checkAlreadyContains();
	}
}
