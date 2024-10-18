package com.docvin.vending_gear.entities.vending_machine;

import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class VendingGearTankRender extends GeoEntityRenderer<VendingGearTankEntity> {

	public VendingGearTankRender(RenderManager renderManager) {
		super(renderManager, new VendingGearTankModel());
		this.shadowSize = 0.5f;
	}

}
