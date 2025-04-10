package dev.hyenacurrency.api

import dev.hyenacurrency.HyenaCurrency
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.UUID

class FlatFileStorageProvider(private val plugin: HyenaCurrency) : StorageProvider {

    private fun getPlayerFile(uuid: UUID): File {
        val dir = File(plugin.dataFolder, "data")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "$uuid.yml")
    }

    override fun load(uuid: UUID): CurrencyData {
        val file = getPlayerFile(uuid)
        if (!file.exists()) return CurrencyData()

        val config = YamlConfiguration.loadConfiguration(file)
        return CurrencyData(
            config.getDouble("money", 0.0),
            config.getDouble("mobcoin", 0.0),
            config.getDouble("gems", 0.0)
        )
    }

    override fun save(uuid: UUID, data: CurrencyData) {
        val file = getPlayerFile(uuid)
        val config = YamlConfiguration.loadConfiguration(file)

        config.set("money", data.money)
        config.set("mobcoin", data.mobCoin)
        config.set("gems", data.gems)

        config.save(file)
    }
}
