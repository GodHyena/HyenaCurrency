package dev.hyenacurrency.command.money

import dev.hyenacurrency.HyenaCurrency
import dev.hyenacurrency.api.CurrencyType
import dev.hyenacurrency.util.mustBePlayer
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MoneyCommand(private val plugin: HyenaCurrency) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.mustBePlayer() ?: return true
        val uuid = player.uniqueId
        val manager = plugin.currencyManager

        if (args.isEmpty()) {
            sender.sendMessage("Usage: /money <balance|send|add|request|help>")
            return true
        }

        when (args[0].lowercase()) {
            "balance" -> {
                val balance = manager.getBalance(uuid, CurrencyType.MONEY)
                sender.sendMessage("Your Money: $balance")
            }

            "send" -> {
                if (args.size != 3) {
                    sender.sendMessage("Usage: /money send <player> <amount>")
                    return true
                }
                val target = Bukkit.getPlayer(args[1])
                val amount = args[2].toDoubleOrNull()
                if (target == null || amount == null || amount <= 0) {
                    sender.sendMessage("Invalid target or amount.")
                    return true
                }

                val targetUUID = target.uniqueId
                if (manager.getBalance(uuid, CurrencyType.MONEY) < amount) {
                    sender.sendMessage("You don't have enough money.")
                    return true
                }

                manager.subtractBalance(uuid, CurrencyType.MONEY, amount)
                manager.addBalance(targetUUID, CurrencyType.MONEY, amount)
                sender.sendMessage("Sent $amount money to ${target.name}.")
                target.sendMessage("You received $amount money from ${player.name}.")
            }

            "add" -> {
                if (!sender.isOp) {
                    sender.sendMessage("Only operators can use this command.")
                    return true
                }

                if (args.size != 3) {
                    sender.sendMessage("Usage: /money add <player> <amount>")
                    return true
                }

                val target = Bukkit.getOfflinePlayer(args[1])
                val amount = args[2].toDoubleOrNull()

                if (amount == null || amount <= 0) {
                    sender.sendMessage("Invalid amount.")
                    return true
                }

                manager.addBalance(target.uniqueId, CurrencyType.MONEY, amount)
                sender.sendMessage("Added $amount money to ${target.name}.")
            }

            "request" -> {
                if (args.size != 3) {
                    sender.sendMessage("Usage: /money request <player> <amount>")
                    return true
                }

                val target = Bukkit.getPlayer(args[1])
                val amount = args[2].toDoubleOrNull()

                if (target == null || amount == null || amount <= 0) {
                    sender.sendMessage("Invalid player or amount.")
                    return true
                }

                plugin.requestManager.sendRequest(player, target, CurrencyType.MONEY, amount)
            }

            "help" -> {
                sender.sendMessage(
                    listOf(
                        "--- /money Help ---",
                        "/money balance",
                        "/money send <player> <amount>",
                        "/money request <player> <amount>",
                        "/money add <player> <amount>",
                        "/money help"
                    ).joinToString("\n")
                )
            }

            else -> sender.sendMessage("Unknown subcommand. Use /money help")
        }

        return true
    }
}
