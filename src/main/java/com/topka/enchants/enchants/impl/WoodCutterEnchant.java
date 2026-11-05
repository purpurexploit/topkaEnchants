package com.topka.enchants.enchants.impl;

import com.topka.enchants.enchants.CustomEnchantment;
import com.topka.enchants.hook.WGHook;
import com.topka.enchants.util.BreakUtil;
import com.topka.enchants.util.TreeUtil;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/** Лесоруб: валит всё дерево. */
public class WoodCutterEnchant implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block origin = event.getBlock();
        if (!CustomEnchantment.WOODCUTTER_BOOK.isOn(tool) || !origin.getType().name().endsWith("_LOG")
                || BreakUtil.isSpawnWorld(origin.getWorld())) {
            return;
        }
        boolean telepathy = CustomEnchantment.TELEPATION_BOOK.isOn(tool) && player.getGameMode() != GameMode.CREATIVE;

        for (Block block : TreeUtil.getTreeBlocks(origin)) {
            if (block.equals(origin) || !WGHook.canBreak(player, block.getLocation())) {
                continue;
            }
            BreakUtil.breakExtra(block, player, tool, telepathy);
        }
    }
}
