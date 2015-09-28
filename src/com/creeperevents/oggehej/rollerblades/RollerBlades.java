package com.creeperevents.oggehej.rollerblades;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.creeperevents.oggehej.rollerblades.versions.NMS;
import com.creeperevents.oggehej.rollerblades.versions.vSomething;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RollerBlades extends JavaPlugin {
	private NMS nmsHandler;
	private PlayerStorage storage;
	private File cacheFile;
	private FileConfiguration cache;
	WorldGuardPlugin wg = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

	public void onEnable() {
		setupNMS();
		setupCache();

		storage = new PlayerStorage(this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

		getCommand("RollerBlades").setExecutor(new CommandHandler(this));

		getConfig().options().copyDefaults(true);
		saveConfig();
		
		if(wg == null)
			getLogger().warning("Not hooking up with WorldGuard!");
		else
			getLogger().info("Using WorldGuard");
	}

	public void onDisable() {
		System.out.println("Proper shutdown");
		storage.stopAllPlayers();
	}

	/**
	 * Get the player storage
	 * 
	 * @return PlayerStorage
	 */
	public PlayerStorage getStorage() {
		return storage;
	}

	/**
	 * Get the class responsible for NMS functions
	 * 
	 * @return
	 */
	public NMS getNMS() {
		return this.nmsHandler;
	}

	/**
	 * Get the player cache
	 * 
	 * @return Cache
	 */
	public FileConfiguration getCache() {
		return this.cache;
	}
	
	/**
	 * Save the cache
	 */
	public void saveCache() {
		try {
			cache.save(cacheFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set up the NMS functions
	 */
	private void setupNMS() {
		String packageName = this.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		try {
			final Class<?> clazz = Class.forName(getClass().getPackage().getName() + ".versions." + version);
			if (NMS.class.isAssignableFrom(clazz)) {
				getLogger().info("Using NMS version " + version);
				this.nmsHandler = (NMS) clazz.getConstructor().newInstance();
			}
		} catch (Exception e) {
			getLogger().info("Couldn't find support for " + version +". Using failproof (but laggy and less fancy) option.");
			this.nmsHandler = new vSomething();
		}
	}

	/**
	 * Set up the player cache
	 */
	private void setupCache() {
		cacheFile = new File(getDataFolder(), "cache.yml");

		try {
			cacheFile.getParentFile().mkdirs();
			cacheFile.createNewFile();
		} catch(Exception e) {
			e.printStackTrace();
		}

		cache = new YamlConfiguration();

		try {
			cache.load(cacheFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
