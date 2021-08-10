package com.khazovgames.questjobs.quests;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.khazovgames.questjobs.QuestJobs;
import com.khazovgames.questjobs.events.QuestCompleteEvent;
import com.khazovgames.questjobs.events.QuestProgressUpdateEvent;
import com.khazovgames.questjobs.events.QuestStartEvent;
import com.khazovgames.questjobs.packets.EnumChatAction;
import com.khazovgames.questjobs.packets.PacketChatData;
import com.khazovgames.questjobs.packets.PacketCreator;
import com.khazovgames.questjobs.packets.PacketSender;
import com.khazovgames.questjobs.particles.FireParticles;
import com.khazovgames.questjobs.quests.rewards.QuestReward;
import com.khazovgames.questjobs.quests.tasks.QuestTask;
import com.khazovgames.questjobs.utils.DoubleDispatchObserver;
import com.khazovgames.questjobs.utils.QuestUtils;

import net.minecraft.server.v1_10_R1.EnumChatFormat;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;

public class QuestGUI extends DoubleDispatchObserver{

	private static final String rewardsObjectiveName = "Quest Rewards";
	
	private static final int timePerReward = 30; //in ticks
	
	public void handle(QuestCompleteEvent event) {
		QuestJobs questJobs = QuestJobs.getPlugin(QuestJobs.class);
		
		Quest quest = event.getQuest();
		Player owner = quest.GetOwner();
		
		PacketSender.SendPacket(owner, PacketCreator.createTitlePacket(QuestUtils.createTitle(quest.GetName(), EnumChatFormat.YELLOW), EnumTitleAction.TITLE, 60, 40, 30));
		PacketSender.SendPacket(owner, PacketCreator.createTitlePacket(QuestUtils.createTitle("Complete", EnumChatFormat.GREEN), EnumTitleAction.SUBTITLE, 60, 40, 30));
		
		FireParticles fireParticles = new FireParticles(owner.getLocation(), 3, 1, 50);
		PacketSender.SendPacketsInSequence(owner, fireParticles.generatePackets(), 1);
		
		owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		
		Scoreboard scoreboard = owner.getScoreboard();
		if(scoreboard == null) scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Objective objective = scoreboard.getObjective(rewardsObjectiveName);
		if(objective == null) objective = scoreboard.registerNewObjective(rewardsObjectiveName, "");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		owner.setScoreboard(scoreboard);
		
		for(QuestReward reward : quest.GetRewards())
		{
			String entry = ChatColor.YELLOW + reward.toString();
			if(entry.length() > 30)
				entry = entry.substring(0, 27) + "..";
			
			Score score = objective.getScore(entry);
			score.setScore((int)reward.getAmount());
		}
		
		Bukkit.getScheduler().runTaskLater(questJobs, new Runnable() {

			@Override
			public void run() {
				Objective objective = owner.getScoreboard().getObjective(rewardsObjectiveName);
				if(objective != null) objective.unregister();
			}
			
		}, timePerReward * quest.GetRewards().length);
		
		if(quest.IsRepeatable())
			Bukkit.getScheduler().runTaskLater(questJobs, new Runnable() {

				@Override
				public void run() {
					PacketChatData chatData = new PacketChatData(ChatColor.GOLD + "This is a repeatable quest. You may complete it again.", EnumChatAction.ActionBar);
					PacketSender.SendPacket(owner, PacketCreator.createChatPacket(chatData));
				}
				
			}, 20);
	}
	
	public void handle(QuestStartEvent event) {
		Quest quest = event.getQuest();
		Player owner = quest.GetOwner();
		PacketSender.SendPacket(owner, PacketCreator.createTitlePacket(QuestUtils.createTitle(quest.GetName(), EnumChatFormat.YELLOW), EnumTitleAction.TITLE, 60, 40, 30));
		PacketSender.SendPacket(owner, PacketCreator.createTitlePacket(QuestUtils.createTitle("Accepted", EnumChatFormat.GREEN), EnumTitleAction.SUBTITLE, 60, 40, 30));
		
		owner.playSound(owner.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
	}

	@Override
	public void handle(QuestProgressUpdateEvent event) {
		Quest quest = event.getQuest();
		QuestTask task = event.getTask();
		
		ChatColor color;
		if(task.getProgress() < 0.5f)
			color = ChatColor.RED;
		else if(task.getProgress() < 1f)
			color = ChatColor.YELLOW;
		else
			color = ChatColor.GREEN;
		
		String text = color + "[" + quest.GetName() + " :: " + task + "] Progress: " + task.getProgressInPercent() + "% (" + task.getAmountComplete() + "/" + task.getAmountRequired() + ")";
		PacketChatData chatData = new PacketChatData(text, EnumChatAction.ActionBar);
		PacketSender.SendPacket(quest.GetOwner(), PacketCreator.createChatPacket(chatData));
	}

}
