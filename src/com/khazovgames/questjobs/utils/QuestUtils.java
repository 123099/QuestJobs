package com.khazovgames.questjobs.utils;

import org.bukkit.block.Sign;

import com.khazovgames.questjobs.packets.PacketTitleData;

import net.minecraft.server.v1_10_R1.ChatModifier;
import net.minecraft.server.v1_10_R1.EnumChatFormat;

public final class QuestUtils {

	public static int getQuestIDFromString(String string) {
		int identifierIndex = string.indexOf(Constants.questIdentifier);
		if(identifierIndex != -1) 
		{
			String IDString = "";
			for(int i = identifierIndex + 1; i < string.length(); ++i) 
			{
				char currentChar = string.charAt(i);
				if(Character.isDigit(currentChar))
					IDString += currentChar;
				else if(IDString.isEmpty() && currentChar == ' ')//if no numbers were found after hashtag and there are spaces, keep looking
					continue;
				else
					break;
			}
			
			if(!IDString.isEmpty()) 
				return Integer.parseInt(IDString);
		}
		
		return -1;
	}
	
	public static boolean isQuestSign(Sign sign) {
		String firstLine = sign.getLine(0); 
		int questID = getQuestIDFromString(firstLine);
		return questID != -1;
	}
	
	public static PacketTitleData createTitle(String text, EnumChatFormat color) {
		ChatModifier modifier = new ChatModifier();
		modifier.setColor(color);
		
		PacketTitleData data = new PacketTitleData(text, modifier);
		return data;
	}
}
