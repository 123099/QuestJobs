package com.khazovgames.questjobs.commands.questcommands;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.khazovgames.questjobs.commands.PlayerCommandExecutor;
import com.khazovgames.questjobs.quests.Quest;
import com.khazovgames.questjobs.quests.QuestInventory;
import com.khazovgames.questjobs.quests.QuestInventoryManager;
import com.khazovgames.questjobs.quests.rewards.QuestReward;
import com.khazovgames.questjobs.quests.tasks.QuestTask;
import com.khazovgames.questjobs.utils.Constants;

public class PlayerCommandQuestShow extends PlayerCommandExecutor {

	@Override
	public boolean onPlayerCommand(Player player, Command command, String label, String[] args) {
		QuestInventory questInventory = QuestInventoryManager.GetInstance().GetInventory(player);
		List<Quest> quests = null;
		if(args.length >= 2 && args[1].equalsIgnoreCase("complete"))
			quests = questInventory.GetAllQuests();
		else 
			quests = questInventory.GetActiveQuests(true);
		
		Collections.sort(quests);
		
		int nextMultipleOf9 = (int) (9 * Math.ceil(quests.size() / 9f));

		String inventoryTitle = Constants.questLogInventoryName;
		if(nextMultipleOf9 == 0) inventoryTitle += " - No Active Quests";
		Inventory guiQuestInventory = Bukkit.createInventory(null, nextMultipleOf9, inventoryTitle);
		
		for(Quest quest : quests) 
		{
			ItemStack questDesc = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta bookMeta = (BookMeta) questDesc.getItemMeta();
			
			bookMeta.setTitle(Constants.questIdentifier + "" + quest.GetID() + " - " + quest.GetName() + (quest.IsComplete() ? " - Complete" : ""));
			bookMeta.setAuthor(quest.GetGiver());
			
			String tasks = "";
			for(QuestTask task : quest.GetTasks())
				tasks += " - " + task + "\n";
			
			String taskProgresses = "";
			for(QuestTask task : quest.GetTasks())
				taskProgresses += " [" + task.getSimpleName() + "] " + task.getProgressInPercent() + "% (" + task.getAmountComplete() + "/" + task.getAmountRequired() + ")\n";
			
			String rewards = "";
			for(QuestReward reward : quest.GetRewards())
				rewards += " - " + reward + "\n";
			
			bookMeta.setPages(
				"Kind traveller,\n"
				+ "I need you to please help me with a few things!\n"
				+ "Would you be so kind to:\n"
				+ tasks + "\n"
				+ "If you do this for me, I will pay you with:\n"
				+ rewards,
				
				"Progress:\n" 
				+ taskProgresses 
				+ "Repeatable: " + (quest.IsRepeatable() ? "Yes" : "No") + "\n"
				+ "Complete: " + (quest.IsComplete() ? "Yes" : "No") + "\n\n"
				+ "*Note: To see updated progress, you need to get a new book from your log."
			);
			
			questDesc.setItemMeta(bookMeta);
			
			guiQuestInventory.addItem(questDesc);
		}
		
		player.openInventory(guiQuestInventory);
		
		return true;
	}

}
