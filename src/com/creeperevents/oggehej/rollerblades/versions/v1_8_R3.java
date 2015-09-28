package com.creeperevents.oggehej.rollerblades.versions;

import java.util.List;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class v1_8_R3 implements NMS {
	@Override
	public void sendParticlePacket(List<Player> players, String particleType, float x, float y, float z, float xSpread, float ySpread, float zSpread, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particleType), false, x, y, z, xSpread, ySpread, zSpread, 0, amount);
		for(Player player : players)
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void sendParticlePacket(List<Player> players, String particleType, float x, float y, float z, float xSpread, float ySpread, float zSpread, int amount, int blockType) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particleType), false, x, y, z, xSpread, ySpread, zSpread, 0, amount, blockType);
		for(Player player : players)
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}
