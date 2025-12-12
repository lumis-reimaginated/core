package com.lumi.reimagined

import com.lumi.reimagined.registry.ModBlocks
import com.lumi.reimagined.registry.CreativeTabs
import com.lumi.reimagined.registry.ModItems
import com.mojang.logging.LogUtils
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.server.ServerStartingEvent
import org.slf4j.Logger

@Mod(Reimagined.MOD_ID)
class Reimagined(modEventBus: IEventBus, modContainer: ModContainer) {
    
    companion object {
        const val MOD_ID: String = "reimagined"
        val LOGGER: Logger = LogUtils.getLogger()
    }
    
    init {
        modEventBus.addListener(::onCommonSettup);
        modEventBus.addListener(::onClientSettup);

        // Register defferred registers
        ModBlocks.register(modEventBus)
        ModItems.register(modEventBus)
        CreativeTabs.register(modEventBus)
        
        // Register ourselves for listening to events
        NeoForge.EVENT_BUS.register(this)


        // Register mod's configuration specifications
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC)
    }


    fun onCommonSettup(event: FMLCommonSetupEvent?) {
        LOGGER.info("HELLO from common")
    }
    
    fun onClientSettup(event: FMLClientSetupEvent?) {
        LOGGER.info("HELLO from client")
    }
    
    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent?) {
        LOGGER.info("HELLO from server")
    }
}
