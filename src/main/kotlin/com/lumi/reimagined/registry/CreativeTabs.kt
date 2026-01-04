package com.lumi.reimagined.registry


import com.lumi.reimagined.Reimagined.Companion.MOD_ID
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object CreativeTabs {
    val CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID)

    val MainTab = CREATIVE_MODE_TABS.register("items", Supplier { CreativeModeTab.builder()
        .title(Component.translatable("tab.reimagined.items"))
        .icon { ModItems.AMETHIST_HEART.get().defaultInstance}
        .displayItems { args, output ->
            // All mod items must be listed here!
            
            output.accept { ModItems.AMETHIST_HEART.get() }
            output.accept { ModItems.QUARTZ_HEART.get() }
            output.accept { ModItems.RUBI_HEART.get() }
            output.accept { ModItems.AQUAMARINE_HEART.get() }
            output.accept { ModItems.CREATIVE_BROKEN_HEART.get() }
        }
        .build()
    })
    
    fun register(modEventBus: IEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus)
    }
}
