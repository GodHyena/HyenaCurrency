package dev.hyenacurrency

import dev.hyenacurrency.api.CurrencyManager
import dev.hyenacurrency.api.FlatFileStorageProvider
import dev.hyenacurrency.command.EconomyCommand
import dev.hyenacurrency.command.gem.GemCommand
import dev.hyenacurrency.command.mobcoin.MobCoinCommand
import dev.hyenacurrency.command.money.MoneyCommand
import dev.hyenacurrency.cooldown.CooldownManager
import dev.hyenacurrency.limit.TransferLimits
import dev.hyenacurrency.listener.MobKillListener
import dev.hyenacurrency.listener.PlayerKillListener
import dev.hyenacurrency.placeholder.HyenaCurrencyExpansion
import dev.hyenacurrency.request.RequestManager
import dev.hyenacurrency.util.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class HyenaCurrency : JavaPlugin() {

    companion object {
        lateinit var instance: HyenaCurrency
            private set

        lateinit var currencyManager: CurrencyManager
            private set

        lateinit var cooldownManager: CooldownManager
            private set

        lateinit var requestManager: RequestManager
            private set

        lateinit var transferLimits: TransferLimits
            private set
    }

    override fun onEnable() {
        instance = this

        // Save default configs if not exist
        saveDefaultConfig()
        saveResource("messages.yml", false)

        // Initialize messages
        MessageUtil.loadMessages()

        // Initialize core systems
        currencyManager = CurrencyManager(FlatFileStorageProvider())
        cooldownManager = CooldownManager()
        requestManager = RequestManager()
        transferLimits = TransferLimits()

        // Register commands
        getCommand("money")?.setExecutor(MoneyCommand())
        getCommand("mobcoin")?.setExecutor(MobCoinCommand())
        getCommand("gem")?.setExecutor(GemCommand())
        getCommand("economy")?.setExecutor(EconomyCommand())

        // Register event listeners
        server.pluginManager.registerEvents(PlayerKillListener(), this)
        server.pluginManager.registerEvents(MobKillListener(), this)

        // Register PlaceholderAPI expansion if PAPI is present
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            HyenaCurrencyExpansion(currencyManager).register()
            logger.info("PlaceholderAPI expansion registered.")
        } else {
            logger.warning("PlaceholderAPI not found. Placeholders will not be available.")
        }

        logger.info("HyenaCurrency has been enabled.")
    }

    override fun onDisable() {
        logger.info("HyenaCurrency has been disabled.")
    }
}
