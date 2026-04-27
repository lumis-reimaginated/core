package com.lumi.reimagined.core.datagen

import com.lumi.reimagined.registry.ModItems
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder
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
            RecipeCategory.FOOD,
            ModItems.AMETHIST_HEART.get())
            
            .define('A', Items.AMETHYST_SHARD)
            .define('D', Items.DIAMOND)

            .pattern("A A")
            .pattern("ADA")
            .pattern(" A ")
            
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(output);

        ShapedRecipeBuilder.shaped(
            RecipeCategory.FOOD,
            ModItems.QUARTZ_HEART.get())

            .define('Q', Items.QUARTZ)
            .define('B', Items.BLAZE_POWDER)

            .pattern("Q Q")
            .pattern("QBQ")
            .pattern(" Q ")

            .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
            .save(output);

        ShapedRecipeBuilder.shaped(
            RecipeCategory.FOOD,
            ModItems.PRISMARINE_HEART.get())

            .define('P', Items.PRISMARINE_SHARD)
            .define('C', Items.HEART_OF_THE_SEA)

            .pattern("P P")
            .pattern("PCP")
            .pattern(" P ")

            .unlockedBy("has_heart_of_the_sea", has(Items.HEART_OF_THE_SEA))
            .save(output);

        MechanicalCraftingRecipeBuilder.shapedRecipe(Items.NETHERITE_HOE)

            .key('P', Items.PUMPKIN)
            .key('C', Items.COPPER_BLOCK)

            .patternLine(" P ")
            .patternLine("CCC")
            .patternLine(" C ")

            .build(output)
    }
}
