package com.khazovgames.questjobs.particles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.khazovgames.questjobs.packets.PacketCreator;
import com.khazovgames.questjobs.packets.PacketParticleData;

import net.minecraft.server.v1_10_R1.EnumParticle;
import net.minecraft.server.v1_10_R1.Packet;

public class FireParticles {

	private Location location;
	private float height;
	private float radius;
	private float steps;
	
	public FireParticles(Location startLocation, float height, float radius, int steps) {
		location = startLocation;
		this.height = height;
		this.radius = radius;
		this.steps = steps;
	}
	
	public List<Packet> generatePackets(){
		ArrayList<Packet> packets = new ArrayList<>();
		
		for(float y = 0; y < height; y+=height/steps)
		{
			Location newLocation = location.clone();
			newLocation.add(radius * Math.sin(2 * Math.PI * y), y, radius * Math.cos(2 * Math.PI * y));
			PacketParticleData data = new PacketParticleData(EnumParticle.FLAME, newLocation, 0, 1);
			packets.add(PacketCreator.createParticlePacket(data));
		}
		
		return packets;
	}
}
