package com.khazovgames.questjobs.quests.rewards;

import java.io.Serializable;

import org.bukkit.entity.Player;

import com.khazovgames.questjobs.utils.SerializeField;

public abstract class QuestReward implements Serializable{

	private static final long serialVersionUID = 6980661108328934889L;
	
	@SerializeField(name="Amount")
	protected int amount;
	
	public QuestReward(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public abstract boolean GiveReward(Player player);
	
	@Override
	public String toString() {
		return "[" + getClass().getName() + "::" + amount + "]";
	}
}
