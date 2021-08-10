package com.khazovgames.questjobs.packets;

import net.minecraft.server.v1_10_R1.ChatModifier;

public class PacketChatData extends PacketData {

	private String text;
	private EnumChatAction chatAction;
	
	public PacketChatData(String text, EnumChatAction chatAction) {
		this.text = text;
		this.chatAction = chatAction;
	}
	
	public String getText() {
		return text;
	}
	
	public EnumChatAction getChatAction() {
		return chatAction;
	}
}
