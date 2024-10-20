package com.docvin.vending_gear.registar;

import com.docvin.vending_gear.VendingGear;
import com.docvin.vending_gear.entities.vending_machine.VendingGearTankEntity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class VendingGearEntities {

	private static void registerEntity(String name, Class<? extends Entity> entityClass, int id, int range,
			int colorPrim, int colorSec) {
		EntityRegistry.registerModEntity(new ResourceLocation(VendingGear.MODID, name), entityClass, name, id,
				VendingGear.instance, range, 1, true, colorPrim, colorSec);
	}

	public static void init() {
		registerEntity("vending_machine", VendingGearTankEntity.class, 1, 256, 0x999999, 0x111111);
	}
}
