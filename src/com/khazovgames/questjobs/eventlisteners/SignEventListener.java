package com.khazovgames.questjobs.eventlisteners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.khazovgames.questjobs.permissions.Permissions;
import com.khazovgames.questjobs.utils.Constants;
import com.khazovgames.questjobs.utils.QuestUtils;


public class SignEventListener implements Listener {

	@EventHandler
	public void onSignChangeEvent(SignChangeEvent event) {
		if(event.getLine(0).indexOf(Constants.questIdentifier) != -1) {
			Player placer = event.getPlayer();
			
			if(!placer.hasPermission(Permissions.Create)) {
				placer.sendMessage(
					ChatColor.YELLOW + "[Warning] You have used the characer " + Constants.questIdentifier + " in the first line\n"
					+ "of the sign.\n"
					+ "Unfortunately, this is a saved character for the QuestJobs\n"
					+ "plugin, so it will be removed.\n"
					+ "You may use this character on any other line of the sign."
				);
				
				event.setLine(0, event.getLine(0).replace(Constants.questIdentifier, (char)0));
			}
			else 
				placer.sendMessage(ChatColor.GREEN + "Successfully created a quest sign!");
		}
	}
	
	@EventHandler
	public void onSignBreakEvent(BlockBreakEvent event) {
		Material blockType = event.getBlock().getType(); 
		if(blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) 
		{
			Sign sign = (Sign) event.getBlock().getState();
			if(QuestUtils.isQuestSign(sign) && !event.getPlayer().hasPermission(Permissions.Create)) {
				event.getPlayer().sendMessage(ChatColor.RED + "You may not break this quest sign. Others may want to use it!");
				event.setCancelled(true);
			}
		}
	}
}
