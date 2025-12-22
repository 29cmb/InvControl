package xyz.devcmb.invcontrol.commands

import org.bukkit.command.CommandExecutor
import xyz.devcmb.invcontrol.InvControl

object CommandManager {
    fun registerAllCommands() {
        registerCommand("basicinv", StaticInventoryCommand())
        registerCommand("aimtrainer", AimTrainerInventoryCommand())
    }

    private fun registerCommand(name: String, command: CommandExecutor) {
        InvControl.plugin.getCommand(name)!!.setExecutor(command)
    }
}