package xyz.devcmb.invcontrol.common

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.devcmb.invcontrol.Registry
import java.util.UUID

/**
 * The main item class that the library keeps track of
 * @property getItemStack The method invoked to get the [ItemStack] whenever the inventory opens or is reloaded
 * @property slot The numerical slot where the [ItemStack] from [getItemStack] is placed. Must be at or below the amount of rows multiplied by 9 minus 1
 * @property onClick The method that is invoked whenever the element is clicked
 * @property cancelClickEvents Should clicking this item snap it back into the inventory it was pulled from
 */
class InventoryItem(
    val getItemStack: () -> ItemStack,
    val slot: Int,
    val onClick: (ui: AbstractInventoryUI) -> Unit = {},
    val cancelClickEvents: Boolean = true
) {
    internal var ui: AbstractInventoryUI? = null
        private set
    internal var uuid: UUID = UUID.randomUUID()

    /**
     * Internal method for registering the parent UI and with the registry
     */
    internal fun register(ui: AbstractInventoryUI) {
        this.ui = ui
        Registry.registerItem(this)
    }

    /**
     * Internal method for setting up the item stack provided with the item identifier
     */
    internal fun formulateItemStack(): ItemStack {
        val stack: ItemStack = getItemStack()
        val meta = stack.itemMeta
        meta.persistentDataContainer.set(
            NamespacedKey("invctrl", "item_id"),
            PersistentDataType.STRING,
            uuid.toString()
        )

        stack.itemMeta = meta
        return stack
    }

    /**
     * Internal method for handling click events. Currently only here to pass in the [ui] instance
     */
    internal fun handleOnClick() {
        onClick(ui!!)
    }
}
