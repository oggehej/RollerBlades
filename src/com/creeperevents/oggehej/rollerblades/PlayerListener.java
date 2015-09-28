package com.creeperevents.oggehej.rollerblades;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlayerListener implements Listener {
	private RollerBlades plugin;
	PlayerListener(RollerBlades instance) {
		plugin = instance;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Material runningOn = player.getLocation().subtract(0D, 1D, 0D).getBlock().getType();

		// Make sure the player is running on the right surface and is moving
		if(plugin.getConfig().getList("Blocks").contains(runningOn.getId())
				&& (event.getFrom().getZ() != event.getFrom().getZ() || (event.getFrom().getX() != event.getTo().getX()))) 
		{
			boolean wgPass = false;
			List<String> acceptedRegions = plugin.getConfig().getStringList("Regions");
			if(acceptedRegions.contains(player.getWorld().getName()) || acceptedRegions.contains("all"))
				wgPass = true;
			else if(plugin.wg != null)
				for(ProtectedRegion r : plugin.wg.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()))
					if(acceptedRegions.contains(r.getId())) {
						wgPass = true;
						break;
					}

			if(wgPass) {
				if(player.getVelocity().getY() > 0 && !(runningOn == Material.AIR)) // If player is jumping {
					if(runningOn != Material.AIR) {
						plugin.getStorage().addRunning(player);
						RunningPlayer p = plugin.getStorage().getPlayer(player);
						p.gearUp();
						p.stopSprint();
					}

					player.setVelocity(player.getVelocity().multiply(2).setY(0));
				} else if(plugin.getStorage().isRunning(player)) {
					if(player.isSprinting())
						plugin.getStorage().stopPlayer(player);
					else {
						RunningPlayer p = plugin.getStorage().getPlayer(player);
						if(player.isSneaking()) {
							if(!p.gearDown())
								plugin.getStorage().stopPlayer(player);
							player.setSneaking(false); // Needed to stop bugs
						} else if(runningOn != Material.AIR)
							p.runParticles(runningOn);
					}
				}
			else
				plugin.getStorage().stopPlayer(player);
		} else if(plugin.getStorage().isRunning(player))
			plugin.getStorage().stopPlayer(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.getStorage().stopPlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		if(plugin.getCache().isConfigurationSection(uuid)) {
			System.out.println("[" + plugin.getName() + "] Restoring player values from cache");
			player.setFoodLevel(plugin.getCache().getInt(uuid + ".hunger"));
			player.setExp((float) plugin.getCache().getDouble(uuid + ".exp"));
			player.setLevel(plugin.getCache().getInt(uuid + ".level"));
			player.setGameMode(GameMode.valueOf(plugin.getCache().getString(uuid + ".gamemode")));
			player.setWalkSpeed((float) plugin.getCache().getDouble(uuid + ".walkspeed"));

			plugin.getCache().set(event.getPlayer().getUniqueId().toString(), null);
			plugin.saveCache();
		}
	}
}
