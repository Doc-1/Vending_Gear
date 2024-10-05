package com.docvin.vending_gear.capabilities;

import com.docvin.vending_gear.VendingGear;
import com.docvin.vending_gear.capabilities.potion.PotionsDrankProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {
	public static final ResourceLocation POTIONS_DRANK = new ResourceLocation(VendingGear.MODID, "potions_drank");

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityPlayer))
			return;
		event.addCapability(POTIONS_DRANK, new PotionsDrankProvider());
	}
}
