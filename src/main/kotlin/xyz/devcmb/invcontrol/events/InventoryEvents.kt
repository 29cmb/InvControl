package xyz.devcmb.invcontrol.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.devcmb.invcontrol.Registry

/**
 * The [Listener] for most of the inventory events
 */
internal class InventoryEvents : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if(event.currentItem == null) return

        val shouldCancel = Registry.buttonClick(event.currentItem!!)
        if(shouldCancel) {
            event.isCancelled = true
        }
    }
}