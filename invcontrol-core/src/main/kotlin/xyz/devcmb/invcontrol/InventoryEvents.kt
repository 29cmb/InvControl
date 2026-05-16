package xyz.devcmb.invcontrol

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.devcmb.invcontrol.chest.page.ChestPageInventoryHolder

class InventoryEvents : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if(event.view.topInventory.holder is ChestPageInventoryHolder) {
            event.isCancelled = true
            if(event.currentItem == null) return
            InvControlRegistry.click(event.view.topInventory, event.currentItem!!, event.isRightClick)
        }
    }
}