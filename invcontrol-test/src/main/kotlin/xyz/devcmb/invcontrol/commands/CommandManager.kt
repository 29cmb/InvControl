package xyz.devcmb.invcontrol.commands

import org.bukkit.command.CommandExecutor
import xyz.devcmb.invcontrol.InvControl

object CommandManager {
    fun registerAllCommands() {
        registerCommand("basicinv", BasicInventoryCommand())
    }

    private fun registerCommand(name: String, command: CommandExecutor) {
        InvControl.plugin.getCommand(name)!!.setExecutor(command)
    }
}