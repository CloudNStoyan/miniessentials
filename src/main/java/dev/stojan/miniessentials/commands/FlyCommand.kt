package dev.stojan.miniessentials.commands

import dev.stojan.miniessentials.shared.ChatColors
import dev.stojan.miniessentials.shared.CustomNamespacedKeys
import dev.stojan.miniessentials.shared.Messages
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class FlyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        if (!sender.hasPermission("miniessentials.fly")) {
            sender.sendMessage(Component
                    .text("You don't have the miniessentials.fly permission.").color(ChatColors.Danger))
            return true;
        }

        if (sender.allowFlight) {
            sender.isFlying = false;
            sender.allowFlight = false;

            sender.sendMessage(
                    Component.text("Fly is ").color(ChatColors.Yellow)
                            .append(Component.text("disabled").color(ChatColors.Danger))
                            .append(Component.text(".").color(ChatColors.Yellow))
            );

        } else {
            sender.allowFlight = true;
            sender.isFlying = true;

            sender.sendMessage(
                    Component.text("Fly is ").color(ChatColors.Yellow)
                            .append(Component.text("enabled").color(ChatColors.Success))
                            .append(Component.text(".").color(ChatColors.Yellow))
            );
        }

        val playerStorageData = sender.persistentDataContainer;

        playerStorageData.set(CustomNamespacedKeys.FlyState, PersistentDataType.BOOLEAN, sender.allowFlight);

        return true;
    }
}