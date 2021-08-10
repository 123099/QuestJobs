package com.khazovgames.questjobs.utils;

import org.bukkit.ChatColor;

public final class ChatUtils {

	private static ChatColor titleColor = ChatColor.GOLD;
	private static ChatColor commandNameColor = ChatColor.GREEN;
	private static ChatColor commandDescColor = ChatColor.AQUA;
	
	public static String GetTitle(String title) {
		return titleColor + "[------<" + title + ">------]";
	}
	
	public static String GetCommandHelp(String commandName, String commandDesc) {
		return commandNameColor + commandName + ChatColor.WHITE + " - " + commandDescColor + commandDesc;
	}
	
	@SuppressWarnings("rawtypes")
	public static String EnumToString(Enum enumValue) {
		String name = enumValue.toString().toLowerCase().replace('_', ' ');
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
}
