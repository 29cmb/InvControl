package xyz.devcmb.invcontrol

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.devcmb.invcontrol.common.InventoryItem

/**
 * The object responsible for keeping track of the items in inventories.
 * TODO: Remove items once the menu has been closed
 */
internal object Registry {
    private val items: HashMap<String, InventoryItem> = HashMap()

    /**
     * Registers a single item
     * @param item The item to register
     */
    fun registerItem(item: InventoryItem) {
        items[item.uuid.toString()] = item
    }

    /**
     * The handler for a button click event, comparing against registry entries
     * @param itemStack The [ItemStack] that was clicked
     * @return If the click event should be cancelled
     */
    fun buttonClick(itemStack: ItemStack): Boolean {
        val meta = itemStack.itemMeta
        val itemId = meta.persistentDataContainer.get(NamespacedKey("invctrl", "item_id"), PersistentDataType.STRING)

        if(items.containsKey(itemId)) {
            val item = items[itemId]!!
            item.handleOnClick()

            return item.cancelClickEvents
        }

        return false
    }
}