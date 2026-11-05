package com.topka.enchants.util;

import com.topka.enchants.TopkaEnchants;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class BreakUtil {
    private BreakUtil() {}

    public static boolean isSpawnWorld(World world) {
        String spawn = TopkaEnchants.getInstance().getConfig().getString("spawnWorld");
        return spawn != null && world.getName().equalsIgnoreCase(spawn);
    }

    private static boolean isDoubleBlock(Material type) {
        String n = type.name();
        return n.endsWith("_DOOR") || n.equals("TALL_GRASS") || n.equals("LARGE_FERN")
                || n.equals("SUNFLOWER") || n.equals("LILAC") || n.equals("ROSE_BUSH")
                || n.equals("PEONY") || n.equals("TALL_SEAGRASS") || n.equals("PITCHER_PLANT");
    }

    /** Вторая половина двойного блока (дверь, высокая трава, кровать) или null. */
    public static Block getPartner(Block block) {
        BlockData data = block.getBlockData();
        if (data instanceof Bisected bisected && isDoubleBlock(block.getType())) {
            return block.getRelative(bisected.getHalf() == Bisected.Half.TOP ? BlockFace.DOWN : BlockFace.UP);
        }
        if (data instanceof Bed bed) {
            return bed.getPart() == Bed.Part.FOOT
                    ? block.getRelative(bed.getFacing())
                    : block.getRelative(bed.getFacing().getOppositeFace());
        }
        return null;
    }

    /**
     * Дропы блока. У двери лут-таблица привязана к нижней половине, у кровати к головной,
     * поэтому для верхней половины двери / ножной части кровати берём дроп с партнёра.
     */
    public static Collection<ItemStack> getDrops(Block block, Player player, ItemStack tool) {
        Block source = block;
        Block partner = getPartner(block);
        if (partner != null) {
            BlockData data = block.getBlockData();
            if (data instanceof Bisected bisected && bisected.getHalf() == Bisected.Half.TOP) {
                source = partner;
            } else if (data instanceof Bed bed && bed.getPart() == Bed.Part.FOOT) {
                source = partner;
            }
        }
        Collection<ItemStack> drops = source.getDrops(tool, player);
        return drops == null ? Collections.emptyList() : drops;
    }

    public static void give(Player player, Collection<ItemStack> items) {
        for (ItemStack item : items) {
            if (item == null || item.getType().isAir()) {
                continue;
            }
            Map<Integer, ItemStack> left = player.getInventory().addItem(item);
            for (ItemStack rest : left.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), rest);
            }
        }
    }

    /**
     * Убрать вторую половину БЕЗ физики. Если убрать её с физикой, игра сама выбросит
     * дверь при обновлении соседнего блока — это и был дюп.
     */
    public static void removePartnerSilently(Block block) {
        Block partner = getPartner(block);
        if (partner != null && partner.getType() == block.getType()) {
            partner.setType(Material.AIR, false);
        }
    }

    /** Массовое ломание (бур, лесоруб). */
    public static void breakExtra(Block block, Player player, ItemStack tool, boolean telepathy) {
        if (!telepathy) {
            block.breakNaturally(tool);
            return;
        }
        Collection<ItemStack> drops = getDrops(block, player, tool);
        removePartnerSilently(block);
        block.setType(Material.AIR, false);
        give(player, drops);
    }
}
