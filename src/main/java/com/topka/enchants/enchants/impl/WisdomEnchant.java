package com.topka.enchants.enchants.impl;

import com.topka.enchants.enchants.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/** Мудрец: x2 опыта с мобов. (Раньше условие было перевёрнуто — удваивалось без зачарования.) */
public class WisdomEnchant implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null && CustomEnchantment.WISDOM_BOOK.isOn(killer.getInventory().getItemInMainHand())) {
            event.setDroppedExp(event.getDroppedExp() * 2);
        }
    }
}
