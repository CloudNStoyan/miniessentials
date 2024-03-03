package dev.stojan.miniessentials.commands

import dev.stojan.miniessentials.shared.ChatColors
import dev.stojan.miniessentials.shared.Coordinates
import dev.stojan.miniessentials.shared.CustomNamespacedKeys
import dev.stojan.miniessentials.shared.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.*


class BackCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        val playerStorageData = sender.persistentDataContainer;

        val backCoordsAsString = playerStorageData.get(CustomNamespacedKeys.BackCoords, PersistentDataType.STRING);
        val backWorldIdAsString = playerStorageData.get(CustomNamespacedKeys.BackWorldId, PersistentDataType.STRING)

        if (backCoordsAsString == null || backWorldIdAsString == null) {
            sender.sendMessage(Component.text("There is no destination to go back to.").color(ChatColors.Yellow))
            return true;
        }

        val backCoords = Coordinates.parse(backCoordsAsString);
        val backWorldId = UUID.fromString(backWorldIdAsString);

        sender.sendMessage(Component.text("Teleporting...").color(ChatColors.Yellow))

        playerStorageData.set(
                CustomNamespacedKeys.BackCoords,
                PersistentDataType.STRING,
                Coordinates(sender.x, sender.y, sender.z).toString()
        );

        playerStorageData.set(
                CustomNamespacedKeys.BackWorldId,
                PersistentDataType.STRING,
                sender.world.uid.toString()
        )

        sender.teleport(Location(Bukkit.getWorld(backWorldId), backCoords.x, backCoords.y, backCoords.z, sender.yaw, sender.pitch))

        return true;
    }
}