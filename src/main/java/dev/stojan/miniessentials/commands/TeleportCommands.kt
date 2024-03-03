package dev.stojan.miniessentials.commands

import dev.stojan.miniessentials.shared.ChatColors
import dev.stojan.miniessentials.shared.Coordinates
import dev.stojan.miniessentials.shared.CustomNamespacedKeys
import dev.stojan.miniessentials.shared.Messages
import dev.stojan.miniessentials.state.TeleportRequests
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.time.Instant


class TpaCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        if (args == null || args.isEmpty()) {
            sender.sendMessage("Wrong Usage: /tpa <NAME>")
            return true;
        }

        val receivingPlayer = Bukkit.getPlayer(args[0]);

        if (receivingPlayer == null) {
            sender.sendMessage("Player was not found")
            return true;
        }


        val tpAcceptMessage = Component.text("/tpaccept").color(ChatColors.Red);
        val tpDenyMessage = Component.text("/tpdeny").color(ChatColors.Red);

        val senderPlayerName = sender.name();

        val announceTeleportIntentionMessage = Component.text().append(senderPlayerName)
                .color(ChatColors.Red)
                .append(
                        Component.text(" has requested to teleport to you.")
                                .color(ChatColors.Yellow)
                )
                .appendNewline()
                .append(
                        Component.text("To teleport, type ").color(ChatColors.Yellow)
                )
                .append(
                        tpAcceptMessage.clickEvent(ClickEvent.runCommand("/tpaccept"))
                                .hoverEvent(HoverEvent.showText(Component.text("Accept teleport")))
                )
                .append(Component.text(".").color(ChatColors.Yellow))
                .appendNewline()
                .append(Component.text("To deny this request, type ").color(ChatColors.Yellow))
                .append(
                        tpDenyMessage.clickEvent(ClickEvent.runCommand("/tpdeny"))
                                .hoverEvent(HoverEvent.showText(Component.text("Deny teleport")))
                )
                .append(Component.text(".").color(ChatColors.Yellow))
                .appendNewline()
                .append(Component.text("This request will timeout after ").color(ChatColors.Yellow))
                .append(Component.text("120 seconds").color(ChatColors.Red))
                .append(Component.text(".").color(ChatColors.Yellow))

        receivingPlayer.sendMessage(announceTeleportIntentionMessage);

        val receiverPlayerName = receivingPlayer.name();

        val announceTeleportSentMessage = Component.text("Request sent to ")
                .color(ChatColors.Yellow)
                .append(receiverPlayerName.color(ChatColors.Red))
                .append(Component.text(".").color(ChatColors.Yellow))
                .appendNewline()
                .append(Component.text("To cancel this request, type ").color(ChatColors.Yellow))
                .append(
                        Component.text("/tpacancel")
                                .color(ChatColors.Red)
                                .clickEvent(ClickEvent.runCommand("/tpacancel"))
                                .hoverEvent(HoverEvent.showText(Component.text("Cancel teleport")))
                )
                .append(Component.text(".").color(ChatColors.Yellow))

        sender.sendMessage(announceTeleportSentMessage);

        TeleportRequests.addTeleportRequest(receivingPlayer.uniqueId, sender.uniqueId);

        return true;
    }
}

class TpacceptCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        val teleportRequest = TeleportRequests.getTeleportRequest(sender.uniqueId);

        if (teleportRequest == null) {
            sender.sendMessage(
                    Component.text("There are no active teleport requests to accept.")
                            .color(ChatColors.Yellow)
            );
            return true;
        }

        val now = Instant.now();

        if (now.epochSecond - teleportRequest.requestedAt.epochSecond >= 120) {
            sender.sendMessage(Component.text("Teleport request timed out!").color(ChatColors.Yellow))
            TeleportRequests.clearTeleportRequest(sender.uniqueId);
            return true;
        }

        val playerToTeleport = Bukkit.getPlayer(teleportRequest.playerId)

        if (playerToTeleport == null) {
            sender.sendMessage(Component.text("Player is no longer online.").color(ChatColors.Yellow))
            return true;
        }

        val acceptedTeleportMessage = Component.text("Teleport request accepted.").color(ChatColors.Yellow);
        sender.sendMessage(acceptedTeleportMessage)

        playerToTeleport.sendMessage(Component.text("Teleporting...").color(ChatColors.Yellow))

        playerToTeleport.persistentDataContainer.set(
                CustomNamespacedKeys.BackCoords,
                PersistentDataType.STRING,
                Coordinates(sender.x, sender.y, sender.z).toString()
        );

        playerToTeleport.persistentDataContainer.set(
                CustomNamespacedKeys.BackWorldId,
                PersistentDataType.STRING,
                playerToTeleport.world.uid.toString()
        )

        playerToTeleport.teleport(sender)

        TeleportRequests.clearTeleportRequest(sender.uniqueId);

        return true;
    }
}

class TpdenyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        val teleportRequest = TeleportRequests.getTeleportRequest(sender.uniqueId);

        if (teleportRequest == null) {
            sender.sendMessage(
                    Component.text("There are no active teleport requests to deny.")
                            .color(ChatColors.Yellow)
            );
            return true;
        }

        val now = Instant.now();

        if (now.epochSecond - teleportRequest.requestedAt.epochSecond >= 120) {
            sender.sendMessage(Component.text("Teleport request timed out!").color(ChatColors.Yellow))
            TeleportRequests.clearTeleportRequest(sender.uniqueId);
            return true;
        }

        TeleportRequests.clearTeleportRequest(sender.uniqueId);

        val deniedTeleportMessage = Component.text("Teleport request denied.").color(ChatColors.Yellow);
        sender.sendMessage(deniedTeleportMessage)

        return true;
    }
}

class TpcancelCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Messages.cantBeUsedByConsoleMessage());
            return true;
        }

        val canceledTeleportRequestsPlayerIds = TeleportRequests.clearTeleportRequestBySender(sender.uniqueId);

        if (canceledTeleportRequestsPlayerIds.isEmpty()) {
            sender.sendMessage(Component.text("There are no active teleport requests to cancel.")
                    .color(ChatColors.Yellow))
        } else {
            for (canceledTeleportRequestPlayerId in canceledTeleportRequestsPlayerIds) {
                val canceledTeleportRequestPlayer = Bukkit.getPlayer(canceledTeleportRequestPlayerId) ?: continue;

                canceledTeleportRequestPlayer.sendMessage(
                        Component.text("Teleport request was canceled by the sender.").color(ChatColors.Yellow)
                )
            }
            sender.sendMessage(Component.text("Teleport request(s) canceled.").color(ChatColors.Yellow))
        }

        return true;
    }
}