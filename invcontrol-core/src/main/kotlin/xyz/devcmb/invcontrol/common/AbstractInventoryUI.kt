package xyz.devcmb.invcontrol.common

import org.bukkit.entity.Player

abstract class AbstractInventoryUI(open val player: Player) {
    /**
     * Shows the inventory UI
     */
    abstract fun show()
    /**
     * Reloads the inventory view
     */
    abstract fun reload()
}