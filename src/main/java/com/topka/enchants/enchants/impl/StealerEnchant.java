package com.topka.enchants.enchants.impl;

import com.topka.enchants.TopkaEnchants;
import com.topka.enchants.enchants.CustomEnchantment;
import com.topka.enchants.hook.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/** Грабитель: забирает часть денег убитого игрока. */
public class StealerEnchant implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null || killer.equals(victim)
                || !CustomEnchantment.STEALER_BOOK.isOn(killer.getInventory().getItemInMainHand())) {
            return;
        }
        Economy economy = VaultHook.getEconomy();
        if (economy == null) {
            return;
        }
        FileConfiguration cfg = TopkaEnchants.getInstance().getConfig();
        double percent = cfg.getInt("stealerPercent", 50) / 100.0;
        double max = cfg.getDouble("stealerMaxMoney", 45000);
        double money = Math.min(economy.getBalance(victim) * percent, max);
        if (money <= 0) {
            return;
        }
        economy.withdrawPlayer(victim, money);
        economy.depositPlayer(killer, money);
        String amount = Integer.toString((int) money);
        killer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                cfg.getString("stealerKillMessage", "")).replace("%money%", amount));
        victim.sendMessage(ChatColor.translateAlternateColorCodes('&',
                cfg.getString("stealerVictimMessage", "")).replace("%money%", amount));
    }
}
