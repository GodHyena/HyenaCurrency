package dev.hyenacurrency.command

import dev.hyenacurrency.HyenaCurrency
import dev.hyenacurrency.api.CurrencyType
import dev.hyenacurrency.util.mustBePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class EconomyCommand(private val plugin: HyenaCurrency) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.mustBePlayer() ?: return true
        if (args.size != 3) {
            sender.sendMessage("Usage: /economy <from> <to> <amount>")
            return true
        }

        val from = args[0].uppercase()
        val to = args[1].uppercase()
        val amount = args[2].toDoubleOrNull()

        if (amount == null || amount <= 0) {
            sender.sendMessage("Invalid amount.")
            return true
        }

        val fromType = runCatching { CurrencyType.valueOf(from) }.getOrNull()
        val toType = runCatching { CurrencyType.valueOf(to) }.getOrNull()

        if (fromType == null || toType == null) {
            sender.sendMessage("Invalid currency type. Use MONEY, MOBCOIN, GEM.")
            return true
        }

        val manager = plugin.currencyManager
        val uuid = player.uniqueId

        if (manager.getBalance(uuid, fromType) < amount) {
            sender.sendMessage("You don't have enough $from.")
            return true
        }

        // Optionally apply conversion rate here if you want
        manager.subtractBalance(uuid, fromType, amount)
        manager.addBalance(uuid, toType, amount)
        sender.sendMessage("Converted $amount from $from to $to.")
        return true
    }
}
