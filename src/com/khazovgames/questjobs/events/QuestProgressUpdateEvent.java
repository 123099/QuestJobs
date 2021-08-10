package com.khazovgames.questjobs.events;

import com.khazovgames.questjobs.quests.Quest;
import com.khazovgames.questjobs.quests.tasks.QuestTask;
import com.khazovgames.questjobs.utils.DoubleDispatchObserver;

public class QuestProgressUpdateEvent extends QuestEvent {

	private QuestTask task;
	
	public QuestProgressUpdateEvent(Quest quest, QuestTask task) {
		super(quest);
		this.task = task;
	}

	@Override
	public void callback(DoubleDispatchObserver observer) {
		observer.handle(this);
	}
	
	public QuestTask getTask() {
		return task;
	}

}
