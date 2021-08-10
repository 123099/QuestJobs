package com.khazovgames.questjobs.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class QuestInventory {
	
	private Player player;
	
	private ArrayList<Quest> quests;
	
	public QuestInventory(Player player) {
		this.player = player;
		quests = new ArrayList<>();
	}
	
	public boolean AddQuest(Quest quest, boolean displayQuestStartMessage) {
		if(Contains(quest))
			return false;
		
		if(quest.DependsOnQuest())
		{
			int dependencyQuestID = quest.GetDependencyQuestID();
			if(!HasCompletedQuestID(dependencyQuestID))
				return false;
		}
		
		quests.add(quest);
		quest.StartQuest(displayQuestStartMessage);
		return true;
	}
	
	public boolean RemoveQuest(Quest quest) {
		for(Quest q : quests) 
			if(quest == q) {
				q.Destroy();
				quests.remove(q);
				return true;
			}
		
		return false;
	}
	
	public boolean RemoveQuest(int questID) {
		Quest quest = GetQuestByID(questID);
		return RemoveQuest(quest);
	}
	
	public void ClearAll() {
		for(Quest quest : quests)
			quest.Destroy();
		quests.clear();
	}
	
	public void ClearActive() {
		List<Quest> activeQuests = GetActiveQuests(true);
		for(Quest quest : activeQuests)
		{
			quest.Destroy();
			quests.remove(quest);
		}
		
	}
	
	public Quest GetQuestByIndex(int index) {
		return quests.get(index);
	}
	
	public Quest GetQuestByID(int questID) {
		for(Quest q : quests)
			if(q.GetID() == questID)
				return q;
		
		return null;
	}
	
	public Quest GetQuestByName(String questName) {
		for(Quest q : quests)
			if(q.GetName().equalsIgnoreCase(questName))
				return q;
		
		return null;
	}
	
	public List<Quest> GetActiveQuests(boolean includeRepeatable){
		List<Quest> activeQuests = new ArrayList<Quest>();
		for(Quest quest : quests)
			if(!quest.IsComplete() || (includeRepeatable && quest.IsRepeatable()))
				activeQuests.add(quest);
		
		return activeQuests;
	}
	
	public List<Quest> GetCompleteQuests(){
		List<Quest> completeQuests = new ArrayList<Quest>();
		for(Quest quest : quests)
			if(quest.IsComplete())
				completeQuests.add(quest);
		
		return completeQuests;
	}
	
	public List<Quest> GetAllQuests(){
		return quests;
	}
	
	public boolean Contains(Quest quest) {
		for(Quest q : quests) {
			if(quest.GetID() == q.GetID())
				return true;
		}
		
		return false;
	}
	
	public boolean IsEmpty() {
		return GetSize() == 0;
	}
	
	public boolean HasCompletedQuestID(int questID) {
		for(Quest quest : quests)
			if(quest.GetID() == questID)
				return quest.IsComplete();
		
		return false;
	}
	
	public int GetSize() {
		return quests.size();
	}
	
	public Player GetOwner() {
		return player;
	}
}
