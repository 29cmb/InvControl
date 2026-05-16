package xyz.devcmb.invcontrol.chest.page

import org.bukkit.inventory.ItemStack
import xyz.devcmb.invcontrol.chest.ChestInventory
import xyz.devcmb.invcontrol.item.Item
import xyz.devcmb.invcontrol.item.ItemContext
import xyz.devcmb.invcontrol.item.StaticItem

class ChestPageContext internal constructor(inventory: ChestInventory) {
    val items: ArrayList<Item> = ArrayList()

    fun item(item: Item) {
        items.add(item)
    }

    fun item(item: ItemStack, slot: Int, onClick: (ItemContext.() -> Unit)? = null) {
        items.add(StaticItem {
            type = item.type
            meta = item.itemMeta
            this@StaticItem.slot = slot

            onClick { onClick?.let { this@StaticItem.it() } }
        })
    }
}