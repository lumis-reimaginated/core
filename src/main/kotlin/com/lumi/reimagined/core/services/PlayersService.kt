package com.lumi.reimagined.core.services

import com.lumi.reimagined.Reimagined
import com.lumi.reimagined.items.BaseCrystalHeartItem
import com.lumi.reimagined.registry.Attachments
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent


object PlayersService {

    val LOGGER = Reimagined.LOGGER;
    val PLAYER_DATA = Attachments.PLAYER_DATA;

    fun onLogin(player: Player, event: PlayerEvent.PlayerLoggedInEvent) {
        val data = player.getData(PLAYER_DATA)
        updatePlayerAttributes(player, data)
    }
    fun onLogout(player: Player, event: PlayerEvent.PlayerLoggedOutEvent) {

    }
    fun onDeath(player: Player, event: LivingDeathEvent) {
        if (keepInventory(player)) return
        player.getData(PLAYER_DATA).deathInventory = filterInventoryOnDeath(player)
    }
    fun onRespawn(player: Player, event: PlayerEvent.PlayerRespawnEvent) {
        val data = player.getData(PLAYER_DATA)
        updatePlayerAttributes(player, data)

        if (!keepInventory(player)) loadInventoryOnRespawn(player, data)
    }
    fun onClone(oldPlayer: Player, newPlayer: Player, event: PlayerEvent.Clone) {
        val oldData = event.original.getData(PLAYER_DATA)
        val newData = event.entity.setData(PLAYER_DATA, oldData)
    }

    fun canEatCrystalHeart(player: Player, heart: BaseCrystalHeartItem.CrystalHeartItemType) : Boolean {
        val playerData = player.getData(PLAYER_DATA)
        return playerData!!.crystalHearts and heart.asFlag() == 0
    }
    fun ateCrystalHeart(player: Player, heart: BaseCrystalHeartItem.CrystalHeartItemType) {
        if (heart == BaseCrystalHeartItem.CrystalHeartItemType.UNDEFINED) return

        val playerData = player.getData(PLAYER_DATA)
        if (heart == BaseCrystalHeartItem.CrystalHeartItemType.BROKEN) playerData.crystalHearts = 0
        else playerData.crystalHearts = playerData.crystalHearts or heart.asFlag()

        updatePlayerAttributes(player, playerData)
    }

    fun filterInventoryOnDeath(player: Player): ArrayList<ItemStack> {
        val inv = player.inventory
        val result = ArrayList<ItemStack>(inv.containerSize)
        repeat(inv.containerSize) { result.add(ItemStack.EMPTY) }

        // Inventory
        var off = 0
        for (slot in 0 until inv.items.size) {
            val stack = inv.items[slot]
            if (!itemDropsOnDeath(stack)) {
                result[slot + off] = stack.copy()
                inv.items[slot] = ItemStack.EMPTY
            }
        }

        // Armor
        off += inv.items.size
        for (slot in 0 until inv.armor.size) {
            val stack = inv.armor[slot]
            if (!itemDropsOnDeath(stack)) {
                result[slot + off] = stack.copy()
                inv.armor[slot] = ItemStack.EMPTY
            }
        }

        // Offhand
        off += inv.armor.size
        for (slot in 0 until inv.offhand.size) {
            val stack = inv.offhand[slot]
            if (!itemDropsOnDeath(stack)) {
                result[slot + off] = stack.copy()
                inv.offhand[slot] = ItemStack.EMPTY
            }
        }

        return result
    }
    fun loadInventoryOnRespawn(player: Player, data: PlayerData) {
        val oldInv = data.deathInventory!!
        val newInv = player.inventory

        var off = 0
        for (slot in 0 until newInv.items.size) newInv.items[slot] = oldInv[slot]

        // Armor
        off += newInv.items.size
        for (slot in 0 until newInv.armor.size) newInv.armor[slot] = oldInv[slot + off]

        // Offhand
        off += newInv.armor.size
        for (slot in 0 until newInv.offhand.size) newInv.offhand[slot] = oldInv[slot + off]
    }
    fun updatePlayerAttributes(player: Player, data: PlayerData) {
        val inst = player.getAttribute(Attributes.MAX_HEALTH)
        if (inst != null) {

            val id = ResourceLocation.fromNamespaceAndPath("reimagined", "max_health");
            val newAmount = -10 + data.crystalHearts.countOneBits().toDouble() * 7.5;

            inst.removeModifier(id);
            val mod = AttributeModifier(id, newAmount, AttributeModifier.Operation.ADD_VALUE);
            inst.addPermanentModifier(mod);

            player.heal(0f);
        }
    }

    fun itemDropsOnDeath(stack: ItemStack): Boolean {
        return stack.item == Items.GOLDEN_CARROT
    }

    fun keepInventory(player: Player): Boolean = player.level().gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)

    class PlayerData(
        var deathInventory: List<ItemStack>? = null,
        var crystalHearts: Int = 0,
    ) {
        companion object {
            val CODEC: Codec<PlayerData> = RecordCodecBuilder.create { inst ->
                inst.group(

                    Codec.list(ItemStack.CODEC)
                        .optionalFieldOf("deathInventory", emptyList())
                        .forGetter { it.deathInventory },

                    Codec.INT.fieldOf("crystalHearts")
                        .forGetter { it.crystalHearts }

                ).apply(inst, ::PlayerData)
            }
        }
    }
}
