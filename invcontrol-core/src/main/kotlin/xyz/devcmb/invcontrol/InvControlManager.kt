package xyz.devcmb.invcontrol

import org.bukkit.plugin.Plugin
import xyz.devcmb.invcontrol.events.EventManager

/**
 * The main state management for the library
 */
object InvControlManager {
    internal var plugin: Plugin? = null

    /**
     * Sets the [InvControlManager.plugin] for event calls
     * @param plugin The plugin instance
     * @throws IllegalStateException Exception when the plugin is already set
     */
    fun setPlugin(plugin: Plugin) {
        if (this.plugin != null) {
            throw IllegalStateException("Plugin is already set.")
        }

        this.plugin = plugin
        EventManager.registerAllEvents()
    }
}