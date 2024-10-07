package com.docvin.vending_gear.registar;

import java.lang.reflect.InvocationTargetException;

import com.docvin.vending_gear.entities.vending_machine.VendingGearTankEntity;
import com.docvin.vending_gear.entities.vending_machine.VendingGearTankRender;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class VendingGearRenders {

	private static <T extends Entity, K extends Render<T>> void registerEntityRender(Class<T> entityClass,
			Class<K> rendererClass) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<T>() {
			@Override
			public Render<? super T> createRenderFor(RenderManager manager) {
				try {
					return rendererClass.getConstructor(RenderManager.class).newInstance(manager);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				return null;
			}
		});

	}

	public static void init() {
		registerEntityRender(VendingGearTankEntity.class, VendingGearTankRender.class);
	}
}
