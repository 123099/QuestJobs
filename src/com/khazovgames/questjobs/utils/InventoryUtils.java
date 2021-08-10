package com.khazovgames.questjobs.utils;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryUtils {

	public static int GetItemCount(Inventory inventory, Material material) {
		HashMap<Integer, ? extends ItemStack> items = inventory.all(material);
		
		int totalCount = 0;
		for(int slot : items.keySet())
			totalCount += items.get(slot).getAmount();
		
		return totalCount;
	}
}
