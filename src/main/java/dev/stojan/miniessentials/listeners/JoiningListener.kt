package dev.stojan.miniessentials.listeners

import dev.stojan.miniessentials.shared.CustomNamespacedKeys
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.persistence.PersistentDataType

class JoiningListener: Listener {
    @EventHandler
    fun playerJoinEvent(event: PlayerJoinEvent) {
        val player = event.player

        val playerStorageData = player.persistentDataContainer

        val hadFlying = playerStorageData.get(CustomNamespacedKeys.FlyState, PersistentDataType.BOOLEAN)

        if (hadFlying != null && hadFlying) {
            player.allowFlight = true
            player.isFlying = true
        }
    }
}