package xyz.devcmb.invcontrol.chest

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.UUID

/**
 * The holder class for the UUID
 * @property uuid The unique identifier for the inventory holder
 */
class ChestInventoryHolder(
    val uuid: UUID,
) : InventoryHolder {
    lateinit var inv: Inventory
    override fun getInventory(): Inventory = inventory
}
