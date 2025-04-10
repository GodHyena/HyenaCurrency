package dev.hyenacurrency.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object MessageUtil {

    private lateinit var config: YamlConfiguration

    fun loadMessages(dataFolder: File) {
        val file = File(dataFolder, "messages.yml")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText(defaultMessages)
        }
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun get(key: String): String {
        return ChatColor.translateAlternateColorCodes('&', config.getString(key, "&cMessage not found: $key"))
    }

    fun format(key: String, vararg args: Pair<String, Any>): String {
        var message = get(key)
        args.forEach { (placeholder, value) ->
            message = message.replace("{$placeholder}", value.toString())
        }
        return message
    }

    private val defaultMessages = """
        prefix: '&6[HyenaCurrency] '
        balance: '&aYour balance: &f{amount} {currency}'
        sent: '&aYou sent &f{amount} {currency} &ato &f{target}'
        received: '&aYou received &f{amount} {currency} &afrom &f{sender}'
        requestSent: '&eYou requested &f{amount} {currency} &efrom &f{target}'
        requestReceived: '&e{sender} is requesting &f{amount} {currency}&e from you. Use /accept or /deny.'
        cooldown: '&cYou must wait {seconds}s before doing that again.'
        limitReached: '&cYou reached your daily limit for {currency}.'
        notEnough: '&cYou don''t have enough {currency}.'
        unknownPlayer: '&cThat player could not be found.'
        invalidAmount: '&cThat amount is invalid.'
    """.trimIndent()
}
