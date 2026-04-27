package com.lumi.reimagined.core.registry

import com.lumi.reimagined.Reimagined.Companion.MOD_ID
import com.lumi.reimagined.items.*
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItems {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(MOD_ID)

    val AMETHIST_HEART = ITEMS.register("amethyst_heart", Supplier { AmethystHeartItem() })
    val QUARTZ_HEART = ITEMS.register("quartz_heart", Supplier { QuartzHeartItem() })
    val RUBI_HEART = ITEMS.register("rubi_heart", Supplier { RubiHeartItem() })
    val PRISMARINE_HEART = ITEMS.register("prismarine_heart", Supplier { PrismarineHeart() })
    val CREATIVE_BROKEN_HEART = ITEMS.register("creative_broken_heart", Supplier { CreativeBrokenHeartItem() })

    fun register(modEventBus: IEventBus) {
        ITEMS.register(modEventBus)
    }
}
