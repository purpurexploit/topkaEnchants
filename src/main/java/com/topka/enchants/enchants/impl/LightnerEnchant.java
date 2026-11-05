package com.topka.enchants.enchants.impl;

import com.topka.enchants.enchants.CustomEnchantment;
import com.topka.enchants.util.BreakUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/** Светлячок: подсвечивает игрока при ударе. */
public class LightnerEnchant implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getEntity() instanceof Player victim)) {
            return;
        }
        if (BreakUtil.isSpawnWorld(damager.getWorld())) {
            return;
        }
        if (CustomEnchantment.LIGHTNER_BOOK.isOn(damager.getInventory().getItemInMainHand())) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));
        }
    }
}
