package xyz.devcmb.invcontrol.chest

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import xyz.devcmb.invcontrol.Registry
import xyz.devcmb.invcontrol.common.AbstractInventoryUI
import java.util.UUID

/**
 * The base for chest inventory menus
 * @property player The player that the UI is shown to.
 * @property title The title of the inventory UI. Defaults to "Inventory"
 * @property rows The amount of rows in the inventory UI. Defaults to 3
 * @constructor Creates the inventory from the bukkit server method
 */
class ChestInventoryUI(
    override val player: Player,
    title: Component = Component.text("Inventory"),
    val rows: Int = 3
) : AbstractInventoryUI(player) {
    val uuid: UUID = UUID.randomUUID()
    private val inv: Inventory

    val pages: HashMap<String, ChestInventoryPage> = HashMap()
    val currentItems: HashMap<InventoryItem, ItemStack> = HashMap()
    var currentPage: ChestInventoryPage? = null

    init {
        Registry.registerInventory(this)

        val holder = ChestInventoryHolder(uuid)
        inv = Bukkit.getServer().createInventory(
            holder,
            rows * 9,
            title
        )
        holder.inventory = inv
    }

    /**
     * Shows the attached player the inventory
     */
    override fun show() {
        propagateItems()
        player.openInventory(inv)
    }

    /**
     * Reloads the inventory view
     */
    override fun reload() {
        propagateItems()
    }

    /**
     * Fill the page with the current items
     */
    private fun propagateItems() {
        inv.clear()
        currentItems.clear()
        if(currentPage == null) return

        for(item in currentPage!!.items) {
            val itemStack = item.formulateItemStack()
            currentItems[item] = itemStack
            inv.setItem(item.slot, itemStack)
        }
    }

    /**
     * Registers a page to be set
     * @param id The identifier of the page
     * @param page The page class to put into the pages map
     */
    fun addPage(id: String, page: ChestInventoryPage) {
        pages[id] = page
    }

    /**
     * Sets the active page
     */
    fun setPage(id: String) {
        if (pages.containsKey(id)) {
            throw IllegalArgumentException("Page with ID $id does not exist or is not registered")
        }

        currentPage = pages[id]
        reload()
    }
}