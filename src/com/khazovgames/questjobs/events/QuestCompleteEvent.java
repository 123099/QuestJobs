package com.khazovgames.questjobs.events;

import com.khazovgames.questjobs.quests.Quest;
import com.khazovgames.questjobs.utils.DoubleDispatchObserver;

public class QuestCompleteEvent extends QuestEvent {

	public QuestCompleteEvent(Quest quest) {
		super(quest);
	}

	@Override
	public void callback(DoubleDispatchObserver observer) {
		observer.handle(this);
	}

}
