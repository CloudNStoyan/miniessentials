package dev.stojan.miniessentials.shared

import net.kyori.adventure.text.Component

object Messages {
    fun cantBeUsedByConsoleMessage(): Component {
        return Component.text("This command can't be used by the console.").color(ChatColors.Danger)
    }
}