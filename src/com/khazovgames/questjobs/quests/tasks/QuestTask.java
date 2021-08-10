package com.khazovgames.questjobs.quests.tasks;

import java.io.Serializable;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.khazovgames.questjobs.QuestJobs;
import com.khazovgames.questjobs.quests.Quest;
import com.khazovgames.questjobs.utils.SerializeField;

public abstract class QuestTask implements Listener, Serializable{

	private static final long serialVersionUID = -7920729851625104601L;
	
	private static int createdTasks;
	private int ID;

	protected transient QuestJobs questJobs;
	
	protected transient Player taskOwner;
	
	@SerializeField(name="Amount")
	protected int amountRequired;
	protected int amountComplete;
	
	protected Quest quest;
	
	public QuestTask(Player taskOwner, int amountRequired) {
		this.taskOwner = taskOwner;
		this.amountRequired = amountRequired;
		
		ID = createdTasks;
		++createdTasks;
	}
	
	public abstract void onQuestStart();
	public abstract void onQuestEnd();
	public abstract String getSimpleName();
	
	public float getProgress() {
		return (float)amountComplete / amountRequired;
	}
	
	public int getProgressInPercent() {
		float progress = getProgress();
		return Math.round(progress * 100);
	}
	
	public int getAmountRequired() {
		return amountRequired;
	}
	
	public int getAmountComplete() {
		return amountComplete;
	}
	
	public boolean isComplete() {
		return amountRequired == amountComplete;
	}
	
	public void setOwner(Player player) {
		this.taskOwner = player;
	}
	
	public void setQuestBelongingTo(Quest quest) {
		this.quest = quest;
	}
	
	public void setAmountComplete(int amountComplete) {		
		this.amountComplete = amountComplete;
		quest.MarkTaskUpdated(this);
	}
	
	public void setProgress(float progress) {
		amountComplete = Math.round(progress * amountRequired);
	}
	
	public void start() {
		registerAsListener();
	}
	
	public void pause() {
		deregisterAsListener();
	}
	
	public void restart() {
		if(isComplete() && quest.IsRepeatable())
			amountComplete = 0;
		pause();
		start();
	}
	
	private void registerAsListener() {
		try {
			questJobs = QuestJobs.getPlugin(QuestJobs.class);
			questJobs.getServer().getPluginManager().registerEvents(this, questJobs);
		}
		catch(Exception e) {}
	}
	
	protected void deregisterAsListener() {
		HandlerList.unregisterAll(this);
	}
	
	public int getID() {
		return ID;
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getName() + "::" + amountRequired + "]";
	}
}
