package com.khazovgames.questjobs.events;

import com.khazovgames.questjobs.utils.DoubleDispatchObserver;

public abstract class Event {

	public abstract void callback(DoubleDispatchObserver observer);
}
