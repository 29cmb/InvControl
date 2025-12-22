package xyz.devcmb.invcontrol.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import xyz.devcmb.invcontrol.chest.ChestInventoryPage
import xyz.devcmb.invcontrol.chest.ChestInventoryUI
import xyz.devcmb.invcontrol.chest.InventoryItem
import xyz.devcmb.invcontrol.chest.map.InventoryItemMap
import xyz.devcmb.invcontrol.chest.map.InventoryMappedItem

class PlayerListCommand : CommandExecutor {
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

        val ui = ChestInventoryUI(
            sender,
            rows = 6,
            title = Component.text("Online players")
        )

        val page = ChestInventoryPage()
        ui.addPage("main", page)

        val itemMap = InventoryItemMap(
            getInventoryItems = { page, item ->
                val items: ArrayList<InventoryMappedItem> = ArrayList()
                for(player in Bukkit.getOnlinePlayers()) {
                    items.add(InventoryMappedItem(
                        getItemStack = { page, item ->
                            val itemStack = ItemStack.of(Material.PLAYER_HEAD)
                            val meta = itemStack.itemMeta as SkullMeta
                            meta.owningPlayer = player
                            meta.displayName(Component.text(player.name).color(NamedTextColor.YELLOW))
                            itemStack.itemMeta = meta

                            itemStack
                        }
                    ))
                }

                items
            },
            startSlot = 0,
            maxItems = 36,
            itemPage = 1,
        )
        page.addItemMap(itemMap)

        for(i in 36..44) {
            page.addItem(InventoryItem(
                getItemStack = { page, item ->
                    val itemStack = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
                    val meta = itemStack.itemMeta
                    meta.isHideTooltip = true
                    itemStack.itemMeta = meta

                    itemStack
                },
                slot = i
            ))
        }

        // Previous Page
        page.addItem(InventoryItem(
            getItemStack = { page, item ->
                val itemStack = ItemStack.of(Material.ARROW)
                val meta = itemStack.itemMeta
                meta.itemName(Component.text("Previous Page").color(NamedTextColor.YELLOW))
                meta.lore(listOf<Component>(
                    Component.text("Page ")
                        .append(Component.text(itemMap.itemPage.toString()))
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false)
                ))
                itemStack.itemMeta = meta

                itemStack
            },
            slot = 45,
            onClick = { page, item ->
                itemMap.pageBack()
                page.reload()
            }
        ))

        // Next Page
        page.addItem(InventoryItem(
            getItemStack = { page, item ->
                val itemStack = ItemStack.of(Material.ARROW)
                val meta = itemStack.itemMeta
                meta.itemName(Component.text("Next Page").color(NamedTextColor.YELLOW))
                meta.lore(listOf<Component>(
                    Component.text("Page ")
                        .append(Component.text(itemMap.itemPage.toString()))
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false)
                ))
                itemStack.itemMeta = meta

                itemStack
            },
            slot = 53,
            onClick = { page, item ->
                itemMap.pageForward()
                page.reload()
            }
        ))

        ui.setPage("main")
        ui.show()

        return true
    }

}