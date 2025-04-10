package dev.hyenacurrency.listener

import dev.hyenacurrency.api.CurrencyManager
import dev.hyenacurrency.api.CurrencyType
import dev.hyenacurrency.api.RankUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class MobKillListener(
    private val currencyManager: CurrencyManager
) : Listener {

    private val mobRewards = mapOf(
        "ZOMBIE" to 5.0,
        "SKELETON" to 6.0,
        "CREEPER" to 8.0,
        "SPIDER" to 4.0,
        "ENDERMAN" to 15.0,
        "WITHER" to 100.0,
        "ENDER_DRAGON" to 250.0
    )

    @EventHandler
    fun onMobKill(event: EntityDeathEvent) {
        val killer = event.entity.killer ?: return
        if (killer !is Player) return

        val entityType = event.entity.type.name
        val baseReward = mobRewards[entityType] ?: return

        val bonusMultiplier = RankUtils.getRankBonus(killer)
        val totalReward = baseReward * bonusMultiplier

        currencyManager.addBalance(killer.uniqueId, CurrencyType.MOBCOIN, totalReward)
        killer.sendMessage("§6[HyenaCurrency] §aYou earned §f$totalReward MobCoins §afor killing a §f$entityType.")
    }
}
