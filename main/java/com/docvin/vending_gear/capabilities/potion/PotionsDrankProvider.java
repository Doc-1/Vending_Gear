package com.docvin.vending_gear.capabilities.potion;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * This will retrieve the player's specific PotionsDrank Capability. As well as
 * update the data.
 */
public class PotionsDrankProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IPotionsDrank.class)
	public final static Capability<IPotionsDrank> POTIONS_DRANK = null;

	private IPotionsDrank instance = POTIONS_DRANK.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return POTIONS_DRANK == capability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == POTIONS_DRANK ? POTIONS_DRANK.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return POTIONS_DRANK.getStorage().writeNBT(POTIONS_DRANK, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		POTIONS_DRANK.getStorage().readNBT(POTIONS_DRANK, this.instance, null, nbt);
	}

}
