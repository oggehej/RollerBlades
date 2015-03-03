package com.creeperevents.oggehej.rollerblades;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.creeperevents.oggehej.rollerblades.versions.EffectEnum;

public enum Effects
{
	BURP()
	{
		public void play(Player player, RollerBlades plugin)
		{
			Location loc = player.getLocation();
			List<Player> list = new ArrayList<Player>();
			list.add(player);
			plugin.getNMS().sendParticlePacket(list, EffectEnum.SPELL_WITCH, (float) loc.getX(), (float) loc.getY() + 0.25F, (float) loc.getZ(), 0.25F, 0.5F, 0.25F, 25);
			player.playSound(loc, Sound.BURP, 1, 1);
		}
	},
	CRASH()
	{
		public void play(Player player, RollerBlades plugin)
		{
			Location loc = player.getLocation();
			List<Player> list = player.getWorld().getPlayers();
			plugin.getNMS().sendParticlePacket(list, EffectEnum.SMOKE_LARGE, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.5F, 0.5F, 0.5F, 50);
			for(Player p : list)
				p.playSound(loc, Sound.BLAZE_HIT, 1, 1);
		}
	},
	FLAME()
	{
		public void play(Player player, RollerBlades plugin)
		{
			Location loc = player.getLocation();
			List<Player> list = player.getWorld().getPlayers();
			plugin.getNMS().sendParticlePacket(list, EffectEnum.FLAME, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.3F, 1.5F, 0.3F, 25);
			for(Player p : list)
				p.playSound(loc, Sound.FIREWORK_BLAST, 1, 1);
			player.playSound(loc, Sound.FIREWORK_BLAST2, 1, 1);
		}
	},
	COOL()
	{
		public void play(Player player, RollerBlades plugin)
		{
			Location loc = player.getLocation();
			List<Player> list = new ArrayList<Player>();
			list.add(player);
			plugin.getNMS().sendParticlePacket(list, EffectEnum.SPELL_WITCH, (float) loc.getX(), (float) loc.getY() + .25F, (float) loc.getZ(), 0.25F, 0.5F, 0.25F, 25);
			player.playSound(loc, Sound.CLICK, 1, 1);
		}
	};

	public abstract void play(Player player, RollerBlades plugin);
}
