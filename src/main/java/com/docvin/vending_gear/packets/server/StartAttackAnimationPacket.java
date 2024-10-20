package com.docvin.vending_gear.packets.server;

import java.util.Random;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.docvin.vending_gear.entities.vending_machine.VendingGearTankEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

public class StartAttackAnimationPacket extends CreativeCorePacket {
	private int entityID;

	public StartAttackAnimationPacket() {
	}

	public StartAttackAnimationPacket(int entityID) {
		this.entityID = entityID;
	}

	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeInt(entityID);
	}

	@Override
	public void readBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
	}

	@Override
	public void executeClient(EntityPlayer player) {
		VendingGearTankEntity vendingTank = (VendingGearTankEntity) player.world.getEntityByID(entityID);
		vendingTank.startAttackAnim = true;
		Random rand = new Random();
		double motX = rand.nextGaussian() * 0.1D;
		double motZ = rand.nextGaussian() * 0.1D;
		player.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, vendingTank.posX + motX, vendingTank.posY + 1,
				vendingTank.posZ + motZ, motX, -0.0D, motZ);
	}

	@Override
	public void executeServer(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

}
