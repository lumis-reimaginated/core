package com.lumi.reimagined.blocks

import com.lumi.reimagined.Reimagined
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class ExtensionBlock : Block(Properties.of().noOcclusion()) {

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
    }

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.INVISIBLE

    fun getRootPos(state: BlockState, pos: BlockPos): BlockPos = pos.relative(state.getValue(FACING));

    override fun playerWillDestroy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        player: Player
    ): BlockState {
        val rootPos = getRootPos(state, pos)
        val rootState = level.getBlockState(rootPos)
        rootState.block.playerWillDestroy(level, rootPos, rootState, player)
        return state
    }

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean,
        fluid: FluidState
    ): Boolean {
        val rootPos = getRootPos(state, pos)
        val rootState = level.getBlockState(rootPos)

        val result = rootState.block.onDestroyedByPlayer(rootState, level, rootPos, player, willHarvest, fluid)
        if (result) rootState.block.playerDestroy(level, player, rootPos, rootState, null, player.mainHandItem)

        return false
    }

    override fun canHarvestBlock(state: BlockState, level: BlockGetter, pos: BlockPos, player: Player): Boolean {
        val rootPos = getRootPos(state, pos)
        return level.getBlockState(rootPos).block.canHarvestBlock(level.getBlockState(rootPos), level, rootPos, player)
    }

    override fun getDestroyProgress(state: BlockState, player: Player, level: BlockGetter, pos: BlockPos): Float {
        val rootPos = getRootPos(state, pos)
        return level.getBlockState(rootPos).getDestroyProgress(player, level, rootPos);
    }

    override fun attack(state: BlockState, level: Level, pos: BlockPos, player: Player) {
        if (level.isClientSide) return

        val facing = state.getValue(FACING)
        val rootPos = pos.relative(facing)

        val rootState = level.getBlockState(rootPos)
        Reimagined.LOGGER.debug(rootPos.toString() + " " + rootState.block.name)

        if (rootState == null) {
            level.removeBlock(pos, false)
            return
        }

        rootState.attack(level, rootPos, player)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape? {
        val facing = state.getValue(FACING)
        val rootPos = pos.relative(facing)
        val rootState = level.getBlockState(rootPos)

        val rootShape = rootState.getShape(level, rootPos, context)
        if (rootShape.isEmpty) return Shapes.empty()

        val dx = rootPos.x - pos.x
        val dy = rootPos.y - pos.y
        val dz = rootPos.z - pos.z

        return rootShape.move(dx.toDouble(), dy.toDouble(), dz.toDouble())
    }
}
