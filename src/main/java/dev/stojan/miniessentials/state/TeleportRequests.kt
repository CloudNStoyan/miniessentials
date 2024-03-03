package dev.stojan.miniessentials.state

import java.time.Instant
import java.util.*
import kotlin.collections.HashMap

data object TeleportRequests {
    private val teleportRequests: HashMap<UUID, TeleportRequestInfo> = HashMap()

    fun addTeleportRequest(toPlayerId: UUID, fromPlayerId: UUID) {
        teleportRequests[toPlayerId] = TeleportRequestInfo(fromPlayerId)
    }

    fun getTeleportRequest(playerId: UUID): TeleportRequestInfo? {
        return teleportRequests[playerId];
    }

    fun clearTeleportRequestBySender(playerId: UUID): Array<UUID> {
        val playerIds = mutableListOf<UUID>();
        val allTeleportRequests = teleportRequests.entries;

        for (request in allTeleportRequests) {
            if (request.value.playerId == playerId) {
                teleportRequests.remove(request.key)
                playerIds.add(request.key)
            }
        }

        return playerIds.toTypedArray();
    }

    fun clearTeleportRequest(playerId: UUID) {
        teleportRequests.remove(playerId)
    }
}

class TeleportRequestInfo(playerUuid: UUID) {
    val playerId: UUID = playerUuid
    val requestedAt: Instant = Instant.now()
}