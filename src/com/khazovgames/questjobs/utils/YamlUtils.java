package com.khazovgames.questjobs.utils;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public final class YamlUtils {

	public static String FormatPath(String... tags) {
		String path = "";
		for(String tag : tags) {
			path += tag + ".";
		}
		
		return path.substring(0, path.length()-1);
	}
	
	@SuppressWarnings("unchecked")
	public static <V> HashMap<String, V> GetHashMap(FileConfiguration config, String path){
		HashMap<String,V> hashMap = new HashMap<String,V>();
		
		ConfigurationSection configSection = config.getConfigurationSection(path);
		if(configSection == null) return hashMap;

		for(String key : configSection.getKeys(false)) 				
			hashMap.put(key, (V) config.get(path + "." + key));
		
		return hashMap;
	}
	
	@SuppressWarnings("unchecked")
	public static <V extends HashMap> HashMap<String, V> GetDoubleHashMap(FileConfiguration config, String path){
		HashMap<String,V> hashMap = new HashMap<String,V>();
		
		ConfigurationSection configSection = config.getConfigurationSection(path);
		if(configSection == null) return hashMap;

		for(String key : configSection.getKeys(false)) 				
			hashMap.put(key, (V) GetHashMap(config, path + "." + key));
		
		return hashMap;
	}
}
