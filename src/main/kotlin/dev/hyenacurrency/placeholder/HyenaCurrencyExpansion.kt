package dev.hyenacurrency.placeholder

import dev.hyenacurrency.api.CurrencyManager
import dev.hyenacurrency.api.CurrencyType
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class HyenaCurrencyExpansion(private val manager: CurrencyManager) : PlaceholderExpansion() {

    override fun getIdentifier(): String = "hyenacurrency"
    override fun getAuthor(): String = "HyenaDev"
    override fun getVersion(): String = "1.0.0"

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {
        if (player == null || !player.isOnline) return ""

        val uuid = player.uniqueId
        val type = CurrencyType.entries.find { it.name.equals(identifier, ignoreCase = true) } ?: return null
        val balance = manager.getBalance(uuid, type)

        return balance.toString()
    }
}
