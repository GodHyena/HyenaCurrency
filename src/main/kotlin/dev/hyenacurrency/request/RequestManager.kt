package dev.hyenacurrency.request

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RequestManager {

    private val pendingRequests: MutableMap<UUID, Request> = ConcurrentHashMap()
    private val timeoutSeconds = 120L

    data class Request(
        val sender: UUID,
        val receiver: UUID,
        val currency: String,
        val amount: Double,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun addRequest(sender: UUID, receiver: UUID, currency: String, amount: Double) {
        pendingRequests[receiver] = Request(sender, receiver, currency, amount)

        val receiverPlayer = Bukkit.getPlayer(receiver)
        receiverPlayer?.sendMessage("§6[HyenaCurrency] §fYou have received a $currency request from ${Bukkit.getOfflinePlayer(sender).name} for $amount.")
        receiverPlayer?.sendMessage("§7Use §a/accept §7or §c/deny §7within $timeoutSeconds seconds.")

        Bukkit.getScheduler().runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("HyenaCurrency")!!, Runnable {
            if (pendingRequests[receiver]?.timestamp ?: 0 + timeoutSeconds * 1000 <= System.currentTimeMillis()) {
                pendingRequests.remove(receiver)
                receiverPlayer?.sendMessage("§c[HyenaCurrency] The $currency request has timed out.")
            }
        }, timeoutSeconds * 20)
    }

    fun getRequest(receiver: UUID): Request? {
        return pendingRequests[receiver]
    }

    fun acceptRequest(receiver: UUID): Boolean {
        val request = pendingRequests.remove(receiver) ?: return false
        val senderPlayer = Bukkit.getPlayer(request.sender)
        val receiverPlayer = Bukkit.getPlayer(request.receiver)

        // You can add your currency logic here to actually transfer the money
        senderPlayer?.sendMessage("§a[HyenaCurrency] Your request to ${receiverPlayer?.name} for ${request.amount} ${request.currency} was accepted.")
        receiverPlayer?.sendMessage("§a[HyenaCurrency] You accepted the request from ${senderPlayer?.name}.")

        return true
    }

    fun denyRequest(receiver: UUID): Boolean {
        val request = pendingRequests.remove(receiver) ?: return false
        val senderPlayer = Bukkit.getPlayer(request.sender)
        val receiverPlayer = Bukkit.getPlayer(request.receiver)

        senderPlayer?.sendMessage("§c[HyenaCurrency] Your request to ${receiverPlayer?.name} was denied.")
        receiverPlayer?.sendMessage("§c[HyenaCurrency] You denied the request from ${senderPlayer?.name}.")

        return true
    }

    fun hasRequest(receiver: UUID): Boolean {
        return pendingRequests.containsKey(receiver)
    }

    fun removeRequest(receiver: UUID) {
        pendingRequests.remove(receiver)
    }
}
