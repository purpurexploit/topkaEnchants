package com.topka.enchants.listener;

import com.topka.enchants.enchants.CustomEnchantment;
import com.topka.enchants.util.BreakUtil;
import com.topka.enchants.util.ItemStackUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AnvilListener implements Listener {

    @EventHandler
    public void onPrepare(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        ItemStack tool = anvil.getItem(0);
        CustomEnchantment enchant = CustomEnchantment.getEnchant(anvil.getItem(1));
        if (enchant != null && ItemStackUtil.isItemEligibleToEnchant(tool, enchant)) {
            event.setResult(ItemStackUtil.enchantItem(tool, enchant));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory anvil) || event.getRawSlot() != 2
                || !(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        ItemStack tool = anvil.getItem(0);
        ItemStack book = anvil.getItem(1);
        CustomEnchantment enchant = CustomEnchantment.getEnchant(book);
        if (enchant == null || !ItemStackUtil.isItemEligibleToEnchant(tool, enchant)) {
            return;
        }
        // Отменяем ванильную выдачу результата — иначе предмет получался и от игры, и от плагина (дюп).
        event.setCancelled(true);

        ItemStack result = ItemStackUtil.enchantItem(tool, enchant);
        anvil.setItem(0, null);
        if (book.getAmount() > 1) {
            book.setAmount(book.getAmount() - 1);
            anvil.setItem(1, book);
        } else {
            anvil.setItem(1, null);
        }
        anvil.setItem(2, null);

        BreakUtil.give(player, List.of(result));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
    }
}
