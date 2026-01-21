package com.lumi.reimagined.datagen

import com.lumi.reimagined.registry.ModBlocks
import com.lumi.reimagined.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

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
        add(ModBlocks.EXTENSION_BLOCK.get()) {_ -> LootTable.lootTable() }

        dropSelf(ModBlocks.OAK_BED.block.get())
        add(ModBlocks.OAK_BED.block.get()) {_ -> createSingleItemTable(ModItems.OAK_BED.get()) }
    }
    
}
