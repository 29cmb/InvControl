package xyz.devcmb.invcontrol.chest.map

import xyz.devcmb.invcontrol.InvControlManager
import xyz.devcmb.invcontrol.chest.ChestInventoryPage

/**
 * A class for adding a list of items to a list
 * @property getInventoryItems The function that gets invoked any time the page reloads to fetch the list of items
 * @property startSlot The first slot the items will fill from
 * @property maxItems The max amount of items that can be on a single item page
 * @property itemPage The page number for the items. Not to be confused with a [ChestInventoryPage]
 */
class InventoryItemMap(
    var getInventoryItems: (page: ChestInventoryPage, map: InventoryItemMap) -> ArrayList<InventoryMappedItem>,
    var startSlot: Int,
    var maxItems: Int,
    var itemPage: Int,
) {
    lateinit var page: ChestInventoryPage

    /**
     * The internal function to register the map
     * @param page The parent page
     */
    internal fun register(page: ChestInventoryPage) {
        this.page = page
    }

    // ChatGPT code because I just woke up and don't want to sift through pagination examples
    /**
     * Gets the list of InventoryMappedItems that are visible on the current page
     * @return The [ArrayList] of [InventoryMappedItem] with respect to the [itemPage]
     */
    internal fun formulateItems(): ArrayList<InventoryMappedItem> {
        val allItems = getInventoryItems(page, this)
        if (maxItems <= 0) return ArrayList()

        val pageIndex = maxOf(1, itemPage)
        val startIndex = (pageIndex - 1) * maxItems
        if (startIndex >= allItems.size) return ArrayList()

        val endIndex = minOf(startIndex + maxItems, allItems.size)
        val pageSlice = allItems.subList(startIndex, endIndex)

        val items = ArrayList<InventoryMappedItem>(pageSlice.size)
        for ((i, item) in pageSlice.withIndex()) {
            item.slot = startSlot + i
            InvControlManager.plugin?.logger?.info(item.slot.toString())
            items.add(item)
        }

        return allItems
    }

    /**
     * Moves the page back relative to the min and max pages
     */
    fun pageBack() {
        val allItems = getInventoryItems(page, this)
        val pageCount = if (maxItems <= 0) 0 else (allItems.size + maxItems - 1) / maxItems
        if (pageCount == 0) {
            itemPage = 1
            return
        }

        itemPage--
        if (itemPage < 1) itemPage = pageCount
    }

    /**
     * Moves the page forward relative to the min and max pages
     */
    fun pageForward() {
        val allItems = getInventoryItems(page, this)
        val pageCount = if (maxItems <= 0) 0 else (allItems.size + maxItems - 1) / maxItems
        if (pageCount == 0) {
            itemPage = 1
            return
        }

        itemPage++
        if (itemPage > pageCount) itemPage = 1
    }
}