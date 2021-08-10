package com.khazovgames.questjobs.quests.tasks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import com.khazovgames.questjobs.utils.ChatUtils;
import com.khazovgames.questjobs.utils.MathExtension;
import com.khazovgames.questjobs.utils.SerializeField;

public class MineQuestTask extends QuestTask {

	private static final long serialVersionUID = 6776848755783075629L;
	
	@SerializeField(name="Material to mine")
	private Material materialToMine;
	
	public MineQuestTask(Player taskOwner, Material materialToMine, int amountRequired) {
		super(taskOwner, amountRequired);
		this.materialToMine = materialToMine;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getPlayer() == taskOwner && event.getBlock().getType() == materialToMine)
		{
			setAmountComplete(MathExtension.Clamp(amountComplete + 1, 0, amountRequired));
			quest.CheckCompletion();
		}
	}

	@Override
	public void onQuestStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onQuestEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "Mine " + amountRequired + " " + getSimpleName();
	}

	@Override
	public String getSimpleName() {
		return ChatUtils.EnumToString(materialToMine);
	}
}
