# **QuestJobs - Questing Done Right!**

## Description

QuestJobs is a Minecraft Spigot/Bukkit plugin that allows users to create in-game quests and tasks that the players have to perform, and reward them for it. For example, you can create a quest where the player has to walk around and hunt for 10 zombies, and then he will receive 25 diamonds or maybe 150 experience points.

The plugin interacts with other Minecraft plugins to allow for more varied quests and rewards.

This is a project I have started around 2013-2014 when I was just 16 years old, and then reworked a bit again in 2016 when I started studying game engineering.

## Noteworthy Features

- **Quest creation through an external GUI tool**
- **Multiple tasks and rewards per quest**
- **Titles and Subtitles for important events**
- **In-game inventory UI for quest log**
- **Accept quests through signs**
- **Manage all quests with the /quest command**
- **Support for multiple reward types**
- **Support for multiple task types**

## Tasks

- **Killing mobs**
- **Collecting items**
- **Mining blocks**
- **Travelling to different worlds**
- **Talking to Cititzens NPCs**


## Rewards

- **Items**
- **Experience points**
- **Money for economy plugins**
- **Permissions**
- **Slimefun custom items**


**For Rewards and Tasks where the amount variable doesn't make sense, like the permissions reward or the npc talk to task, you can put any number in the amount field.
\*
Slimefun rewards\***

**The name of the item is in the form of AAA_BBB_CCC (caps insensitive).
For example, to give a steel jetpack to a player, the reward would be: "Steel_Jetpack"**​

## Config Options

- **Clear Complete - Allows clearing and abandoning complete quests. \*Repeatable quests are not considered complete.**

## Permissions

- **questjobs.create - Allows creating quest signs for users to accept quests.**

Quest Jobs is a remake of a plugin I have started making a few years back for Bukkit called QuestBoard.
Unfortunately, due to lack of time and the experience in programming, I never got to finish the plugin.
Now that I am studying game engineering, QuestBoard comes back as QuestJobs!

## User Interface

**The user interface for the plugin is nice and easy to use!
The server ops or anyone with the 'questjobs.create' permission needs to place a sign.
Then, somewhere in the first line add the character # followed by the ID of the quest that he would like the sign to represent.**​


For example:

![upload_2016-7-4_23-21-56](https://user-images.githubusercontent.com/21365830/128937447-dfb527ab-51a2-4c85-bcfd-0e68e189ac96.png)

As long as the first line contains the # character and the ID of the quest, you can put any other words anywhere on the sign.
Also, as you can see, you can have multiple signs representing the same quest. That means you can place signs for the same quest in different locations in the world.

The user can see their quest log with all their accepted quests by typing:
**/quest or /quest show**
This will open an inventory with books, each per accepted quest.
The book contains the description of the quest and the current progress.

You can also abandon all quests or abandon a specific quest.
All of this is described in the help command of the plugin, accessible via **/quest help

## Commands

- **quest - Shows the quest log with all the active quests**
- **quest show - Same as above.**
- **quest show complete - Shows the quest log with all the active and complete quests.**
- **quest abandon [ID/Name] - Abandons a quest with the ID or name provided. Does not allow abandoning complete quests if the config is set to false.**
- **quest clear - Clears the quest log. Does not clear complete quests if the config is set to false.**
- **quest help - shows a help message with all the commands.**


## Creating quests is more fun!
By double clicking on the plugin's jar file, a GUI will open, and there you can edit or create new quests for the plugin!

![8012d0fe694d2cb32e8bd8d7dd61c531edfb575e](https://user-images.githubusercontent.com/21365830/128937497-498c3924-0bd4-424d-a219-449369404292.png)

Some in-game images of the current version of the plugin:

User Experience:

![upload_2016-7-15_8-30-31](https://user-images.githubusercontent.com/21365830/128937554-4c0940d4-dd41-4b18-9da4-7cdc1decebb8.png)

On-join quest progress announcement:

![upload_2016-7-15_8-27-52](https://user-images.githubusercontent.com/21365830/128937574-46eb2204-5383-438e-b342-4dfbe90a5cab.png)

The quest log:

![upload_2016-7-6_10-10-13](https://user-images.githubusercontent.com/21365830/128937586-ecc9ec74-a808-4533-929d-907b2f85ef67.png)

A test quest:

![upload_2016-7-6_10-10-25](https://user-images.githubusercontent.com/21365830/128937609-beb3aaf7-6163-4fe9-8ba6-d64f00daffeb.png)
![upload_2016-7-6_10-10-32](https://user-images.githubusercontent.com/21365830/128937614-303cd515-6c72-4f67-9631-31484404f114.png)

On quest completion:

![upload_2016-7-15_8-32-22](https://user-images.githubusercontent.com/21365830/128937629-0ecdbca2-ae25-4107-b855-6ec68ccafe94.png)

Economy quest rewards:

![upload_2016-7-6_20-27-14](https://user-images.githubusercontent.com/21365830/128937647-807497fd-5485-46e0-ac18-a6a95a1bfc94.png)

Quest progress tracking:

![upload_2016-7-12_22-11-59](https://user-images.githubusercontent.com/21365830/128937660-a83e360d-1ff8-4172-a328-48eb1d1526d5.png)

## Plugin release page

https://www.spigotmc.org/resources/quest-jobs.25884/?__cf_chl_jschl_tk__=pmd_71b57e7fcaa37b35106158e230512576f17f956a-1628629951-0-gqNtZGzNAfijcnBszQi6
