package xyz.devcmb.invcontrol.chest

import net.kyori.adventure.text.Component
import xyz.devcmb.invcontrol.chest.page.ChestPage
import xyz.devcmb.invcontrol.chest.page.ChestPageContext

class ChestInventoryContext internal constructor(val inventory: ChestInventory) {
    fun page(id: String, title: Component, rows: Int, default: Boolean = false, build: ChestPageContext.() -> Unit) {
        val page = ChestPage(id, title, rows, default, build)
        page.register(inventory)
    }

    fun setPage(id: String) {
        inventory.currentPage = id
    }
}