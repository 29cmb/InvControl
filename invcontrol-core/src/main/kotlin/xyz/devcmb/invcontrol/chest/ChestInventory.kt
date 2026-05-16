package xyz.devcmb.invcontrol.chest

import org.bukkit.entity.Player
import xyz.devcmb.invcontrol.InvControlRegistry
import xyz.devcmb.invcontrol.chest.page.ChestPage

class ChestInventory(init: ChestInventoryContext.() -> Unit) {
    val context: ChestInventoryContext = ChestInventoryContext(this)
    val pages: MutableList<ChestPage> = mutableListOf()
    var currentPage: String? = null

    init {
        context.init()
        InvControlRegistry.register(this)
    }

    fun show(player: Player) {
        if(currentPage == null) throw IllegalStateException("Current page is null")

        val page = pages.find { it.id == currentPage }
            ?: throw IllegalStateException("Current page does not exist or is not registered")

        page.show(player)
    }
}