package xyz.devcmb.invcontrol.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.devcmb.invcontrol.Registry
import xyz.devcmb.invcontrol.chest.ChestInventoryHolder

/**
 * The [Listener] for most of the inventory events
 */
internal class InventoryEvents : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if(event.view.topInventory.holder is ChestInventoryHolder) {
            event.isCancelled = true
            if(event.currentItem == null) return
            Registry.buttonClick(event.view.topInventory, event.currentItem!!)
        }
    }
}