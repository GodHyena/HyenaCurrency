package dev.hyenacurrency.command.mobcoin

import dev.hyenacurrency.HyenaCurrency
import dev.hyenacurrency.api.CurrencyType
import dev.hyenacurrency.util.mustBePlayer
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MobCoinCommand(private val plugin: HyenaCurrency) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        val player = sender.mustBePlayer() ?: return true
        val uuid = player.uniqueId
        val manager = plugin.currencyManager

        if (args.isEmpty()) {
            sender.sendMessage("Usage: /mobcoin <balance|send|add|request|help>")
            return true
        }

        when (args[0].lowercase()) {
            "balance" -> {
                val balance = manager.getBalance(uuid, CurrencyType.MOBCOIN)
                sender.sendMessage("Your MobCoins: $balance")
            }

            "send" -> {
                if (args.size != 3) {
                    sender.sendMessage("Usage: /mobcoin send <player> <amount>")
                    return true
                }
                val target = Bukkit.getPlayer(args[1])
                val amount = args[2].toDoubleOrNull()
                if (target == null || amount == null || amount <= 0) {
                    sender.sendMessage("Invalid target or amount.")
                    return true
                }

                val targetUUID = target.uniqueId
                if (manager.getBalance(uuid, CurrencyType.MOBCOIN) < amount) {
                    sender.sendMessage("You don't have enough MobCoins.")
                    return true
                }

                manager.subtractBalance(uuid, CurrencyType.MOBCOIN, amount)
                manager.addBalance(targetUUID, CurrencyType.MOBCOIN, amount)
                sender.sendMessage("Sent $amount MobCoins to ${target.name}.")
                target.sendMessage("You received $amount MobCoins from ${player.name}.")
            }

            "add" -> {
                if (!sender.isOp) {
                    sender.sendMessage("Only operators can use this command.")
                    return true
                }

                if (args.size != 3) {
                    sender.sendMessage("Usage: /mobcoin add <player> <amount>")
                    return true
                }

                val target = Bukkit.getOfflinePlayer(args[1])
                val amount = args[2].toDoubleOrNull()

                if (amount == null || amount <= 0) {
                    sender.sendMessage("Invalid amount.")
                    return true
                }

                manager.addBalance(target.uniqueId, CurrencyType.MOBCOIN, amount)
                sender.sendMessage("Added $amount MobCoins to ${target.name}.")
            }

            "request" -> {
                if (args.size != 3) {
                    sender.sendMessage("Usage: /mobcoin request <player> <amount>")
                    return true
                }

                val target = Bukkit.getPlayer(args[1])
                val amount = args[2].toDoubleOrNull()

                if (target == null || amount == null || amount <= 0) {
                    sender.sendMessage("Invalid player or amount.")
                    return true
                }

                plugin.requestManager.sendRequest(player, target, CurrencyType.MOBCOIN, amount)
            }

            "help" -> {
                sender.sendMessage(
                    listOf(
                        "--- /mobcoin Help ---",
                        "/mobcoin balance",
                        "/mobcoin send <player> <amount>",
                        "/mobcoin request <player> <amount>",
                        "/mobcoin add <player> <amount>",
                        "/mobcoin help"
                    ).joinToString("\n")
                )
            }

            else -> sender.sendMessage("Unknown subcommand. Use /mobcoin help")
        }

        return true
    }
}
