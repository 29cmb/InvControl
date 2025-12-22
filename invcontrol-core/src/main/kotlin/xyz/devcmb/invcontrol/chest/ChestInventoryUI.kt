package xyz.devcmb.invcontrol.chest

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import xyz.devcmb.invcontrol.InvControlManager
import xyz.devcmb.invcontrol.Registry
import java.util.UUID

/**
 * The base for chest inventory menus
 * @property player The player that the UI is shown to.
 * @property title The title of the inventory UI. Defaults to "Menu"
 * @property rows The amount of rows in the inventory UI. Defaults to 3
 * @constructor Creates the inventory from the bukkit server method
 */
class ChestInventoryUI(
    val player: Player,
    val title: Component = Component.text("Menu"),
    val rows: Int = 3
) {
    val uuid: UUID = UUID.randomUUID()
    private val inv: Inventory

    val pages: HashMap<String, ChestInventoryPage> = HashMap()
    val currentItems: HashMap<InventoryItem, ItemStack> = HashMap()
    var currentPage: ChestInventoryPage? = null

    init {
        if (InvControlManager.plugin == null) {
            throw IllegalStateException("Cannot create an inventory UI unless the plugin is set. Use InvControlManager#setPlugin before creating UIs.")
        }

        Registry.registerInventory(this)

        val holder = ChestInventoryHolder(uuid)
        inv = Bukkit.getServer().createInventory(
            holder,
            rows * 9,
            title
        )
        holder.inv = inv
    }

    /**
     * Shows the attached player the inventory
     */
    fun show() {
        propagateItems()
        player.openInventory(inv)
    }

    /**
     * Reloads the inventory view
     */
    fun reload() {
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
            InvControlManager.plugin?.logger?.info(itemStack.type.toString())
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
        page.register(this)
    }

    /**
     * Sets the active page
     */
    fun setPage(id: String) {
        if (!pages.containsKey(id)) {
            throw IllegalArgumentException("Page with ID $id does not exist or is not registered")
        }

        currentPage = pages[id]
        reload()
    }
}