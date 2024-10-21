package com.docvin.vending_gear.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.docvin.vending_gear.capabilities.potion.IPotionsDrank;
import com.docvin.vending_gear.capabilities.potion.PotionsDrankProvider;
import com.docvin.vending_gear.entities.vending_machine.VendingGearTankEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
			System.out.println("hi");
			if (potionsDrank.hasDrankEnough()) {
				VendingGearTankEntity entity = new VendingGearTankEntity(player.world);
				BlockPos pos = new BlockPos(player.posX, 250, player.posZ);
				entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
				player.world.spawnEntity(entity);

				potionsDrank.reset();

			}
		}
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.world.isRemote)
			return;
		if (entity instanceof VendingGearTankEntity) {
			List<PotionType> list = new ArrayList<PotionType>();
			list.add(PotionTypes.AWKWARD);
			list.add(PotionTypes.FIRE_RESISTANCE);
			list.add(PotionTypes.HARMING);
			list.add(PotionTypes.HEALING);
			list.add(PotionTypes.INVISIBILITY);
			list.add(PotionTypes.LEAPING);
			list.add(PotionTypes.LONG_FIRE_RESISTANCE);
			list.add(PotionTypes.LONG_INVISIBILITY);
			list.add(PotionTypes.LONG_LEAPING);
			list.add(PotionTypes.LONG_NIGHT_VISION);
			list.add(PotionTypes.LONG_POISON);
			list.add(PotionTypes.LONG_REGENERATION);
			list.add(PotionTypes.LONG_SLOWNESS);
			list.add(PotionTypes.LONG_STRENGTH);
			list.add(PotionTypes.LONG_SWIFTNESS);
			list.add(PotionTypes.LONG_WATER_BREATHING);
			list.add(PotionTypes.LONG_WEAKNESS);
			list.add(PotionTypes.MUNDANE);
			list.add(PotionTypes.NIGHT_VISION);
			list.add(PotionTypes.POISON);
			list.add(PotionTypes.REGENERATION);
			list.add(PotionTypes.SLOWNESS);
			list.add(PotionTypes.STRENGTH);
			list.add(PotionTypes.STRONG_HARMING);
			list.add(PotionTypes.STRONG_HEALING);
			list.add(PotionTypes.STRONG_LEAPING);
			list.add(PotionTypes.STRONG_POISON);
			list.add(PotionTypes.STRONG_REGENERATION);
			list.add(PotionTypes.STRONG_STRENGTH);
			list.add(PotionTypes.STRONG_SWIFTNESS);
			list.add(PotionTypes.SWIFTNESS);
			list.add(PotionTypes.THICK);
			list.add(PotionTypes.WATER);
			list.add(PotionTypes.WATER_BREATHING);
			list.add(PotionTypes.WEAKNESS);
			ItemStack potion = new ItemStack(Items.SPLASH_POTION);
			PotionType potiontype = list.get(ThreadLocalRandom.current().nextInt(0, list.size()));

			PotionUtils.addPotionToItemStack(potion, potiontype);
			entity.entityDropItem(potion, 1);
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
