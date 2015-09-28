package com.creeperevents.oggehej.rollerblades.versions;

import java.util.List;

import org.bukkit.entity.Player;

public interface NMS {
	public void sendParticlePacket(List<Player> players, String particleType, float x, float y, float z, float xSpread, float ySpread, float zSpread, int amount);
	public void sendParticlePacket(List<Player> players, String particleType, float x, float y, float z, float xSpread, float ySpread, float zSpread, int amount, int blockType);
}
