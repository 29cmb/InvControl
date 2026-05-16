package xyz.devcmb.invcontrol.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import xyz.devcmb.invcontrol.chest.ChestInventory
import xyz.devcmb.invcontrol.item.StaticItem

class StaticInventoryCommand : CommandExecutor {
    val inventory = ChestInventory {
        page("main", Component.text("Static Inventory"), 3, true) {
            item(StaticItem {
                type = Material.ARROW
                slot = 12

                onClick {
                    player.sendMessage(Component.text(item.id.toString(), NamedTextColor.YELLOW))
                }
            })
            item(ItemStack(Material.ARROW), 14) {
                player.sendMessage(Component.text(item.id.toString(), NamedTextColor.YELLOW))
            }
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("Only players can use this command").color(NamedTextColor.RED))
            return true
        }

        inventory.show(sender)

        return true
    }
}