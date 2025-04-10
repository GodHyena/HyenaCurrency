package dev.hyenacurrency.listener

import dev.hyenacurrency.api.CurrencyManager
import dev.hyenacurrency.api.CurrencyType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerKillListener(private val currencyManager: CurrencyManager) : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.entity
        val killer = victim.killer ?: return

        if (killer == victim || killer !is Player || victim !is Player) return

        CurrencyType.entries.forEach { currency ->
            val amount = currencyManager.getBalance(victim.uniqueId, currency)
            if (amount > 0) {
                currencyManager.removeBalance(victim.uniqueId, currency, amount)
                currencyManager.addBalance(killer.uniqueId, currency, amount)

                killer.sendMessage("§6[HyenaCurrency] §aYou stole §f$amount ${currency.name} §afrom §f${victim.name}.")
                victim.sendMessage("§6[HyenaCurrency] §cYou lost §f$amount ${currency.name} §cto §f${killer.name}.")
            }
        }
    }
}
