package xyz.devcmb.invcontrol.chest

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.UUID

class ChestInventoryHolder(
    val uuid: UUID,
) : InventoryHolder {
    lateinit var inventory: Inventory
    override fun getInventory(): Inventory = inventory
}
