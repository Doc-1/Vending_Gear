package com.docvin.vending_gear.packets.server;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class DrankEnoughPotionPacket extends CreativeCorePacket {
	public DrankEnoughPotionPacket() {

	}

	@Override
	public void writeBytes(ByteBuf buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readBytes(ByteBuf buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeClient(EntityPlayer player) {
		player.addVelocity(0, 4, 0);

	}

	@Override
	public void executeServer(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

}
