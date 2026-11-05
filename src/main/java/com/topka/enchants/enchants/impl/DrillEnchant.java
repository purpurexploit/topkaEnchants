package com.topka.enchants.enchants.impl;

import com.topka.enchants.enchants.CustomEnchantment;
import com.topka.enchants.hook.WGHook;
import com.topka.enchants.util.BreakUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/** Бур: ломает 3x3x3. */
public class DrillEnchant implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!CustomEnchantment.DRILL_BOOK.isOn(tool) || BreakUtil.isSpawnWorld(player.getWorld())) {
            return;
        }
        boolean telepathy = CustomEnchantment.TELEPATION_BOOK.isOn(tool) && player.getGameMode() != GameMode.CREATIVE;
        Block center = event.getBlock();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue; // центральный блок ломает сама игра
                    }
                    Block block = center.getRelative(x, y, z);
                    Material type = block.getType();
                    if (type.isAir() || block.isLiquid() || type.getHardness() < 0
                            || type == Material.BEDROCK || block.getState() instanceof Container) {
                        continue;
                    }
                    if (!WGHook.canBreak(player, block.getLocation())) {
                        continue;
                    }
                    BreakUtil.breakExtra(block, player, tool, telepathy);
                }
            }
        }
    }
}
