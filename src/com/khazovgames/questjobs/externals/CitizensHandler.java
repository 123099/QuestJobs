package com.khazovgames.questjobs.externals;

import com.khazovgames.questjobs.QuestJobs;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class CitizensHandler extends ExternalPluginHandler {
	
	private static CitizensHandler instance;
	public static CitizensHandler GetInstance() {
		if(instance == null)
			instance = new CitizensHandler();
		
		return instance;
	}
	
	private CitizensHandler() { super("Citizens"); }
	
	public boolean Initialize(QuestJobs questJobs) {
		if(!super.Initialize(questJobs))
			return false;
		
		return true;
	}
	
	public String GetNPCName(int npcID) {
		for(NPCRegistry registry : CitizensAPI.getNPCRegistries())
			for(NPC npc : registry)
			{
				if(npc.getId() == npcID)
					return npc.getFullName();
			}
		
		return "Unknown NPC";
	}
}
