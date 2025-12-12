package com.lumi.reimagined.datagen

import com.lumi.reimagined.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.conditions.IConditionBuilder
import java.util.concurrent.CompletableFuture

class Recipes : RecipeProvider, IConditionBuilder {

    constructor(output: PackOutput, future: CompletableFuture<HolderLookup.Provider>)
            : super(output, future) {}


    override fun buildRecipes(output: RecipeOutput) {

        ShapedRecipeBuilder.shaped(
            RecipeCategory.BUILDING_BLOCKS,
            ModItems.AMETHIST_HEART.get())
            
            .define('A', Items.AMETHYST_SHARD)
            
            .pattern("A A")
            .pattern("AAA")
            .pattern(" A ")
            
            .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
            .save(output);
        
    }
}
