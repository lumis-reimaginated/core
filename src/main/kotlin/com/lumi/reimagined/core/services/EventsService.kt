package com.lumi.reimagined.core.services

import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent
import net.neoforged.neoforge.event.entity.living.LivingEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent

object EventsService {

    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        if (event.isServerSide()) {
            // Server-only logic
            PlayersService.onLogin(event.entity, event)
        } else {
            // Client-only logic
        }
    }

    @SubscribeEvent
    fun onPlayerLogout(event: PlayerEvent.PlayerLoggedOutEvent) {
        if (event.isServerSide()) {
            // Server-only logic
            PlayersService.onLogout(event.entity, event)
        } else {
            // Client-only logic
        }
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        if (event.isServerSide()) {
            // Server-only logic
            PlayersService.onRespawn(event.entity, event);
        } else {
            // Client-only logic
        }
    }

    @SubscribeEvent
    fun onPlayerClone(event: PlayerEvent.Clone) {
        if (event.isServerSide()) {
            // Server-only logic
            PlayersService.onClone(event.original, event.entity, event);
        } else {
            // Client-only logic
        }
    }

    @SubscribeEvent
    fun onPlayerDeath(event: LivingDeathEvent) {
        if (event.isServerSide()) {
            // Server-only logic
            when (val entity = event.entity) {
                is Player -> PlayersService.onDeath(entity, event)
            }
        } else {
            // Client-only logic
        }
    }
    
    @SubscribeEvent
    fun onEntityDropItems(event: LivingDropsEvent) {
        if (event.isServerSide()) {
            // Server-only logic
        } else {
            // Client-only logic
        }
    }


    fun Event.isServerSide(): Boolean = when (this) {
        is LivingEvent -> !this.entity.level().isClientSide
        else -> error("Event type '${this.javaClass}' not implemented")
    }
}
