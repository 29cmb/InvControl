package xyz.devcmb.invcontrol.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import xyz.devcmb.invcontrol.InvControl
import xyz.devcmb.invcontrol.chest.ChestInventoryPage
import xyz.devcmb.invcontrol.chest.ChestInventoryUI
import xyz.devcmb.invcontrol.chest.InventoryItem
import xyz.devcmb.invcontrol.chest.map.InventoryItemMap
import xyz.devcmb.invcontrol.chest.map.InventoryMappedItem

class UpdatingMapCommand : CommandExecutor {
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

        val items: ArrayList<Material> = ArrayList()
        val choices = arrayListOf(
            Material.STONE,
            Material.IRON_BLOCK,
            Material.SPECTRAL_ARROW,
            Material.REDSTONE,
            Material.LEATHER_HELMET,
            Material.DANDELION,
            Material.DIAMOND_HORSE_ARMOR,
            Material.BIRCH_BUTTON,
            Material.ACACIA_BUTTON,
            Material.ACACIA_LOG,
            Material.BEACON
        )

        (1..5).forEach { _ -> items.add(choices.random()) }

        val ui = ChestInventoryUI(sender, rows = 5)
        val page = ChestInventoryPage()
        ui.addPage("main", page)

        val map = InventoryItemMap(
            getInventoryItems = { _, _ ->
                InvControl.pluginLogger.info("Getting inventory items for the changing item map")
                val itemList: ArrayList<InventoryMappedItem> = ArrayList()
                items.forEach {
                    itemList.add(InventoryMappedItem(
                        getItemStack = { _, _ -> ItemStack.of(it) },
                        onClick = { page, item ->
                            sender.sendMessage(Component.text("Hi! I have a UUID of ${item.uuid}!").color(NamedTextColor.YELLOW))
                        }
                    ))
                }

                itemList
            },
            startSlot = 0,
            maxItems = 27,
            itemPage = 1,
        )
        page.addItemMap(map)

        for(i in 27..35) {
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

        page.addItem(InventoryItem(
            getItemStack = { _, _ ->
                ItemStack.of(Material.BLUE_CONCRETE).apply {
                    val meta = itemMeta
                    meta.itemName(Component.text("Random item").color(NamedTextColor.BLUE))
                    itemMeta = meta
                }
            },
            slot = 40,
            onClick = { page,_ ->
                items.add(choices.random())
                page.reload()
            }
        ))

        page.addItem(InventoryItem(
            getItemStack = { page, item ->
                ItemStack.of(Material.ARROW).apply {
                    val meta = itemMeta
                    meta.itemName(Component.text("Previous Page").color(NamedTextColor.YELLOW))
                    meta.lore(arrayListOf<Component>(
                        Component.text("Page ").append(Component.text(map.itemPage.toString()))
                            .color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
                    ))
                    itemMeta = meta
                }
            },
            slot = 36,
            onClick = { page, _ ->
                map.pageBack()
                page.reload()
            }
        ))

        page.addItem(InventoryItem(
            getItemStack = { page, item ->
                ItemStack.of(Material.ARROW).apply {
                    val meta = itemMeta
                    meta.itemName(Component.text("Next Page").color(NamedTextColor.YELLOW))
                    meta.lore(arrayListOf<Component>(
                        Component.text("Page ").append(Component.text(map.itemPage.toString()))
                            .color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
                    ))
                    itemMeta = meta
                }
            },
            slot = 44,
            onClick = { page, _ ->
                map.pageForward()
                page.reload()
            }
        ))

        ui.setPage("main")
        ui.show()
        return true
    }
}