package dev.stojan.miniessentials.commands

import dev.stojan.miniessentials.shared.ChatColors
import dev.stojan.miniessentials.shared.Coordinates
import dev.stojan.miniessentials.shared.CustomNamespacedKeys
import dev.stojan.miniessentials.shared.Messages
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.*


class HomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        val playerStorageData = sender.persistentDataContainer;

        val homeCoordsAsString = playerStorageData.get(CustomNamespacedKeys.HomeCoords, PersistentDataType.STRING)
        val homeWorldIdAsString = playerStorageData.get(CustomNamespacedKeys.HomeWorldId, PersistentDataType.STRING);

        if (homeCoordsAsString == null || homeWorldIdAsString == null) {
            sender.sendMessage(Component.text("You don't have a home.").color(ChatColors.Yellow))
            return true;
        }

        println(homeCoordsAsString)
        println(homeWorldIdAsString)

        val homeCoords = Coordinates.parse(homeCoordsAsString);

        val homeWorld = Bukkit.getWorld(UUID.fromString(homeWorldIdAsString))

        playerStorageData.set(
                CustomNamespacedKeys.BackCoords,
                PersistentDataType.STRING,
                Coordinates(sender.x, sender.y, sender.z).toString()
        )
        playerStorageData.set(
                CustomNamespacedKeys.BackWorldId,
                PersistentDataType.STRING,
                sender.world.uid.toString()
        )

        sender.teleport(Location(homeWorld, homeCoords.x, homeCoords.y, homeCoords.z, sender.yaw, sender.pitch))

        sender.sendMessage(Component.text("Teleporting...").color(ChatColors.Yellow))

        return true;
    }
}

class SetHomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        val playerStorageData = sender.persistentDataContainer;

        playerStorageData.set(
                CustomNamespacedKeys.HomeCoords,
                PersistentDataType.STRING,
                Coordinates(sender.x, sender.y, sender.z).toString()
        );

        playerStorageData.set(
                CustomNamespacedKeys.HomeWorldId,
                PersistentDataType.STRING,
                sender.world.uid.toString()
        )

        sender.sendMessage(
                Component.text("Home is set. Use ").color(ChatColors.Yellow)
                        .append(
                                Component.text("/home")
                                        .color(ChatColors.Red)
                                        .clickEvent(ClickEvent.runCommand("/home"))
                                        .hoverEvent(
                                                HoverEvent.showText(
                                                        Component.text("Teleport to home.")
                                                                .color(ChatColors.Yellow)
                                                )
                                        )
                        )
                        .append(Component.text(" to go home.").color(ChatColors.Yellow))
        )

        return true;
    }
}