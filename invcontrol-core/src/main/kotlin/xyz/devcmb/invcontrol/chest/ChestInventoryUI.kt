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
 * @param player The player that the UI is shown to.
 * @param title The title of the inventory UI. Defaults to "Menu"
 * @param rows The amount of rows in the inventory UI. Defaults to 3
 * @constructor Creates the inventory from the bukkit server method
 */
class ChestInventoryUI(
    val player: Player,
    var title: Component = Component.text("Menu"),
    var rows: Int = 3,
) {
    val uuid: UUID = UUID.randomUUID()
    private lateinit var inv: Inventory

    val pages: HashMap<String, ChestInventoryPage> = HashMap()
    val currentItems: HashMap<InventoryItem, ItemStack> = HashMap()
    var currentPage: ChestInventoryPage? = null

    var currentTitle: Component = title
    var currentRows: Int = rows

    init {
        if (InvControlManager.plugin == null)
            throw IllegalStateException("Cannot create an inventory UI unless the plugin is set. Use InvControlManager#setPlugin before creating UIs.")

        Registry.registerInventory(this)

        createInventory()
    }

    /**
     * Shows the attached player the inventory
     */
    fun show(player: Player?) {
        if(player == null)
            throw IllegalStateException("Cannot show an inventory ")

        if(title != currentTitle || currentRows != rows) {
            createInventory()
            currentTitle = title
            currentRows = rows
        }

        propagateItems()
        player.openInventory(inv)
    }

    fun show() = show(player)

    /**
     * Reloads the inventory view
     */
    fun reload() {
        if(title != currentTitle || currentRows != rows) {
            createInventory()
            currentTitle = title
            currentRows = rows

            player.openInventory(inv)
        }

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

        for(map in currentPage!!.itemMaps) {
            val items = map.formulateItems()
            for(item in items) {
                item.register(currentPage!!)

                val itemStack = item.formulateItemStack()
                currentItems[item] = itemStack
                inv.setItem(item.slot, itemStack)
            }
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

    /**
     * Creates the bukkit inventory instance
     */
    internal fun createInventory() {
        val holder = ChestInventoryHolder(uuid)
        val inventory = Bukkit.getServer().createInventory(
            holder,
            rows * 9,
            title
        )
        holder.inv = inventory

        inv = inventory
    }
}