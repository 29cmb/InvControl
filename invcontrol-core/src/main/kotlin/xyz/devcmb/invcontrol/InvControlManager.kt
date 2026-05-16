package xyz.devcmb.invcontrol

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin

object InvControlManager {
    var inventoryEvents: InventoryEvents? = null
    val INV_CONTROL_ITEM_ID = NamespacedKey("invcontrol", "item_id")

    internal var plugin: Plugin? = null
        set(value) {
            inventoryEvents?.let { HandlerList.unregisterAll(it) }
            value?.let {
                inventoryEvents = InventoryEvents()
                Bukkit.getServer().pluginManager.registerEvents(inventoryEvents!!, it)
            }
            field = value
        }

    fun setPlugin(plugin: Plugin?) {
        this.plugin = plugin
    }
}