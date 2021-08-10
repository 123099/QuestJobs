package com.khazovgames.questjobs.utils;

import java.util.Observable;
import java.util.Observer;

import com.khazovgames.questjobs.events.Event;
import com.khazovgames.questjobs.events.QuestCompleteEvent;
import com.khazovgames.questjobs.events.QuestProgressUpdateEvent;
import com.khazovgames.questjobs.events.QuestStartEvent;

public abstract class DoubleDispatchObserver implements Observer {

	public abstract void handle(QuestStartEvent event);
	public abstract void handle(QuestCompleteEvent event);
	public abstract void handle(QuestProgressUpdateEvent event);
	
	@Override
	public void update(Observable o, Object event) {
		if(event instanceof Event) 
			((Event)event).callback(this);
	}
}
