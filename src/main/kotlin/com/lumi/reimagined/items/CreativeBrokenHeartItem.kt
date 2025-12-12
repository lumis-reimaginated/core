package com.lumi.reimagined.items

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

class CreativeBrokenHeartItem(props: Properties): Item(props) {

    override fun getMaxStackSize(stack: ItemStack): Int = 1;
    override fun getDefaultMaxStackSize(): Int = 1;
    
    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT;
    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32;


    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack?> {
        player.startUsingItem(hand);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        
        // TODO it should clear all the crystal hearts effect
        
        stack.shrink(1);
        return stack;
    }
    
}
