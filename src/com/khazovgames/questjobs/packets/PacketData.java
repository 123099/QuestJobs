package com.khazovgames.questjobs.packets;

public abstract class PacketData {

	public String toJSON() {
		return "\"text\":\"You have completed the quest\",\"color\":\"green\"";
	}
}
