package com.docvin.vending_gear.entities.vending_machine;

import javax.annotation.Nullable;

import com.docvin.vending_gear.VendingGear;
import com.docvin.vending_gear.geckolib3.model.CustomAnimatedGeoModel;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class VendingGearTankModel extends CustomAnimatedGeoModel<VendingGearTankEntity> {
	ModelRenderer body;

	@Override
	public ResourceLocation getAnimationFileLocation(VendingGearTankEntity animatable) {
		return new ResourceLocation(VendingGear.MODID, "animations/vending_gear_tank.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(VendingGearTankEntity object) {
		return new ResourceLocation(VendingGear.MODID, "geo/vending_gear_tank.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(VendingGearTankEntity object) {
		return new ResourceLocation(VendingGear.MODID, "textures/entity/vending_gear_tank.png");
	}

	@Override
	public void setCustomAnimations(VendingGearTankEntity animatable, long instanceId,
			@Nullable AnimationEvent animationEvent) {

		GeoBone bone = (GeoBone) getAnimationProcessor().getBone("left_track");
		if (bone != null && animatable.isFalling) {
			bone.setRotationX((float) Math.toRadians(12));
		}
		GeoBone bone2 = (GeoBone) getAnimationProcessor().getBone("right_track");
		if (bone2 != null && animatable.isFalling) {
			bone2.setRotationX((float) Math.toRadians(12));
		}
	}
}
