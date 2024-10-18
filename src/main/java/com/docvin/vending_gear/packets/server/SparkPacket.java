package com.docvin.vending_gear.packets.server;

import java.util.Random;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

public class SparkPacket extends CreativeCorePacket {

	private double x, y, z;

	public SparkPacket() {
		// TODO Auto-generated constructor stub
	}

	public SparkPacket(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}

	@Override
	public void readBytes(ByteBuf buf) {
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}

	@Override
	public void executeClient(EntityPlayer player) {
		Random rand = new Random();
		double motX = rand.nextGaussian() * 0.1D;
		double motZ = rand.nextGaussian() * 0.1D;
		player.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, x + motX, y, z + motZ, motX, 0.1D, motZ);
		player.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + motX, y, z + motZ, motX, 0.0D, motZ);
	}

	@Override
	public void executeServer(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

}
