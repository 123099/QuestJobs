package com.khazovgames.questjobs.quests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Observable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.khazovgames.questjobs.QuestJobs;
import com.khazovgames.questjobs.events.QuestCompleteEvent;
import com.khazovgames.questjobs.events.QuestProgressUpdateEvent;
import com.khazovgames.questjobs.events.QuestStartEvent;
import com.khazovgames.questjobs.packets.EnumChatAction;
import com.khazovgames.questjobs.packets.PacketChatData;
import com.khazovgames.questjobs.packets.PacketCreator;
import com.khazovgames.questjobs.packets.PacketSender;
import com.khazovgames.questjobs.quests.rewards.QuestReward;
import com.khazovgames.questjobs.quests.tasks.QuestTask;
import com.khazovgames.questjobs.utils.SerializeField;

import net.minecraft.server.v1_10_R1.ChatModifier;
import net.minecraft.server.v1_10_R1.EnumChatFormat;

public class Quest extends Observable implements Serializable, Comparable<Quest>{
	
	private static final long serialVersionUID = -8912916762037435439L;
	
	@SerializeField(name="Quest ID")
	private int ID;
	@SerializeField(name="Quest Name")
	private String questName;
	@SerializeField(name="Name of Quest Giver")
	private String questGiver;
	
	@SerializeField(name="Repeatable")
	private boolean repeatable;
	@SerializeField(name="Dependency (Use -1 for no dependency)")
	private int dependencyQuestID;
	
	@SerializeField(name="Tasks")
	private QuestTask[] tasks;
	@SerializeField(name="Rewards")
	private QuestReward[] rewards;
	
	private transient Player owner;
	
	private boolean complete;
	
	public Quest(Player questOwner, int ID, String questName, String questGiver, boolean repeatable, int dependencyQuestID, QuestTask[] tasks, QuestReward[] rewards) {
		this.ID = ID;
		this.questName = questName != null ? questName : "Quest #" + ID;
		this.questGiver = questGiver != null ? questGiver : "Jack Solomon";
		this.repeatable = repeatable;
		this.dependencyQuestID = dependencyQuestID;
		
		this.tasks = tasks;
		this.rewards = rewards;
		
		//This is set when creating the quest using the GUI
		//The GUI calls this constructor, sets the tasks' quest field to equal this.
		//After this is all initialized, this object gets serialized.
		for(QuestTask task : tasks)
			task.setQuestBelongingTo(this);
	}
	
	public Player GetOwner() {
		return owner;
	}
	
	public int GetID() {
		return ID;
	}
	
	public String GetName() {
		return questName;
	}
	
	public String GetGiver() {
		return questGiver;
	}
	
	public QuestTask[] GetTasks() {
		return tasks;
	}
	
	public QuestTask GetTaskFromID(int taskID) {
		for(QuestTask task : tasks)
			if(task.getID() == taskID)
				return task;
		
		return null;
	}
	
	public QuestReward[] GetRewards() {
		return rewards;
	}
	
	public int GetDependencyQuestID() {
		return dependencyQuestID;
	}
	
	public void MarkComplete() {
		complete = true;
	}
	
	public void StartQuest(boolean displayMessage) {
		for(QuestTask task : tasks) 
		{
			task.restart();
			task.onQuestStart();
		}
		
		if(displayMessage)
		{
			setChanged();
			notifyObservers(new QuestStartEvent(this));
		}
	}
	
	public void EndQuest() {
		setChanged();
		notifyObservers(new QuestCompleteEvent(this));
		
		MarkComplete();
		
		for(QuestReward reward : rewards)
			reward.GiveReward(owner);

		for(QuestTask task : tasks)
		{
			task.pause();
			task.onQuestEnd();
		}
		
		if(IsRepeatable()) 
			StartQuest(false);
	}
	
	public void Destroy() {
		for(int i = 0; i < tasks.length; ++i)
		{
			QuestTask task = tasks[i];
			task.pause();
			tasks[i] = null;
		}
		
		for(int i = 0; i < rewards.length; ++i)
			rewards[i] = null;
	}
	
	public void MarkTaskUpdated(QuestTask task) {
		setChanged();
		notifyObservers(new QuestProgressUpdateEvent(this, task));
	}
	
	public void CheckCompletion() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(QuestJobs.getPlugin(QuestJobs.class), new Runnable() {

			@Override
			public void run() {
				boolean complete = true;
				for(QuestTask task : tasks)
					if(!task.isComplete()) {
						complete = false;
						break;
					}
				
				if(complete)
					EndQuest();
			}
			
		}, 1);
		
	}
	
	public boolean IsComplete() {
		return complete;
	}
	
	public boolean IsRepeatable() {
		return repeatable;
	}
	
	public boolean DependsOnQuest() {
		return dependencyQuestID != -1;
	}
	
	public void SetOwner(Player owner) {
		this.owner = owner;
		for(QuestTask task : tasks)
			task.setOwner(owner);
	}

	@Override
	public int compareTo(Quest other) {
		return ID - other.ID;
	}
	
	private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		stream.defaultReadObject();
		addObserver(new QuestGUI());
	}
}
