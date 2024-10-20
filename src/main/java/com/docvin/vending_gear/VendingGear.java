package com.docvin.vending_gear;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creativemd.creativecore.common.config.event.ConfigEventHandler;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.docvin.vending_gear.capabilities.CapabilityHandler;
import com.docvin.vending_gear.capabilities.potion.IPotionsDrank;
import com.docvin.vending_gear.capabilities.potion.PotionsDrank;
import com.docvin.vending_gear.capabilities.potion.PotionsDrankStorage;
import com.docvin.vending_gear.entities.vending_machine.VendingGearTankEntity;
import com.docvin.vending_gear.entities.vending_machine.VendingGearTankRender;
import com.docvin.vending_gear.events.VendingGearEvents;
import com.docvin.vending_gear.packets.server.DrankEnoughPotionPacket;
import com.docvin.vending_gear.packets.server.JetBoostPacket;
import com.docvin.vending_gear.packets.server.SparkPacket;
import com.docvin.vending_gear.packets.server.StartAttackAnimationPacket;
import com.docvin.vending_gear.packets.server.StartJumpAnimationPacket;
import com.docvin.vending_gear.registar.VendingGearEntities;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import software.bernie.geckolib3.GeckoLib;

@Mod(modid = VendingGear.MODID, name = VendingGear.NAME, version = VendingGear.VERSION)
public class VendingGear {
	public static final String MODID = "vending_gear";
	public static final String NAME = "Vending Gear";
	public static final String VERSION = "0.0.1";

	private static final Logger LOGGER = LogManager.getLogger(VendingGear.MODID);
	public static ConfigEventHandler configHandler;
	public static VendingGearConfigs config;

	@Instance(VendingGear.MODID)
	public static VendingGear instance = new VendingGear();

	public VendingGear() {
		GeckoLib.initialize();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		VendingGearEntities.init();
		RenderingRegistry.registerEntityRenderingHandler(VendingGearTankEntity.class, VendingGearTankRender::new);

		event.getModMetadata().version = VERSION;
		config = new VendingGearConfigs();
		configHandler = new ConfigEventHandler(event.getModConfigurationDirectory(), LOGGER);

		CreativeConfigRegistry.ROOT.registerValue(MODID, config);
		CreativeConfigRegistry.load(MODID, Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
		MinecraftForge.EVENT_BUS.register(VendingGearEvents.class);

		// Make sure the Capability is registered after config is set.
		CapabilityManager.INSTANCE.register(IPotionsDrank.class, new PotionsDrankStorage(), PotionsDrank::new);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		CreativeCorePacket.registerPacket(DrankEnoughPotionPacket.class);
		CreativeCorePacket.registerPacket(SparkPacket.class);
		CreativeCorePacket.registerPacket(JetBoostPacket.class);
		CreativeCorePacket.registerPacket(StartJumpAnimationPacket.class);
		CreativeCorePacket.registerPacket(StartAttackAnimationPacket.class);

		// logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}
}
