package com.topka.enchants.enchants;

import com.topka.enchants.TopkaEnchants;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Кастомные зачарования. В 1.21.x нельзя создавать свои Enchantment через наследование/рефлексию
 * (Enchantment теперь реестровый), поэтому зачарование хранится в PersistentDataContainer предмета.
 */
public enum CustomEnchantment {
    LIGHTNER_BOOK("lightner"),
    STEALER_BOOK("stealer"),
    DRILL_BOOK("drill"),
    WISDOM_BOOK("wisdom"),
    WOODCUTTER_BOOK("woodcutter"),
    TELEPATION_BOOK("telepation");

    private final String metaId;

    CustomEnchantment(String metaId) {
        this.metaId = metaId;
    }

    private FileConfiguration config() {
        return TopkaEnchants.getInstance().getConfig();
    }

    public String getMetaId() {
        return metaId;
    }

    public String getName() {
        return config().getString(metaId + ".name", "&6Чародейская книга");
    }

    public List<String> getDesc() {
        return config().getStringList(metaId + ".lore");
    }

    public List<String> getItems() {
        return config().getStringList(metaId + ".items");
    }

    /** Ключ книги (прежний, чтобы уже выданные книги продолжали работать). */
    public NamespacedKey getBookKey() {
        return NamespacedKey.minecraft(metaId);
    }

    /** Ключ зачарования на инструменте. */
    public NamespacedKey getToolKey() {
        return new NamespacedKey(TopkaEnchants.getInstance(), "ench_" + metaId);
    }

    public boolean isOn(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(getToolKey(), PersistentDataType.BYTE);
    }

    public boolean isBook(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(getBookKey(), PersistentDataType.STRING);
    }

    public static CustomEnchantment getEnchant(ItemStack book) {
        for (CustomEnchantment enchant : values()) {
            if (enchant.isBook(book)) {
                return enchant;
            }
        }
        return null;
    }
}
