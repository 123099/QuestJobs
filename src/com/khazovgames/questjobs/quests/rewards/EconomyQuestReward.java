package com.khazovgames.questjobs.quests.rewards;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.khazovgames.questjobs.externals.VaultHandler;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyQuestReward extends QuestReward{

	private static final long serialVersionUID = 6992608470159451500L;
	
	private transient VaultHandler vaultHandler;
	private transient Economy economy;
	
	public EconomyQuestReward(int amount) {
		super(amount);
	}

	@Override
	public boolean GiveReward(Player player) {
		if(!vaultHandler.IsEconomyInitialized())
			return false;
		
		OfflinePlayer offlinePlayer = vaultHandler.PlayerToOfflinePlayer(player);
		EconomyResponse response = economy.depositPlayer(offlinePlayer, amount);
		return response.transactionSuccess();
	}
	
	@Override
	public String toString() {
		return amount + " " + economy.currencyNamePlural();
	}
	
	private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		stream.defaultReadObject();
		vaultHandler = VaultHandler.GetInstance(); 
		economy = vaultHandler.GetEconomy();
	}
}
