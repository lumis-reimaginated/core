package com.lumi.reimagined.registry

import com.lumi.reimagined.Reimagined
import com.lumi.reimagined.services.PlayersService.PlayerData
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier


object Attachments {

    val ATTACHMENT_TYPES: DeferredRegister<AttachmentType<*>?> = DeferredRegister
        .create(NeoForgeRegistries.ATTACHMENT_TYPES, Reimagined.MOD_ID)

    val PLAYER_DATA: Supplier<AttachmentType<PlayerData>> = ATTACHMENT_TYPES.register(
        "player_data", Supplier {
            AttachmentType.builder(::PlayerData)
                .serialize(PlayerData.CODEC)
                .build()
        }
    )

    fun register(eventBus: IEventBus) {
        ATTACHMENT_TYPES.register(eventBus)
    }
}