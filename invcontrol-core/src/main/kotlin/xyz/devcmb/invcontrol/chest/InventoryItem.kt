package xyz.devcmb.invcontrol.chest

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

/**
 * The main item class that the library keeps track of
 * @property getItemStack The method invoked to get the [org.bukkit.inventory.ItemStack] whenever the inventory opens or is reloaded
 * @property slot The numerical slot where the [org.bukkit.inventory.ItemStack] from [getItemStack] is placed. Must be at or below the amount of rows multiplied by 9 minus 1
 * @property onClick The method that is invoked whenever the element is clicked
 */
open class InventoryItem(
    open var getItemStack: (page: ChestInventoryPage, item: InventoryItem) -> ItemStack,
    open var slot: Int,
    open var onClick: (page: ChestInventoryPage, item: InventoryItem) -> Unit = { page, item -> },
    open var onRightClick: (page: ChestInventoryPage, item: InventoryItem) -> Unit = { page, item -> }
) {
    internal var page: ChestInventoryPage? = null
    var uuid: UUID = UUID.randomUUID()

    /**
     * Internal method for registering the parent UI and with the registry
     */
    internal fun register(page: ChestInventoryPage) {
        this.page = page
    }

    /**
     * Internal method for setting up the item stack provided with the item identifier
     */
    internal open fun formulateItemStack(): ItemStack {
        val stack: ItemStack = getItemStack(page!!, this)
        val meta = stack.itemMeta
        if(meta != null) { // Empty ItemStacks have no item meta
            meta.persistentDataContainer.set(
                NamespacedKey("invctrl", "item_id"),
                PersistentDataType.STRING,
                uuid.toString()
            )

            stack.itemMeta = meta
        }
        return stack
    }

    /**
     * Internal method for handling click events.
     * This passes the instance of the [page] and [xyz.devcmb.invcontrol.chest.InventoryItem] along to the function
     * Alongside this, it also has a 150ms cooldown
     */
    internal fun handleOnClick() {
        if(page!!.lastButtonClick + 150 > System.currentTimeMillis()) return // Cooldown to prevent double-clicking
        page!!.lastButtonClick = System.currentTimeMillis()
        onClick(page!!, this)
    }

    /**
     * Internal method for handling right click events.
     * This passes the instance of the [page] and [xyz.devcmb.invcontrol.chest.InventoryItem] along to the function
     * Alongside this, it also has a 150ms cooldown
     */
    internal fun handleOnRightClick() {
        if(page!!.lastButtonClick + 150 > System.currentTimeMillis()) return // Cooldown to prevent double-clicking
        page!!.lastButtonClick = System.currentTimeMillis()
        onRightClick(page!!, this)
    }
}