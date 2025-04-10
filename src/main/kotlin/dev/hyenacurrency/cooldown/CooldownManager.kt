package dev.hyenacurrency.cooldown

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CooldownManager {

    private val cooldowns: MutableMap<UUID, MutableMap<String, Long>> = ConcurrentHashMap()

    fun isOnCooldown(uuid: UUID, key: String): Boolean {
        val playerCooldowns = cooldowns[uuid] ?: return false
        val expiry = playerCooldowns[key] ?: return false
        return System.currentTimeMillis() < expiry
    }

    fun getRemainingSeconds(uuid: UUID, key: String): Long {
        val playerCooldowns = cooldowns[uuid] ?: return 0
        val expiry = playerCooldowns[key] ?: return 0
        val remaining = expiry - System.currentTimeMillis()
        return if (remaining > 0) remaining / 1000 else 0
    }

    fun setCooldown(uuid: UUID, key: String, seconds: Long) {
        cooldowns.computeIfAbsent(uuid) { HashMap() }[key] = System.currentTimeMillis() + (seconds * 1000)
    }

    fun clearCooldown(uuid: UUID, key: String) {
        cooldowns[uuid]?.remove(key)
    }

    fun clearAllCooldowns(uuid: UUID) {
        cooldowns.remove(uuid)
    }
}
