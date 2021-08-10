package com.khazovgames.questjobs.quests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.entity.Player;

public final class QuestIO {	
	
	public static final String questsFolderRelativeToPlugin = "QuestJobs/Quests/";
	public static final String questsFolderRelativeToBukkit = "plugins/QuestJobs/Quests/";
	
	public static Quest LoadQuestFromConfig(Player questOwner, int questID) throws IOException {
		return LoadQuestFromConfig(questOwner, questID, questsFolderRelativeToBukkit);
	}
	
	public static Quest LoadQuestFromConfig(Player questOwner, int questID, String folderPath) throws IOException {
		return LoadQuestFromConfig(questOwner, getQuestFile(questID, folderPath));
	}
	
	public static Quest LoadQuestFromConfig(Player questOwner, File questFile) throws IOException{
		if(questFile == null)
			return null;
		
		Quest quest = null;
		try (FileInputStream fileStream = new FileInputStream(questFile))
		{
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			quest = (Quest)objectStream.readObject();
			objectStream.close();
		} 
		catch (ClassNotFoundException e) { e.printStackTrace(); } 
		
		quest.SetOwner(questOwner);
		return quest;
	}
	
	private static File getQuestFile(int questID, String folderPath) {
		File folder = new File(folderPath);
		File[] quests = folder.listFiles();
		if(quests != null) {
			for(File quest : quests) 
				if(quest.isFile() && quest.getName().split(" ")[0].equals(Integer.toString(questID))) 
					return quest;
		}
		
		return null;
	}
	
	public static String SaveQuestToConfig(Quest quest, String folderPath) {
		File questFile = getQuestFile(quest.GetID(), folderPath);
		if(questFile != null && questFile.exists())
			questFile.delete();
		
		String questFileName = quest.GetID() + " - " + quest.GetName() + ".quest";
		try{
			File file = new File(folderPath + questFileName);
			File folder = file.getParentFile();
			if(!folder.exists()) folder.mkdirs();
			file.createNewFile();
			
			FileOutputStream fileStream = new FileOutputStream(folderPath + questFileName);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(quest);
			objectStream.close();
			fileStream.close();
			
			return questFileName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
