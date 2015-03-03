package com.creeperevents.oggehej.rollerblades;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerStorage
{
	private HashMap<UUID, RunningPlayer> map = new HashMap<UUID, RunningPlayer>();

	private RollerBlades plugin;
	PlayerStorage(RollerBlades instance)
	{
		plugin = instance;
	}

	/**
	 * See if the player exist in storage
	 * 
	 * @param player Player
	 * @return isRunning
	 */
	public boolean isRunning(Player player)
	{
		return map.containsKey(player.getUniqueId());
	}

	/**
	 * Get the RunningPlayer object for a player. Returns null if none.
	 * 
	 * @param player Player
	 * @return RunningPlayer
	 */
	public RunningPlayer getPlayer(Player player)
	{
		return map.get(player.getUniqueId());
	}

	/**
	 * Put a new player in the storage. Will not override.
	 * 
	 * @param player Player
	 */
	public void addRunning(Player player)
	{
		if(!isRunning(player))
			map.put(player.getUniqueId(), new RunningPlayer(player, plugin));
	}

	/**
	 * Remove the player from storage in a safe way
	 * 
	 * @param player Player
	 */
	public void stopPlayer(Player player)
	{
		if(isRunning(player))
			getPlayer(player).stop();
		map.remove(player.getUniqueId());
	}

	/**
	 * Remove all the players from storage in a safe way
	 */
	void stopAllPlayers()
	{
		for(UUID uuid : map.keySet())
		{
			map.get(uuid).stop();
			map.remove(uuid);
		}
	}
}
