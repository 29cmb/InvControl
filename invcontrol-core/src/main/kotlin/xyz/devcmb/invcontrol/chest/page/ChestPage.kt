package xyz.devcmb.invcontrol.chest.page

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import xyz.devcmb.invcontrol.chest.ChestInventory
import java.util.UUID

class ChestPage(
    val id: String,
    title: Component,
    rows: Int,
    val default: Boolean,
    val build: ChestPageContext.() -> Unit
) {
    lateinit var chestInventory: ChestInventory
    lateinit var context: ChestPageContext

    val uuid: UUID = UUID.randomUUID()
    val inventory: Inventory

    init {
        val holder = ChestPageInventoryHolder(uuid)
        inventory = Bukkit.createInventory(holder, rows * 9, title)
        holder.inv = inventory
    }

    fun register(inventory: ChestInventory) {
        this.chestInventory = inventory
        context = ChestPageContext(inventory)
        build(context)
        inventory.pages.add(this)
        if(default) {
            inventory.currentPage = id
        }
    }

    fun show(player: Player) {
        inventory.clear()
        context.items.forEach {
            val stack = it.build(player)
            inventory.setItem(it.context.slot, stack)
        }

        player.openInventory(inventory)
        // TODO: item updating stuff
    }
}