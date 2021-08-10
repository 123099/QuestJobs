package com.khazovgames.questjobs.commands.questcommands;

import com.khazovgames.questjobs.commands.CommandNames;
import com.khazovgames.questjobs.commands.PlayerCommandExecutor;

public class PlayerCommandQuest extends PlayerCommandExecutor{
	
	public PlayerCommandQuest() {
		getExecutors().setDefaultExecutor(new PlayerCommandQuestShow());
		getExecutors().registerExecutor(CommandNames.QuestShow, new PlayerCommandQuestShow());
		getExecutors().registerExecutor(CommandNames.QuestAbandon, new PlayerCommandQuestAbandon());
		getExecutors().registerExecutor(CommandNames.QuestClear, new PlayerCommandQuestClear());
		getExecutors().registerExecutor(CommandNames.QuestHelp, new PlayerCommandQuestHelp());
	}
}
