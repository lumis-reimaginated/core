package com.lumi.reimagined.services

import com.lumi.reimagined.Reimagined
import com.lumi.reimagined.items.BaseCrystalHeartItem
import com.lumi.reimagined.registry.Attachments
import com.lumi.reimagined.services.PlayersService.PlayerData
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import java.util.function.Supplier

object PlayersService {

    val LOGGER = Reimagined.LOGGER;

    val PLAYER_DATA: Supplier<AttachmentType<PlayerData>> = Attachments.ATTACHMENT_TYPES.register(
        "player_data", Supplier {
            AttachmentType.builder(::PlayerData)
            .serialize(PlayerData.CODEC)
            .build()
        }
    )

    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity
        if (player.level().isClientSide) return
        val data = player.getData(PLAYER_DATA)
        updatePlayerAttributes(player, data)
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.entity
        if (player.level().isClientSide) return
        val data = player.getData(PLAYER_DATA)
        updatePlayerAttributes(player, data)
    }

    @SubscribeEvent
    fun onPlayerClone(event: PlayerEvent.Clone) {
        if (!event.isWasDeath) return

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

    fun updatePlayerAttributes(player: Player, data: PlayerData) {
        val inst = player.getAttribute(Attributes.MAX_HEALTH)
        if (inst != null) {

            val id = ResourceLocation.fromNamespaceAndPath("reimagined", "max_health");
            val newAmount = data.crystalHearts.countOneBits() * 5;

            inst.removeModifier(id);
            val mod = AttributeModifier(id, newAmount.toDouble(), AttributeModifier.Operation.ADD_VALUE);
            inst.addPermanentModifier(mod);

            player.heal(0f);
        }
    }


    class PlayerData(
        var crystalHearts: Int = 0
    ) {
        companion object {
            val CODEC: Codec<PlayerData> = RecordCodecBuilder.create { inst ->
                inst.group(Codec.INT.fieldOf("crystalHearts").forGetter { it.crystalHearts })
                    .apply(inst, ::PlayerData)
            }
        }
    }

}
