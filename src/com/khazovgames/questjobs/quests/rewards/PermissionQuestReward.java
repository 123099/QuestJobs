package com.khazovgames.questjobs.quests.rewards;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.entity.Player;

import com.khazovgames.questjobs.externals.VaultHandler;
import com.khazovgames.questjobs.utils.SerializeField;

import net.milkbowl.vault.permission.Permission;

public class PermissionQuestReward extends QuestReward {

	private static final long serialVersionUID = 6895365525885108636L;

	private transient VaultHandler vaultHandler;
	private transient Permission permissions;
	
	@SerializeField(name="Permission to Give")
	private String permission;
	
	public PermissionQuestReward(String permission, int amount) {
		super(amount);
		this.permission = permission;
	}

	@Override
	public boolean GiveReward(Player player) {
		if(!vaultHandler.IsPermissionInitialized())
			return false;
		
		permissions.playerAdd(player, permission);
		return true;
	}
	
	private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		stream.defaultReadObject();
		vaultHandler = VaultHandler.GetInstance(); 
		permissions = vaultHandler.GetPermissions();
	}

	@Override
	public String toString() {
		return "\"" + permission + "\"" + " permission";
	}
}
