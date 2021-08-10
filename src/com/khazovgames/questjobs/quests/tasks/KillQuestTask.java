package com.khazovgames.questjobs.quests.tasks;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import com.khazovgames.questjobs.utils.ChatUtils;
import com.khazovgames.questjobs.utils.MathExtension;
import com.khazovgames.questjobs.utils.SerializeField;



public class KillQuestTask extends QuestTask {

	private static final long serialVersionUID = -1916392147418754643L;
	@SerializeField(name="Mob to kill")
	private EntityType mobToKill;
	
	public KillQuestTask(Player taskOwner, EntityType mobToKill, int amountRequired) {
		super(taskOwner, amountRequired);
		this.taskOwner = taskOwner;
		this.mobToKill = mobToKill;
	}
	
	@EventHandler
	private void onEntityKilled(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if(entity.getType() == mobToKill && entity.getKiller() == taskOwner) {
			setAmountComplete(MathExtension.Clamp(amountComplete + 1, 0, amountRequired));
			quest.CheckCompletion();
		}
	}

	@Override
	public String toString() {
		return "Kill " + amountRequired + " " + getSimpleName();
	}
	
	@Override
	public String getSimpleName() {
		return ChatUtils.EnumToString(mobToKill);
	}

	@Override
	public void onQuestEnd() {}

	@Override
	public void onQuestStart() {}
}
