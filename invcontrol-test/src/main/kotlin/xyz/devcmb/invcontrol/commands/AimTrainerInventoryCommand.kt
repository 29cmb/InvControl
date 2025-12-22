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
import xyz.devcmb.invcontrol.chest.ChestInventoryPage
import xyz.devcmb.invcontrol.chest.ChestInventoryUI
import xyz.devcmb.invcontrol.chest.InventoryItem
import kotlin.math.roundToInt

class AimTrainerInventoryCommand : CommandExecutor {
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
            rows = 5,
            title = Component.text("Aim Trainer")
        )

        val mainPage = ChestInventoryPage()
        ui.addPage("main", mainPage)

        var score = 0
        var arrowCount = 1
        val slots = mutableListOf(11, 13, 15)

        for (i in 0..2) {
            mainPage.addItem(InventoryItem(
                getItemStack = { ui, item ->
                    if(arrowCount < i + 1) return@InventoryItem ItemStack.empty()

                    val item = ItemStack.of(Material.ARROW)
                    val meta = item.itemMeta
                    meta.itemName(Component.text("Score: ").append(Component.text(score)).color(NamedTextColor.YELLOW))
                    item.itemMeta = meta

                    item
                },
                slot = slots[i],
                onClick = { ui, item ->
                    score++
                    val currentSlot = item.slot
                    val newSlot = pickUnoccupiedSlot(slots, 26)
                    slots.remove(currentSlot)
                    slots.add(newSlot)
                    item.slot = newSlot
                    ui.reload()
                }
            ))
        }

        for(i in 27..35) {
            mainPage.addItem(InventoryItem(
                getItemStack = { ui, item ->
                    ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
                },
                slot = i
            ))
        }

        // Reset button
        mainPage.addItem(InventoryItem(
            getItemStack = { ui, item ->
                val itemStack = ItemStack.of(Material.RED_CONCRETE)
                val meta = itemStack.itemMeta
                meta.itemName(Component.text("Reset").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
                itemStack.itemMeta = meta
                itemStack
            },
            slot = 36,
            onClick = { ui, item ->
                score = 0
                ui.reload()
            }
        ))

        // Arrow count selector
        mainPage.addItem(InventoryItem(
            getItemStack = { ui, item ->
                val itemStack = ItemStack.of(Material.SPECTRAL_ARROW)
                val meta = itemStack.itemMeta
                meta.itemName(Component.text("Mode").color(NamedTextColor.AQUA))
                meta.lore(listOf(
                    Component.text("> 1 arrow")
                        .color(if(arrowCount == 1) NamedTextColor.YELLOW else NamedTextColor.DARK_GRAY)
                        .decorate(if(arrowCount == 1) TextDecoration.BOLD else TextDecoration.UNDERLINED) // workaround to prevent needing extra variables
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.UNDERLINED, false),
                    Component.text("> 2 arrows")
                        .color(if(arrowCount == 2) NamedTextColor.YELLOW else NamedTextColor.DARK_GRAY)
                        .decorate(if(arrowCount == 2) TextDecoration.BOLD else TextDecoration.UNDERLINED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.UNDERLINED, false),
                    Component.text("> 3 arrows")
                        .color(if(arrowCount == 3) NamedTextColor.YELLOW else NamedTextColor.DARK_GRAY)
                        .decorate(if(arrowCount == 3) TextDecoration.BOLD else TextDecoration.UNDERLINED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.UNDERLINED, false)
                ))
                itemStack.itemMeta = meta

                itemStack
            },
            slot = 44,
            onClick = { ui, item ->
                arrowCount++
                if(arrowCount > 3) {
                    arrowCount = 1
                }
                ui.reload()
            }
        ))

        ui.setPage("main")
        ui.show()

        return true
    }

    fun pickUnoccupiedSlot(slots: MutableList<Int>, max: Int): Int {
        val num = (Math.random() * max).roundToInt()
        if(num in slots) return pickUnoccupiedSlot(slots, max)

        return num
    }
}