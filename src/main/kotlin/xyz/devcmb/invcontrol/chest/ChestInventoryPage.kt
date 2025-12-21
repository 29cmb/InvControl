package xyz.devcmb.invcontrol.chest

/**
 * A single page for a chest inventory
 * @property ui The UI instance the page is part of
 */
class ChestInventoryPage(val ui: ChestInventoryUI) {
    val items: MutableList<InventoryItem> = ArrayList()

    /**
     * Adds an item to the inventory
     * @param item The [InventoryItem] to add to the inventory
     */
    fun addItem(item: InventoryItem) {
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