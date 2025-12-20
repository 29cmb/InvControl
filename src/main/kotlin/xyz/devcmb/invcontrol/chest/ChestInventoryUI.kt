package xyz.devcmb.invcontrol.chest

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import xyz.devcmb.invcontrol.common.AbstractInventoryUI
import xyz.devcmb.invcontrol.common.InventoryItem

/**
 * The base for chest inventory menus
 * @property player The player that the UI is shown to.
 * @property title The title of the inventory UI. Defaults to "Inventory"
 * @property rows The amount of rows in the inventory UI. Defaults to 3
 * @constructor Creates the inventory from the bukkit server method
 */
class ChestInventoryUI(
    override val player: Player,
    title: Component = Component.text("Inventory"),
    val rows: Int = 3
) : AbstractInventoryUI(player) {
    private val inv: Inventory = Bukkit.getServer().createInventory(player, rows * 9, title)
    private val items: MutableList<InventoryItem> = ArrayList()

    /**
     * Shows the attached player the inventory
     */
    override fun show() {
        propagateItems()
        player.openInventory(inv)
    }

    /**
     * Reloads the inventory view
     */
    override fun reload() {
        propagateItems()
    }

    /**
     * Sets up the slots in the inventory with the formulated item stack
     */
    private fun propagateItems() {
        for(item in items) {
            inv.setItem(item.slot, item.formulateItemStack())
        }
    }

    /**
     * Adds an item to the inventory
     * @param item The [InventoryItem] to add to the inventory
     */
    fun addItem(item: InventoryItem) {
        val maxSlot = (rows * 9 - 1)
        if (item.slot < 0 || item.slot > maxSlot) {
            throw IllegalArgumentException("Slot must be between 0 and $maxSlot")
        }

        items.add(item)
        item.register(this)
    }
}