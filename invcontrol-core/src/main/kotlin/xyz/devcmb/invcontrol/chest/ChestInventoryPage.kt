package xyz.devcmb.invcontrol.chest

/**
 * A single page for a chest inventory
 */
class ChestInventoryPage() {
    val items: MutableList<InventoryItem> = ArrayList()
    lateinit var ui: ChestInventoryUI

    internal fun register(inventoryUI: ChestInventoryUI) {
        this.ui = inventoryUI
    }

    /**
     * Adds an item to the inventory
     * @param item The [InventoryItem] to add to the inventory
     * @throws IllegalStateException If the page hasn't been registered yet
     * @throws IllegalArgumentException If the slot isn't within the inventory menu
     */
    fun addItem(item: InventoryItem) {
        if(!this::ui.isInitialized) {
            throw IllegalStateException("Cannot add an item to a page before the page has been registered")
        }

        val maxSlot = (ui.rows * 9 - 1)
        if (item.slot < 0 || item.slot > maxSlot) {
            throw IllegalArgumentException("Slot must be between 0 and $maxSlot")
        }

        items.add(item)
        item.register(this)
    }

    /**
     * Reload the inventory menu
     * @throws IllegalStateException Exception for when the page is not currently active
     */
    fun reload() {
        if(ui.currentPage !== this) {
            throw IllegalStateException("Cannot reload a page that is not active")
        }

        ui.reload()
    }
}