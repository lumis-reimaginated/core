package com.lumi.reimagined.core.datagen

import com.lumi.reimagined.Reimagined
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = Reimagined.MOD_ID)
class DataGenerators {

    companion object {
        @SubscribeEvent
        @JvmStatic
        fun gatherData(event: GatherDataEvent) {

            val generator = event.generator;
            val packOutput = generator.packOutput;
            val existingFileHelper = event.existingFileHelper;
            val lookupProvider = event.lookupProvider;

            generator.addProvider(
                event.includeServer(),
                LootTableProvider(
                    packOutput,
                    setOf(),
                    listOf(
                        LootTableProvider.SubProviderEntry(::LootTables, LootContextParamSets.BLOCK),
                    ),
                    lookupProvider
                )
            )
            generator.addProvider(event.includeServer(), Recipes(packOutput, lookupProvider))
            
            generator.addProvider(event.includeClient(), BlockStates(packOutput, existingFileHelper))
            generator.addProvider(event.includeClient(), ItemModels(packOutput, existingFileHelper))
            
        }
    }
    
}
