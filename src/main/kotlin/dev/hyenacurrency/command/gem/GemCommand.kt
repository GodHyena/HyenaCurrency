package dev.hyenacurrency.command.gem

import dev.hyenacurrency.HyenaCurrency
import dev.hyenacurrency.api.CurrencyType
import dev.hyenacurrency.util.mustBePlayer
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class GemCommand(private val plugin: HyenaCurrency) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.mustBePlayer() ?: return true
        val uuid = player.uniqueId
        val manager = plugin.currencyManager

        if (args.isEmpty()) {
            sender.sendMessage("Usage: /gem <balance|send|add|request|help>")
            return true
        }

        when (args[0].lowercase()) {
            "balance" -> {
                val balance = manager.getBalance(uuid, CurrencyType.GEM)
                sender.sendMessage("Your Gems: $balance")
            }

            "send" -> {
                if (args.size != 3) {
                    sender.sendMessage("Usage: /gem send <player> <amount>")
                    return true
                }
                val target = Bukkit.getPlayer(args[1])
                val amount = args[2].toDoubleOrNull()
                if (target == null || amount == null || amount <= 0) {
                    sender.sendMessage("Invalid target or amount.")
                    return true
                }

                val targetUUID = target.uniqueId
                if (manager.getBalance(uuid, CurrencyType.GEM) < amount) {
                    sender.sendMessage("You don't have enough Gems.")
                    return true
                }

                manager.subtractBalance(uuid, CurrencyType.GEM, amount)
                manager.addBalance(targetUUID, CurrencyType.GEM, amount)
                sender.sendMessage("Sent $amount Gems to ${target.name}.")
                target.sendMessage("You received $amount Gems from ${player.name}.")
            }

            "add" -> {
                if (!sender.isOp) {
                    sender.sendMessage("Only operators can use this command.")
                    return true
                }

                if (args.size != 3) {
                    sender.sendMessage("Usage: /gem add <player> <amount>")
                    return true
                }

                val target = Bukkit.getOfflinePlayer(args[1])
                val amount = args[2].toDoubleOrNull()

                if (amount == null || amount <= 0) {
                    sender.sendMessage("Invalid amount.")
                    return true
                }

                manager.addBalance(target.uniqueId, CurrencyType.GEM, amount)
                sender.sendMessage("Added $amount Gems to ${target.name}.")
            }

            "request" -> {
                if (args.size != 3) {
                    sender.sendMessage("Usage: /gem request <player> <amount>")
                    return true
                }

                val target = Bukkit.getPlayer(args[1])
                val amount = args[2].toDoubleOrNull()

                if (target == null || amount == null || amount <= 0) {
                    sender.sendMessage("Invalid player or amount.")
                    return true
                }

                plugin.requestManager.sendRequest(player, target, CurrencyType.GEM, amount)
            }

            "help" -> {
                sender.sendMessage(
                    listOf(
                        "--- /gem Help ---",
                        "/gem balance",
                        "/gem send <player> <amount>",
                        "/gem request <player> <amount>",
                        "/gem add <player> <amount>",
                        "/gem help"
                    ).joinToString("\n")
                )
            }

            else -> sender.sendMessage("Unknown subcommand. Use /gem help")
        }

        return true
    }
}
