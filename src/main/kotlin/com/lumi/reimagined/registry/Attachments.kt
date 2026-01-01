package com.lumi.reimagined.registry

import com.lumi.reimagined.Reimagined
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries


object Attachments {

    val ATTACHMENT_TYPES: DeferredRegister<AttachmentType<*>?> = DeferredRegister
        .create(NeoForgeRegistries.ATTACHMENT_TYPES, Reimagined.MOD_ID)

    fun register(eventBus: IEventBus) {
        ATTACHMENT_TYPES.register(eventBus)
    }
}