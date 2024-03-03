package dev.stojan.miniessentials.shared

import org.bukkit.NamespacedKey

object CustomNamespacedKeys {
    private const val NAMESPACE = "stojan-namespace";
    val BackCoords = NamespacedKey(NAMESPACE, "back_coords")
    val BackWorldId = NamespacedKey(NAMESPACE, "back_world_id")
    val HomeCoords = NamespacedKey(NAMESPACE, "home_coords")
    val HomeWorldId = NamespacedKey(NAMESPACE, "home_world_id")
    val FlyState = NamespacedKey(NAMESPACE, "fly_state")
}