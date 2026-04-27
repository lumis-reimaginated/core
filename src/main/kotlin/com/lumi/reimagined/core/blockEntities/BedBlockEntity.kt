package com.lumi.reimagined.core.blockEntities

import com.lumi.reimagined.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class BedBlockEntity(pos: BlockPos, state: BlockState)
    : BlockEntity(ModBlocks.OAK_BED.entity.get(), pos, state) {

}
