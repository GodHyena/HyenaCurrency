package dev.hyenacurrency.api

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object RankUtils {

    fun getRank(player: OfflinePlayer): String {
        // Fallback to "default" if no permission plugin is found
        val perms = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission::class.java)?.provider
        return perms?.getPrimaryGroup(player) ?: "default"
    }

    fun getTaxPercentage(rank: String): Double {
        return when (rank.lowercase()) {
            "default" -> 5.0
            "vip" -> 3.0
            "mvp" -> 1.5
            "elite" -> 0.5
            else -> 5.0
        }
    }

    fun getCooldownSeconds(rank: String): Long {
        return when (rank.lowercase()) {
            "default" -> 60
            "vip" -> 45
            "mvp" -> 30
            "elite" -> 10
            else -> 60
        }
    }

    fun getDailyTransferLimit(rank: String): Int {
        return when (rank.lowercase()) {
            "default" -> 5
            "vip" -> 10
            "mvp" -> 20
            "elite" -> 50
            else -> 5
        }
    }
}
