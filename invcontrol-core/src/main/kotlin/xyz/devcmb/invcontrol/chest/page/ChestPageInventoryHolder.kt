package xyz.devcmb.invcontrol.chest.page

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.UUID

class ChestPageInventoryHolder(
    val uuid: UUID,
) : InventoryHolder {
    lateinit var inv: Inventory
    override fun getInventory(): Inventory = inv
}