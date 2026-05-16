package xyz.devcmb.invcontrol.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.devcmb.invcontrol.InvControlManager
import java.util.UUID

class StaticItem(val buildStaticItem: ItemContext.() -> Unit) : Item {
    override val id: UUID = UUID.randomUUID()
    override lateinit var context: ItemContext

    override fun build(player: Player): ItemStack {
        context = ItemContext(this, player)
        buildStaticItem(context)

        val stack = context.build()
        stack.editMeta {
            it.persistentDataContainer.set(
                InvControlManager.INV_CONTROL_ITEM_ID,
                PersistentDataType.STRING,
                id.toString()
            )
        }

        return stack
    }

    override fun shouldRefresh(player: Player): Boolean = false
}