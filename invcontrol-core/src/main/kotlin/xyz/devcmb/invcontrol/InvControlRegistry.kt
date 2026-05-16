package xyz.devcmb.invcontrol

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.devcmb.invcontrol.chest.ChestInventory
import xyz.devcmb.invcontrol.chest.page.ChestPageInventoryHolder
import java.util.UUID

object InvControlRegistry {
    val inventories: ArrayList<ChestInventory> = ArrayList()
    fun register(inventory: ChestInventory) {
        inventories.add(inventory)
    }

    val lastButtonClick: HashMap<UUID, Long> = HashMap()
    fun click(inventory: Inventory, item: ItemStack, isRightClick: Boolean) {
        val holder = inventory.holder as? ChestPageInventoryHolder ?: return
        val page = inventories
            .firstNotNullOfOrNull { chest ->
                chest.pages.find { it.uuid == holder.uuid }
            } ?: return

        val itemId = UUID.fromString(item.itemMeta.persistentDataContainer.get(
            InvControlManager.INV_CONTROL_ITEM_ID,
            PersistentDataType.STRING
        ))

        if((lastButtonClick[itemId] ?: 0) + 150 > System.currentTimeMillis()) return

        val item = page.context.items.find { it.id == itemId } ?: return
        lastButtonClick[itemId] = System.currentTimeMillis()
        item.context.clickHandlers.forEach { it.invoke() }
    }
}