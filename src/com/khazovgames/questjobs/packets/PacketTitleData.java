package com.khazovgames.questjobs.packets;

import net.minecraft.server.v1_10_R1.ChatModifier;

public class PacketTitleData extends PacketData{

	private String text;
	private ChatModifier chatModifier;
	
	public PacketTitleData(String text, ChatModifier chatModifier) {
		this.text = text;
		this.chatModifier = chatModifier;
	}
	
	public String getText() {
		return text;
	}
	
	public ChatModifier getChatModifier() {
		return chatModifier;
	}
}
