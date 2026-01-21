package com.lumi.reimagined.registry

import com.lumi.reimagined.Reimagined.Companion.MOD_ID
import com.lumi.reimagined.blockEntities.BedBlockEntity
import com.lumi.reimagined.blocks.ExtensionBlock
import com.lumi.reimagined.blocks.OakBed
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(MOD_ID)
    val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID)

    val EXTENSION_BLOCK = registerRawBlock("extension_block") { ExtensionBlock() }

    val OAK_BED = registerBlockWithEntity("oak_bed", ::BedBlockEntity)
        { OakBed(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.of()
            .strength(0.2f)
            .sound(SoundType.WOOD)) }


    fun <T1: Block> registerRawBlock(
        name: String,
        block: Supplier<T1>
    ): DeferredBlock<T1> {
        val block = BLOCKS.register(name, block)
        return block;
    }

    fun <T1: Block> registerBasicBlock(
        name: String,
        block: Supplier<T1>
    ): BasicBlock<T1> {
        val block = BLOCKS.register(name, block)
        val item = registerBlockItem(name, block)
        return BasicBlock(block, item);
    }

    fun <T1 : Block, T2 : BlockEntity> registerBlockWithEntity(
        name: String,
        entityFactory: BlockEntityType.BlockEntitySupplier<T2>,
        block: Supplier<T1>
    ): BlockWithEntity<T1, T2> {

        val regBlock = BLOCKS.register(name, block)
        val regEntity = BLOCK_ENTITIES.register("${name}_be", Supplier {
            BlockEntityType.Builder.of(entityFactory, regBlock.get())
                .build(null)
        })

        return BlockWithEntity<T1, T2>(regBlock, regEntity)
    }

    fun <T: Block> registerBlockItem(name: String, block: DeferredBlock<T>): DeferredItem<BlockItem> {
        return ModItems.ITEMS.register(name, Supplier { BlockItem(block.get(), Item.Properties()) })
    }

    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
        BLOCK_ENTITIES.register(eventBus)
    }

    data class BasicBlock<T: Block>(
        val block: DeferredBlock<T>,
        val item: DeferredItem<BlockItem>)
    data class BlockWithEntity<T1: Block, T2: BlockEntity>(
        val block: DeferredBlock<T1>,
        val entity: DeferredHolder<BlockEntityType<*>, BlockEntityType<T2>>
    )
}
