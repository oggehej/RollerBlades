package com.creeperevents.oggehej.rollerblades;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.creeperevents.oggehej.rollerblades.exceptions.CooldownException;
import com.creeperevents.oggehej.rollerblades.exceptions.NoGearException;
import com.creeperevents.oggehej.rollerblades.versions.EffectEnum;

public class RunningPlayer
{
	private Player player;
	private CooldownRunnable cooldown = null;
	private Gear gear = Gear.NONE;
	private RollerBlades plugin;

	private int prevHunger;
	private float prevExp;
	private int prevLevel;
	private GameMode prevGamemode;
	private float prevWalkspeed;

	RunningPlayer(Player player, RollerBlades plugin)
	{
		this.player = player;
		this.plugin = plugin;
		this.prevHunger = player.getFoodLevel();
		this.prevExp = player.getExp();
		this.prevLevel = player.getLevel();
		this.prevGamemode = player.getGameMode();
		this.prevWalkspeed = player.getWalkSpeed();

		// Put player states in the cache
		String uuid = player.getUniqueId().toString();
		plugin.getCache().set(uuid + ".hunger", prevHunger);
		plugin.getCache().set(uuid + ".exp", (double) prevExp);
		plugin.getCache().set(uuid + ".level", prevLevel);
		plugin.getCache().set(uuid + ".gamemode", prevGamemode.toString());
		plugin.getCache().set(uuid + ".walkspeed", prevWalkspeed);
		plugin.saveCache();

		player.setSaturation(20);
		player.setExp(0);
		player.setLevel(0);
		player.setGameMode(GameMode.ADVENTURE);
	}

	/**
	 * Gear up the player
	 */
	public void gearUp()
	{
		try {
			if(cooldown != null)
				throw new CooldownException();
			Gear nextGear = gear.nextGear();
			this.gear = nextGear;
			player.setWalkSpeed(gear.getSpeed(plugin));
			Effects.FLAME.play(player, plugin);
			if(!(gear == Gear.HIGH))
				cooldown();
			player.setLevel(gear.getGear());
		} catch (NoGearException e) {
			Effects.BURP.play(player, plugin);
		} catch (CooldownException e) {
			Effects.COOL.play(player, plugin);
		}
	}

	/**
	 * Gear down the player
	 * 
	 * @return false if there's now lower gear
	 */
	public boolean gearDown()
	{
		try {
			if(cooldown != null)
				cooldown.cancel();
			Gear prevGear = gear.prevGear();
			this.gear = prevGear;
			player.setWalkSpeed(gear.getSpeed(plugin));
			Effects.FLAME.play(player, plugin);
			player.setLevel(gear.getGear());
			return true;
		} catch (NoGearException e) {
			stop();
			return false;
		}
	}

	/**
	 * Show the running particles under the players feet
	 * 
	 * @param mat
	 */
	@SuppressWarnings("deprecation")
	public void runParticles(Material mat)
	{
		Location loc = player.getLocation();
		List<Player> list = player.getWorld().getPlayers();
		plugin.getNMS().sendParticlePacket(list, EffectEnum.BLOCK_CRACK, (float) loc.getX(), (float) loc.getY() + 0.25F, (float) loc.getZ(), 0F, 0.5F, 0F, (int)(player.getWalkSpeed() * 10), mat.getId());
		for(Player p : list)
			p.playSound(loc, Sound.WOLF_WALK, 1, 1);
	}

	/**
	 * Set all player values to what they were before the player ran
	 */
	void stop()
	{
		if(cooldown != null)
			cooldown.cancel();
		Effects.CRASH.play(player, plugin);
		player.setLevel(prevLevel);
		player.setExp(prevExp);
		player.setWalkSpeed(prevWalkspeed);
		player.setFoodLevel(prevHunger);
		player.setGameMode(prevGamemode);

		plugin.getCache().set(player.getUniqueId().toString(), null);
		plugin.saveCache();
	}

	/**
	 * Stop the sprinting player
	 */
	public void stopSprint()
	{
		player.setFoodLevel(6);
		BukkitRunnable run = new BukkitRunnable()
		{
			@Override
			public void run() {
				player.setFoodLevel(20);
			}
		};
		run.runTaskLater(plugin, 1);
	}

	/**
	 * Run the gear cool down timer
	 */
	private void cooldown()
	{
		CooldownRunnable runnable = new CooldownRunnable();
		cooldown = runnable;
		cooldown.runTaskTimer(plugin, 0, 1);
	}

	private class CooldownRunnable extends BukkitRunnable
	{
		private int num = 0;
		private int ticks = plugin.getConfig().getInt("Cooldown");

		@Override
		public void run()
		{
			player.setExp((float) num / ticks);
			if(num >= ticks)
				cancel();
			num++;
		}

		@Override
		public synchronized void cancel()
		{
			player.setExp(1F);
			super.cancel();
			cooldown = null;
		}
	}
}
