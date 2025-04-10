package dev.hyenacurrency.api

import java.util.UUID

interface StorageProvider {
    fun load(uuid: UUID): CurrencyData
    fun save(uuid: UUID, data: CurrencyData)
}
