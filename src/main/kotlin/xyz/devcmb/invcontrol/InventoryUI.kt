package xyz.devcmb.invcontrol

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class InventoryUI(val player: Player, title: Component = Component.text("Inventory"), val rows: Int = 3) {
    private val inv: Inventory = Bukkit.getServer().createInventory(player, rows, title)
    fun show() {
        player.openInventory(inv)
    }
}