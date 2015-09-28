package com.creeperevents.oggehej.rollerblades.versions;

import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class vSomething implements NMS
{
	@Override
	public void sendParticlePacket(List<Player> players, String particleType, float x, float y, float z, float xSpread, float ySpread, float zSpread, int amount) {
		if(particleType.equals(EffectEnum.FLAME))
			for(Player player : players) {
				for(int i = 1; i <= amount / 25; i++)
					player.getWorld().playEffect(new Location(player.getWorld(), x, y, z), Effect.MOBSPAWNER_FLAMES, null);
				return;
			}
		else if(particleType.equals(EffectEnum.SPELL_WITCH))
			for(Player player : players) {
				for(int i = 1; i <= amount; i++) {
					Random r = new Random();
					player.getWorld().playEffect(new Location(player.getWorld(),
							x + (((float) r.nextInt((int)(xSpread * 100 * 2)) / 100)) - xSpread,
							y + (((float) r.nextInt((int)(ySpread * 100 * 2)) / 100)) - ySpread,
							z + (((float) r.nextInt((int)(zSpread * 100 * 2)) / 100)) - zSpread), Effect.WITCH_MAGIC, 1);
				}
				return;
			}
		else if(particleType.equals(EffectEnum.SMOKE_LARGE))
			for(Player player : players) {
				for(int i = 1; i <= amount; i++) {
					Random r = new Random();
					player.getWorld().playEffect(new Location(player.getWorld(),
							x + (((float) r.nextInt((int)(xSpread * 100 * 2)) / 100)) - xSpread,
							y + (((float) r.nextInt((int)(ySpread * 100 * 2)) / 100)) - ySpread,
							z + (((float) r.nextInt((int)(zSpread * 100 * 2)) / 100)) - zSpread), Effect.SMOKE, 1);
				}
				return;
			}
	}

	@Override
	public void sendParticlePacket(List<Player> players, String particleType, float x, float y, float z, float xSpread, float ySpread, float zSpread, int amount, int blockType) {
		this.sendParticlePacket(players, particleType, x, y, z, xSpread, ySpread, zSpread, amount);
	}
}
