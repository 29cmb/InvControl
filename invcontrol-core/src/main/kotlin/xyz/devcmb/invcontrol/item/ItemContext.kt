package xyz.devcmb.invcontrol.item

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer

class ItemContext internal constructor(val item: Item, val player: Player) {
    var type: Material = Material.AIR
    var slot: Int = 0
    var amount: Int = 1
    var meta: ItemMeta? = null

    val clickHandlers: ArrayList<() -> Unit> = ArrayList()
    val customConfigurators: ArrayList<(stack: ItemStack) -> Unit> = ArrayList()

    internal fun build(): ItemStack {
        val stack = ItemStack(type)
        stack.itemMeta = meta
        customConfigurators.forEach { it.invoke(stack) }
        return stack
    }

    fun onClick(handler: () -> Unit) {
        clickHandlers.add(handler)
    }

    fun name(component: Component) {
        createIfAbsent()
        meta = meta.also { it!!.itemName(component) }
    }

    fun model(key: NamespacedKey) {
        meta = meta.also {
            it!!.itemModel = key
        }
    }

    fun lore(components: List<Component>) {
        createIfAbsent()
        meta = meta.also { it!!.lore(components) }
    }

    fun persistentDataContainer(action: PersistentDataContainer.() -> Unit) {
        createIfAbsent()
        meta = meta.also {
            action(it!!.persistentDataContainer)
        }
    }

    fun unbreakable(unbreakable: Boolean) {
        createIfAbsent()
        meta = meta.also {
            it!!.isUnbreakable = unbreakable
        }
    }

    fun enchants(enchantments: Map<Enchantment, Int>) {
        createIfAbsent()
        meta = meta.also {
            enchantments.forEach { enchant ->
                it!!.addEnchant(enchant.key, enchant.value, true)
            }
        }
    }

    fun configurator(action: (stack: ItemStack) -> Unit) {
        customConfigurators.add(action)
    }

    private fun createIfAbsent() {
        if(meta == null) meta = Bukkit.getItemFactory().getItemMeta(type)
    }
}