package com.khazovgames.questjobs.quests.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.khazovgames.questjobs.externals.CitizensHandler;
import com.khazovgames.questjobs.utils.SerializeField;

import net.citizensnpcs.api.ai.speech.SpeechContext;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class TalkQuestTask extends QuestTask {

	private static final long serialVersionUID = 5290935586751239345L;
	
	@SerializeField(name="Citizens NPC ID")
	private int npcID;
	
	public TalkQuestTask(Player taskOwner, int amountRequired, int npcID) {
		super(taskOwner, 1);
		this.npcID = npcID;
	}

	@Override
	public void onQuestStart() {
	}

	@Override
	public void onQuestEnd() {

	}

	@Override
	public String getSimpleName() {;
		return CitizensHandler.GetInstance().GetNPCName(npcID);
	}
	
	@Override
	public String toString() {
		return "Speak to " + getSimpleName();
	}

	@EventHandler
	public void onTalkToNpc(NPCRightClickEvent event) {
		if(event.getClicker() == taskOwner)
		{
			if(event.getNPC().getId() == npcID)
			{
				event.getNPC().getDefaultSpeechController().speak(new SpeechContext(ChatColor.GOLD + "A message from " + quest.GetGiver() + "? Thank you!"));
				setAmountComplete(amountRequired);
				quest.CheckCompletion();
			}
		}
	}
}
