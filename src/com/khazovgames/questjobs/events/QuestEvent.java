package com.khazovgames.questjobs.events;

import com.khazovgames.questjobs.quests.Quest;

public abstract class QuestEvent extends Event{

	private Quest quest;
	
	public QuestEvent(Quest quest) {
		this.quest = quest;
	}
	
	public Quest getQuest() {
		return quest;
	}
}
