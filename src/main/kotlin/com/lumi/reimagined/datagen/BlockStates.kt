package com.lumi.reimagined.datagen

import com.lumi.reimagined.Reimagined
import com.lumi.reimagined.registry.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper

class BlockStates : BlockStateProvider {

    constructor(out: PackOutput, ex: ExistingFileHelper)
        : super(out, Reimagined.MOD_ID,ex)
    {
        
    }
    
    override fun registerStatesAndModels() {

        getVariantBuilder(ModBlocks.OAK_BED.block.get())
            .forAllStates { state ->
            val dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING)

            val rotY = when (dir) {
                Direction.SOUTH -> 0
                Direction.WEST  -> 90
                Direction.NORTH -> 180
                Direction.EAST  -> 270
                else -> 0
            }

            ConfiguredModel.builder()
                .modelFile(models().getExistingFile(modLoc("block/oak_bed")))
                .rotationY(rotY)
                .build()
        }
    }
    
    
    private fun blockWithItem(basicBlock: ModBlocks.BasicBlock<*>) {
        simpleBlockWithItem(basicBlock.block.get(), cubeAll(basicBlock.block.get()));
    }
}
