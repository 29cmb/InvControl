package xyz.devcmb.invcontrol.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID

interface Item {
    val id: UUID
    var context: ItemContext

    fun build(player: Player): ItemStack
    fun shouldRefresh(player: Player): Boolean
}