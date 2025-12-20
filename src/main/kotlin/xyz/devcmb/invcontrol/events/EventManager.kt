package xyz.devcmb.invcontrol.events

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import xyz.devcmb.invcontrol.InvControlManager

/**
 * Handles the event listeners for the library
 */
internal object EventManager {
    /**
     * Registers all the events
     */
    fun registerAllEvents() {
        registerEvent(InventoryEvents())
    }

    /**
     * Registers a single event
     * @param event The event listener to register
     */
    private fun registerEvent(event: Listener) {
        val manager = Bukkit.getServer().pluginManager
        manager.registerEvents(event, InvControlManager.plugin!!)
    }
}