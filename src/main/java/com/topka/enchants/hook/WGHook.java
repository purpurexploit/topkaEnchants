package com.topka.enchants.hook;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/** Безопасная обёртка над WorldGuard: если его нет — плагин работает без проверок регионов. */
public final class WGHook {
    private static boolean enabled;

    private WGHook() {}

    public static void init(Plugin plugin) {
        enabled = false;
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            plugin.getLogger().info("WorldGuard не найден — проверка регионов отключена.");
            return;
        }
        try {
            WGAdapter.probe();
            enabled = true;
            plugin.getLogger().info("WorldGuard найден — проверка регионов включена.");
        } catch (Throwable t) {
            plugin.getLogger().warning("WorldGuard найден, но не удалось подключиться: " + t);
        }
    }

    /** Может ли игрок ломать блок в этой точке. */
    public static boolean canBreak(Player player, Location location) {
        if (!enabled) {
            return true;
        }
        try {
            return WGAdapter.canBuild(player, location);
        } catch (Throwable t) {
            return false;
        }
    }
}
