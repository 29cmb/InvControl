package xyz.devcmb.invcontrol

import org.bukkit.plugin.java.JavaPlugin
import xyz.devcmb.invcontrol.commands.CommandManager
import java.util.logging.Logger

class InvControl : JavaPlugin() {
    companion object {
        lateinit var pluginLogger: Logger
        lateinit var plugin: InvControl
    }
    override fun onEnable() {
        plugin = this
        pluginLogger = logger

        InvControlManager.setPlugin(this)
        CommandManager.registerAllCommands()
    }

    override fun onDisable() {
    }
}
