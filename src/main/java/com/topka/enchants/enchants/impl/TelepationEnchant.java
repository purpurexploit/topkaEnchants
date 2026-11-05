package com.topka.enchants.enchants.impl;

import com.topka.enchants.TopkaEnchants;
import com.topka.enchants.enchants.CustomEnchantment;
import com.topka.enchants.util.BreakUtil;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/** Телепатия: дропы сразу в инвентарь. */
public class TelepationEnchant implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!CustomEnchantment.TELEPATION_BOOK.isOn(tool) || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        Block block = event.getBlock();
        if (BreakUtil.isSpawnWorld(block.getWorld()) || block.getType().isAir()) {
            return;
        }

        if (block.getType().name().endsWith("SHULKER_BOX")) {
            String msg = TopkaEnchants.getInstance().getConfig().getString("cantBreakShulker");
            if (msg != null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
            event.setCancelled(true);
            return;
        }

        // Реальные дропы блока (с учётом инструмента, удачи и т.д.)
        Collection<ItemStack> drops = BreakUtil.getDrops(block, player, tool);
        // Ванильные дропы выключаем...
        event.setDropItems(false);
        // ...и убираем вторую половину двери/кровати/высокой травы БЕЗ физики,
        // иначе игра сама выбросит её ещё раз (дюп при ломании верхней части двери).
        BreakUtil.removePartnerSilently(block);
        BreakUtil.give(player, drops);
    }
}
