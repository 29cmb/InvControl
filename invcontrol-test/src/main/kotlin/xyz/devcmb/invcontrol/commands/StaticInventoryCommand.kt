package xyz.devcmb.invcontrol.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import xyz.devcmb.invcontrol.chest.ChestInventoryPage
import xyz.devcmb.invcontrol.chest.ChestInventoryUI
import xyz.devcmb.invcontrol.chest.InventoryItem

class StaticInventoryCommand : CommandExecutor {
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

        val ui = ChestInventoryUI(sender)

        val mainPage = ChestInventoryPage()
        ui.addPage("main", mainPage)

        mainPage.addItem(
            InventoryItem(
                getItemStack = { _, _ ->
                    val itemStack: ItemStack = ItemStack.of(Material.FEATHER)
                    val meta: ItemMeta = itemStack.itemMeta
                    meta.itemName(Component.text("This is an item"))
                    itemStack.itemMeta = meta

                    itemStack
                },
                slot = 13,
                onClick = { _, item ->
                    sender.sendMessage(
                        Component.empty()
                            .append(Component.text("This is a message from an item with UUID ")
                                .color(NamedTextColor.YELLOW)
                                .append(Component.text(item.uuid.toString())))
                    )
                },
                cancelClickEvents = true
            )
        )

        ui.setPage("main")
        ui.show()

        return true
    }
}