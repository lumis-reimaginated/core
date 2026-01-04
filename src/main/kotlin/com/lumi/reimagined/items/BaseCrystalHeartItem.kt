package com.lumi.reimagined.items

import com.lumi.reimagined.Reimagined
import com.lumi.reimagined.services.PlayersService
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

abstract class BaseCrystalHeartItem() : Item(Properties()) {

    abstract val type: CrystalHeartItemType

    override fun getMaxStackSize(stack: ItemStack): Int = 1
    override fun getDefaultMaxStackSize(): Int = 1

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT
    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32


    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack?> {

        val stack = player.getItemInHand(hand)

        if (!level.isClientSide) {
            val canEat = PlayersService.canEatCrystalHeart(player, type)
            if (!canEat) return InteractionResultHolder.fail(stack)
            player.startUsingItem(hand)
        }

        return InteractionResultHolder.fail(stack)
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        if (!level.isClientSide && entity is Player) PlayersService.ateCrystalHeart(entity, type)
        stack.shrink(1);
        return stack;
    }

    enum class CrystalHeartItemType {
        AMETHYST    { override fun asFlag(): Int = 1 shl 0 },
        QUARTZ      { override fun asFlag(): Int = 1 shl 1 },
        RUBI        { override fun asFlag(): Int = 1 shl 2 },
        AQUAMARINE  { override fun asFlag(): Int = 1 shl 3 },

        UNDEFINED,
        BROKEN;

        open fun asFlag(): Int = 0
    }
}
