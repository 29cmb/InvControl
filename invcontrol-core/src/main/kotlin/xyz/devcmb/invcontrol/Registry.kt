package xyz.devcmb.invcontrol

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import xyz.devcmb.invcontrol.chest.ChestInventoryHolder
import xyz.devcmb.invcontrol.chest.ChestInventoryUI

/**
 * The object responsible for keeping track of the items in inventories.
 */
internal object Registry {
    private val chestInventories: HashMap<String, ChestInventoryUI> = HashMap()

    /**
     * Registers a single inventory
     * @param inv The inventory to registry
     */
    fun registerInventory(inv: ChestInventoryUI) {
        chestInventories[inv.uuid.toString()] = inv
    }

    /**
     * The handler for a button click event, comparing against registry entries
     * @param inventory The [Inventory] the button was clicked in
     * @param itemStack The [ItemStack] that was clicked
     * @return If the click event should be cancelled
     */
    fun buttonClick(inventory: Inventory, itemStack: ItemStack): Boolean {
        val holder = inventory.holder as? ChestInventoryHolder ?: return false
        val chestInventory = chestInventories[holder.uuid.toString()] ?: return false

        val inventoryItem = chestInventory.currentItems.entries
            .firstOrNull { (_, stack) -> stack.isSimilar(itemStack) }
            ?.key
            ?: return false

        inventoryItem.handleOnClick()
        return inventoryItem.cancelClickEvents
    }
}