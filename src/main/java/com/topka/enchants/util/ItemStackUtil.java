package com.topka.enchants.util;

import com.topka.enchants.enchants.CustomEnchantment;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class ItemStackUtil {
    private static final CustomEnchantment[][] INCOMPATIBLE = {
            {CustomEnchantment.DRILL_BOOK, CustomEnchantment.WOODCUTTER_BOOK},
            {CustomEnchantment.DRILL_BOOK, CustomEnchantment.TELEPATION_BOOK},
            {CustomEnchantment.LIGHTNER_BOOK, CustomEnchantment.STEALER_BOOK},
    };

    private ItemStackUtil() {}

    public static ItemStack enchantItem(ItemStack itemStack, CustomEnchantment enchant) {
        ItemStack result = itemStack.clone();
        ItemMeta meta = result.getItemMeta();
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        for (String line : enchant.getDesc()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(enchant.getToolKey(), PersistentDataType.BYTE, (byte) 1);
        meta.setEnchantmentGlintOverride(true);
        result.setItemMeta(meta);
        return result;
    }

    public static boolean isItemEligibleToEnchant(ItemStack itemStack, CustomEnchantment enchant) {
        if (itemStack == null || itemStack.getType().isAir() || enchant.isOn(itemStack)) {
            return false;
        }
        for (CustomEnchantment[] pair : INCOMPATIBLE) {
            if ((pair[0] == enchant && pair[1].isOn(itemStack)) || (pair[1] == enchant && pair[0].isOn(itemStack))) {
                return false;
            }
        }
        for (String material : enchant.getItems()) {
            if (itemStack.getType().name().equalsIgnoreCase(material)) {
                return true;
            }
        }
        return false;
    }
}
