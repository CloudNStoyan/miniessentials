package dev.stojan.miniessentials

import dev.stojan.miniessentials.commands.*
import dev.stojan.miniessentials.listeners.DyingListener
import dev.stojan.miniessentials.listeners.JoiningListener
import org.bukkit.plugin.java.JavaPlugin

class MiniEssentials : JavaPlugin() {
    private fun registerCommands() {
        getCommand("fly")?.setExecutor(FlyCommand())

        getCommand("home")?.setExecutor(HomeCommand())
        getCommand("sethome")?.setExecutor(SetHomeCommand())

        getCommand("tpa")?.setExecutor(TpaCommand())
        getCommand("tpaccept")?.setExecutor(TpacceptCommand())
        getCommand("tpdeny")?.setExecutor(TpdenyCommand())
        getCommand("tpacancel")?.setExecutor(TpcancelCommand())

        getCommand("back")?.setExecutor(BackCommand())
    }

    private fun registerListeners()  {
        val pluginManager = server.pluginManager

        pluginManager.registerEvents(JoiningListener(), this)
        pluginManager.registerEvents(DyingListener(), this)
    }

    override fun onEnable() {
        // Plugin startup logic

        registerCommands()
        registerListeners()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
