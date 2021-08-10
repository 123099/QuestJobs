package com.khazovgames.questjobs.quests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.khazovgames.questjobs.QuestJobs;
import com.khazovgames.questjobs.quests.tasks.QuestTask;
import com.khazovgames.questjobs.utils.YamlUtils;

public class QuestInventoryIO {

	private static final String activeQuestsTag = "Active Quests";
	private static final String completeQuestsTag = "Complete Quests";
	private static final String repeatableQuestsTag = "Repeatable Quests";
	
	public static QuestInventory LoadInventoryFromConfig(Player player) {
		QuestInventory questInventory = new QuestInventory(player);
		FileConfiguration playerConfig = getPlayerConfigFile(player);
		
		HashMap<String, HashMap<String, Double>> activeQuests = YamlUtils.GetDoubleHashMap(playerConfig, activeQuestsTag);
		HashMap<String, HashMap<String, Double>> completeQuests = YamlUtils.GetDoubleHashMap(playerConfig, completeQuestsTag);
		HashMap<String, HashMap<String, Double>> repeatableQuests = YamlUtils.GetDoubleHashMap(playerConfig, repeatableQuestsTag);
		
		if(activeQuests == null || completeQuests == null || repeatableQuests == null) return questInventory;
		
		HashMap<String, HashMap<String, Double>> allQuests = new HashMap<>();
		allQuests.putAll(activeQuests);
		allQuests.putAll(completeQuests);
		allQuests.putAll(repeatableQuests);
		
		for(String questString : allQuests.keySet()) {
			try {
				int questID = Integer.parseInt(questString);
				Quest quest = QuestIO.LoadQuestFromConfig(player, questID); 
					
				HashMap<String, Double> taskProgresses = allQuests.get(questString);
				for(String taskIDString : taskProgresses.keySet())
				{
					int taskID = Integer.parseInt(taskIDString);
					QuestTask task = quest.GetTaskFromID(taskID);
					if(task != null)
						task.setProgress(taskProgresses.get(taskIDString).floatValue());
				}

				if(completeQuests.containsKey(questString) || repeatableQuests.containsKey(questString)) quest.MarkComplete();
				
				questInventory.AddQuest(quest, false);
			}
			catch (NumberFormatException e) {
				System.out.println("[Error] Could not load quest. ID " + questString + " was given in the save file of " + player.getUniqueId());
			}
			catch(IOException ex) {
				System.out.println("[Error] Could not load quest with ID " + questString + ". It was created with a different version of the plugin. Please recreate the quest.");
			}
		}
		
		return questInventory;
	}
	
	public static void SaveInventory(QuestInventory inventory) {
		Player player = inventory.GetOwner();
		FileConfiguration playerConfig = getPlayerConfigFile(player);

		HashMap<Integer, HashMap<Integer, Float>> activeQuests = new HashMap<>();
		HashMap<Integer, HashMap<Integer, Float>> completeQuests = new HashMap<>();
		HashMap<Integer, HashMap<Integer, Float>> repeatableQuests = new HashMap<>();
		
		for(int i = 0; i < inventory.GetSize(); ++i) {
			Quest quest = inventory.GetQuestByIndex(i);
			HashMap<Integer, Float> taskProgresses = new HashMap<>();
			
			for(QuestTask task : quest.GetTasks())
				taskProgresses.put(task.getID(), task.getProgress());
			
			if(quest.IsComplete() && quest.IsRepeatable())
			{
				repeatableQuests.put(quest.GetID(), taskProgresses);
			}
			else if(quest.IsComplete()) 
			{ 
				completeQuests.put(quest.GetID(), taskProgresses);
			}
			else
			{
				activeQuests.put(quest.GetID(), taskProgresses);
			}
		}
		
		playerConfig.set(activeQuestsTag, activeQuests);
		playerConfig.set(completeQuestsTag, completeQuests);
		playerConfig.set(repeatableQuestsTag, repeatableQuests);
		savePlayerConfigFile(playerConfig, player);
	}
	
	private static FileConfiguration getPlayerConfigFile(Player player) {
		QuestJobs questBoard = QuestJobs.getPlugin(QuestJobs.class);
		String configFileName = player.getUniqueId() + ".yml";
		File configFile = questBoard.getDataFolder();
		FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(new File(configFile, configFileName));
		return playerConfig;
	}
	
	private static void savePlayerConfigFile(FileConfiguration config, Player player) {
		QuestJobs questBoard = QuestJobs.getPlugin(QuestJobs.class);
		String configFileName = player.getUniqueId() + ".yml";
		File configFile = questBoard.getDataFolder();
		try {
			config.save(new File(configFile, configFileName));
		} catch (IOException e) {
			questBoard.getLogger().log(Level.WARNING, "Failed to save quest data file for player " + player.getName(), e);
		}
	}
}
