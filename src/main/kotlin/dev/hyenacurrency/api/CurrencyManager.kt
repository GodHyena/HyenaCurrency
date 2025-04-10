package dev.hyenacurrency.api

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CurrencyManager(private val storage: StorageProvider) {

    private val balances = ConcurrentHashMap<UUID, CurrencyData>()

    fun getBalance(uuid: UUID, type: CurrencyType): Double {
        return getData(uuid).getBalance(type)
    }

    fun setBalance(uuid: UUID, type: CurrencyType, amount: Double) {
        getData(uuid).setBalance(type, amount)
        save(uuid)
    }

    fun addBalance(uuid: UUID, type: CurrencyType, amount: Double) {
        getData(uuid).addBalance(type, amount)
        save(uuid)
    }

    fun subtractBalance(uuid: UUID, type: CurrencyType, amount: Double) {
        getData(uuid).subtractBalance(type, amount)
        save(uuid)
    }

    fun getData(uuid: UUID): CurrencyData {
        return balances.computeIfAbsent(uuid) { storage.load(it) }
    }

    fun save(uuid: UUID) {
        balances[uuid]?.let { storage.save(uuid, it) }
    }
}
