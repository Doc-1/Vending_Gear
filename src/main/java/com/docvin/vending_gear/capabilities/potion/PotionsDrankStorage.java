package com.docvin.vending_gear.capabilities.potion;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/**
 * Used for saving and reading the data stored in IPotionsDrank
 */
public class PotionsDrankStorage implements IStorage<IPotionsDrank> {

	@Override
	public NBTBase writeNBT(Capability<IPotionsDrank> capability, IPotionsDrank instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("potions_drank", instance.totalDrank());
		nbt.setInteger("drinks_needed", instance.drinksNeeded());
		return nbt;
	}

	@Override
	public void readNBT(Capability<IPotionsDrank> capability, IPotionsDrank instance, EnumFacing side, NBTBase nbt) {
		instance.setDrank(((NBTTagCompound) nbt).getInteger("potions_drank"));
		instance.setDrinksNeeded(((NBTTagCompound) nbt).getInteger("drinks_needed"));
	}

}
