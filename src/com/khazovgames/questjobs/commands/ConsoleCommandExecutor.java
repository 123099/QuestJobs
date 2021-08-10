package com.khazovgames.questjobs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public abstract class ConsoleCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof ConsoleCommandSender) {
			ConsoleCommandSender console = (ConsoleCommandSender)sender;
			return onConsoleCommand(console, command, label, args);
		}
		else
			sender.sendMessage("This command may only be used by the console!");
		
		return false;
	}
	
	protected abstract boolean onConsoleCommand(ConsoleCommandSender console, Command command, String label, String[] args);

}
