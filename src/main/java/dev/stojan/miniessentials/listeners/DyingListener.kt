package dev.stojan.miniessentials.listeners

import dev.stojan.miniessentials.shared.ChatColors
import dev.stojan.miniessentials.shared.Coordinates
import dev.stojan.miniessentials.shared.CustomNamespacedKeys
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.persistence.PersistentDataType

class DyingListener : Listener {
    @EventHandler
    fun playerDyingEvent(event: PlayerDeathEvent) {
        val player = event.player;

        val playerStorageData = player.persistentDataContainer;

        playerStorageData.set(
                CustomNamespacedKeys.BackCoords,
                PersistentDataType.STRING,
                Coordinates(player.x, player.y, player.z).toString()
        );

        playerStorageData.set(
                CustomNamespacedKeys.BackWorldId,
                PersistentDataType.STRING,
                player.world.uid.toString()
        )

        val deathMessage = Component.text("Death location: ").color(ChatColors.Yellow)
                .append(Component.text("X:" + String.format("%.3f", player.x)).color(ChatColors.Red))
                .append(Component.text(" Y:" + String.format("%.3f", player.y)).color(ChatColors.Red))
                .append(Component.text(" Z:" + String.format("%.3f", player.z)).color(ChatColors.Red))
                .append(Component.text(".").color(ChatColors.Yellow))

        player.sendMessage(deathMessage);
    }
}