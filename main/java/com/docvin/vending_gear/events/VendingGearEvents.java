package com.docvin.vending_gear.events;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.docvin.vending_gear.capabilities.potion.IPotionsDrank;
import com.docvin.vending_gear.capabilities.potion.PotionsDrankProvider;
import com.docvin.vending_gear.packets.server.DrankEnoughPotionPacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class VendingGearEvents {

	/**
	 * Checks to see if the player has consumed a potion. If done so, it will
	 * increase said player's capability of PotionsDrank by 1. Upon reaching the
	 * goal for drinking X number of potions an event will occur. (This will later
	 * be to spawn the Vending Gear)
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onPotionDrank(LivingEntityUseItemEvent event) {
		if (event instanceof LivingEntityUseItemEvent.Finish && event.getEntityLiving() instanceof EntityPlayerMP
				&& event.getItem().getItem().equals(Items.POTIONITEM)) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
			IPotionsDrank potionsDrank = player.getCapability(PotionsDrankProvider.POTIONS_DRANK, null);
			potionsDrank.add();
			String message = String.format("You have drank §7%d§r potions.", (int) potionsDrank.totalDrank());
			message += String.format(" Something will happen when you drink §7%d§r potions.",
					(int) potionsDrank.drinksNeeded());
			player.sendMessage(new TextComponentString(message));
			if (potionsDrank.hasDrankEnough()) {
				PacketHandler.sendPacketToPlayer(new DrankEnoughPotionPacket(), player);
			}
		}
	}

	/**
	 * Tempary method for debuging the capability of the player
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onPlayerLogsIn(PlayerLoggedInEvent event) {
		if (!(event.player instanceof EntityPlayerMP))
			return;
		EntityPlayer player = event.player;
		IPotionsDrank potionsDrank = player.getCapability(PotionsDrankProvider.POTIONS_DRANK, null);

		String message = String.format("You have drank §7%d§r potions.", (int) potionsDrank.totalDrank());
		message += String.format(" Something will happen when you drink §7%d§r potions.",
				(int) potionsDrank.drinksNeeded());
		player.sendMessage(new TextComponentString(message));
	}
}
