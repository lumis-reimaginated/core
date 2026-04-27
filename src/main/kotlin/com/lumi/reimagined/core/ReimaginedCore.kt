package com.lumi.reimagined.core

import com.lumi.reimagined.registry.Attachments
import com.lumi.reimagined.registry.CreativeTabs
import com.lumi.reimagined.registry.ModBlocks
import com.lumi.reimagined.registry.ModItems
import com.lumi.reimagined.services.EventsService
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
class ReimaginedCore(modEventBus: IEventBus, modContainer: ModContainer) {
    
    companion object {
        const val MOD_ID: String = "reimagined_core"
        val LOGGER: Logger = LogUtils.getLogger()
    }
    
    init {
        modEventBus.addListener(::onCommonSettup);
        modEventBus.addListener(::onClientSettup);

        // Register deferred registers
        ModBlocks.register(modEventBus)
        ModItems.register(modEventBus)
        Attachments.register(modEventBus)
        CreativeTabs.register(modEventBus)

        // Register event listeners
        NeoForge.EVENT_BUS.register(this)
        NeoForge.EVENT_BUS.register(EventsService)

        // Register mod's configuration specifications
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC)
    }


    fun onCommonSettup(event: FMLCommonSetupEvent?) {
        //LOGGER.info("HELLO from common")
    }
    
    fun onClientSettup(event: FMLClientSetupEvent?) {
        //LOGGER.info("HELLO from client")
    }
    
    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent?) {
        LOGGER.info("HELLO from server")
    }
}
