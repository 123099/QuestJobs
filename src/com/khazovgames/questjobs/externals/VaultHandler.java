package com.khazovgames.questjobs.externals;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.khazovgames.questjobs.QuestJobs;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultHandler extends ExternalPluginHandler{

	private static VaultHandler instance;
	public static VaultHandler GetInstance() {
		if(instance == null)
			instance = new VaultHandler();
		
		return instance;
	}
	
	private Economy economy;
	private Permission permissions;
	
	private VaultHandler() { super("Vault"); }
	
	public boolean Initialize(QuestJobs questJobs) {
		if(!super.Initialize(questJobs))
			return false;
		
		RegisteredServiceProvider<Economy> economyProvider = questJobs.getServer().getServicesManager().getRegistration(Economy.class);
		if(economyProvider == null) 
			return false;
		
		economy = economyProvider.getProvider();
		
		RegisteredServiceProvider<Permission> permissionProvider = questJobs.getServer().getServicesManager().getRegistration(Permission.class);
		if(permissionProvider == null)
			return false;
		
		permissions = permissionProvider.getProvider();
		
		return true;
	}
	
	public Economy GetEconomy() {
		return economy;
	}
	
	public boolean IsEconomyInitialized() {
		return economy != null;
	}
	
	public boolean IsPermissionInitialized() {
		return permissions != null;
	}
	
	public Permission GetPermissions() {
		return permissions;
	}
	
	public OfflinePlayer PlayerToOfflinePlayer(Player player) {
		return Bukkit.getOfflinePlayer(player.getUniqueId());
	}
}
