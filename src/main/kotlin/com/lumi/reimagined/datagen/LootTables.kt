package com.lumi.reimagined.datagen

import com.lumi.reimagined.registry.ModBlocks
import com.lumi.reimagined.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class LootTables : BlockLootSubProvider {
    
    constructor(registries: HolderLookup.Provider)
            : super(
        setOf<Item>(),
        FeatureFlags.REGISTRY.allFlags(),
        registries) {}

    override fun getKnownBlocks(): Iterable<Block?> {
        return ModBlocks.BLOCKS.entries.map { it.get() }
    }
    
    override fun generate() {
//        dropSelf(ModBlocks.SILICON_BLOCK.block.get())
//        dropSelf(ModBlocks.LITOGRAPHY_MACHINE.block.get())
//        
//        add(ModBlocks.SILICON_BLOCK.block.get())
//            {_ -> createSingleItemTable(ModBlocks.SILICON_BLOCK.item.get()) }
//        
//        add(ModBlocks.LITOGRAPHY_MACHINE.block.get())
//            { _ -> createSingleItemTable(ModItems.LITOGRAPHY_MACHINE.get()) }
    }
    
}
