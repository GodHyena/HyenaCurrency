package dev.hyenacurrency.util

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CommandUtils {

    fun getOfflinePlayer(name: String): OfflinePlayer? {
        return Bukkit.getOfflinePlayerIfCached(name) ?: Bukkit.getOfflinePlayer(name)
    }

    fun isNumeric(input: String?): Boolean {
        return input?.toDoubleOrNull() != null
    }

    fun parseAmount(input: String?): Double? {
        return if (isNumeric(input)) input!!.toDouble() else null
    }

    fun hasPermission(sender: CommandSender, permission: String): Boolean {
        return sender.hasPermission(permission) || sender.isOp
    }

    fun sendNoPermission(sender: CommandSender) {
        sender.sendMessage("§cYou do not have permission to use this command.")
    }

    fun sendInvalidUsage(sender: CommandSender, usage: String) {
        sender.sendMessage("§cInvalid usage. Try: §f$usage")
    }
}
