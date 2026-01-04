package com.lumi.reimagined.datagen

import com.lumi.reimagined.Reimagined
import com.lumi.reimagined.registry.ModBlocks
import com.lumi.reimagined.registry.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem

class ItemModels : ItemModelProvider {
    
    constructor(out: PackOutput, ex: ExistingFileHelper)
        : super(out, Reimagined.MOD_ID, ex)
    {}

    override fun registerModels() {
        
        
        basicItem(ModItems.AMETHIST_HEART)
        basicItem(ModItems.QUARTZ_HEART)
        basicItem(ModItems.RUBI_HEART)
        basicItem(ModItems.AQUAMARINE_HEART)
        basicItem(ModItems.CREATIVE_BROKEN_HEART)
        
    }
    
    
    fun basicItem(deferredItem: DeferredItem<*>) {
        basicItem(deferredItem.get());
    
    }fun modelItem(deferredItem: DeferredItem<*>, res: ResourceLocation) {
        withExistingParent(deferredItem.get().toString(), res);
    }
    fun basicBlock(basicBlock: ModBlocks.BasicBlock<*>) {
        simpleBlockItem(basicBlock.block.get());
    }
    
}
