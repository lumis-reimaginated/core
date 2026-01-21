package com.lumi.reimagined.blocks

import com.lumi.reimagined.blockEntities.BedBlockEntity
import com.lumi.reimagined.registry.ModBlocks
import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

open class BedBase(props: Properties) : BaseEntityBlock(props.noOcclusion()) {

    companion object {
        val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
        val CODEC: MapCodec<BedBase> = simpleCodec(::BedBase)
    }

    private val NORTH_SHAPE: VoxelShape = Shapes.or(box(0.0, 0.0, -16.0, 16.0, 8.0, 16.0))
    private val SOUTH_SHAPE: VoxelShape = Shapes.or(box(0.0, 0.0, 0.0, 16.0, 8.0, 32.0))
    private val EAST_SHAPE: VoxelShape = Shapes.or(box(0.0, 0.0, 0.0, 32.0, 8.0, 16.0))
    private val WEAST_SHAPE: VoxelShape = Shapes.or(box(-16.0, 0.0, 0.0, 16.0, 8.0, 16.0))
    private fun getShape(facing: Direction): VoxelShape {
        return when (facing) {
            Direction.NORTH -> NORTH_SHAPE
            Direction.SOUTH -> SOUTH_SHAPE
            Direction.WEST  -> WEAST_SHAPE
            Direction.EAST  -> EAST_SHAPE
            else -> NORTH_SHAPE
        }
    }

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH))
    }

    override fun codec(): MapCodec<out BedBase?> = CODEC
    override fun getRenderShape(state: BlockState?): RenderShape = RenderShape.MODEL
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = BedBlockEntity(pos, state)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val direction = context.horizontalDirection.opposite

        val blockpos = context.clickedPos
        val blockpos1 = blockpos.relative(direction)

        val level = context.level
        if (level.getBlockState(blockpos1).canBeReplaced(context) && level.worldBorder.isWithinBounds(blockpos1))
            return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction)
        return null
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)

        val facing = state.getValue(FACING)
        val extensionPos = pos.relative(facing)
        level.setBlock(extensionPos, ModBlocks.EXTENSION_BLOCK.get().defaultBlockState()
                .setValue(ExtensionBlock.FACING, facing.opposite), UPDATE_ALL)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (!level.isClientSide && state !== newState) {
            val facing = state.getValue(FACING)
            val extensionPos = pos.relative(facing)

            val extensionState = level.getBlockState(extensionPos) ?: return
            val extensionFacing = extensionState.getValue(ExtensionBlock.FACING) ?: return
            val rootPos = extensionPos.relative(extensionFacing)

            if (rootPos == pos) {
                level.removeBlock(extensionPos, false)
                level.removeBlockEntity(extensionPos)
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun getCollisionShape(state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = getShape(state.getValue(FACING))
    override fun getShape(state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape  = getShape(state.getValue(FACING))
    override fun useShapeForLightOcclusion(state: BlockState): Boolean = true
}
