package com.docvin.vending_gear.entities.vending_machine;

import com.docvin.vending_gear.VendingGear;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class VendingGearTankRender extends RenderLiving<VendingGearTankEntity> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(VendingGear.MODID,
			"textures/entity/vending_machine.png");

	public VendingGearTankRender(RenderManager rendermanager) {
		super(rendermanager, new VendingGearTankModel(), 0.05F);
	}

	@Override
	protected ResourceLocation getEntityTexture(VendingGearTankEntity entity) {
		return TEXTURES;
	}

	@Override
	protected void applyRotations(VendingGearTankEntity entityLiving, float p_77043_2_, float rotationYaw,
			float partialTicks) {
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
