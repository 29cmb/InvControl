package xyz.devcmb.invcontrol.chest.map

import org.bukkit.inventory.ItemStack
import xyz.devcmb.invcontrol.chest.ChestInventoryPage
import xyz.devcmb.invcontrol.chest.InventoryItem

/**
 * An extension of the [InventoryItem] class just without a slot field
 * @property getItemStack The method invoked to get the [org.bukkit.inventory.ItemStack] whenever the inventory opens or is reloaded
 * @property onClick The method that is invoked whenever the element is clicked
 */
open class InventoryMappedItem(
    override var getItemStack: (page: ChestInventoryPage, item: InventoryItem) -> ItemStack,
    override var onClick: (page: ChestInventoryPage, item: InventoryItem) -> Unit = { page, item -> },
    override var onRightClick: (page: ChestInventoryPage, item: InventoryItem) -> Unit = { page, item -> }
) : InventoryItem(getItemStack, -1, onClick)