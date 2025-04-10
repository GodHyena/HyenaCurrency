package dev.hyenacurrency.limit

import dev.hyenacurrency.api.RankUtils
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class TransferLimits {

    private val transferCounts: MutableMap<UUID, TransferInfo> = ConcurrentHashMap()

    data class TransferInfo(var count: Int, var lastReset: LocalDate)

    fun canTransfer(uuid: UUID, rank: String): Boolean {
        val today = LocalDate.now()
        val info = transferCounts.computeIfAbsent(uuid) {
            TransferInfo(0, today)
        }

        if (info.lastReset != today) {
            info.count = 0
            info.lastReset = today
        }

        val limit = RankUtils.getDailyTransferLimit(rank)
        return info.count < limit
    }

    fun incrementTransfers(uuid: UUID) {
        val today = LocalDate.now()
        val info = transferCounts.computeIfAbsent(uuid) {
            TransferInfo(0, today)
        }

        if (info.lastReset != today) {
            info.count = 0
            info.lastReset = today
        }

        info.count++
    }

    fun getRemainingTransfers(uuid: UUID, rank: String): Int {
        val today = LocalDate.now()
        val info = transferCounts.computeIfAbsent(uuid) {
            TransferInfo(0, today)
        }

        if (info.lastReset != today) {
            info.count = 0
            info.lastReset = today
        }

        return RankUtils.getDailyTransferLimit(rank) - info.count
    }

    fun resetTransfers(uuid: UUID) {
        transferCounts[uuid]?.count = 0
        transferCounts[uuid]?.lastReset = LocalDate.now()
    }
}
