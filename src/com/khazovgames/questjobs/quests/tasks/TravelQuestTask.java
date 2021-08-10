package com.khazovgames.questjobs.quests.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.khazovgames.questjobs.utils.SerializeField;

public class TravelQuestTask extends QuestTask {
	
	private static final long serialVersionUID = 4759663633645090189L;
	
	@SerializeField(name="Name of the world to visit")
	private String worldName;

	public TravelQuestTask(Player taskOwner, int amountRequired, String worldName) {
		super(taskOwner, 1);
		this.worldName = worldName;
	}

	@Override
	public void onQuestStart() {
		checkCompletion();
	}

	@Override
	public void onQuestEnd() {

	}

	@Override
	public String getSimpleName() {
		return worldName;
	}

	@Override
	public String toString() {
		return "Visit " + worldName;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if(event.getPlayer() == taskOwner)
		{
			checkCompletion();
		}
	}
	
	private void checkCompletion() {
		String currentWorld = taskOwner.getWorld().getName();
		if(currentWorld.equalsIgnoreCase(worldName))
		{
			setAmountComplete(amountRequired);
			quest.CheckCompletion();
		}
	}
}
