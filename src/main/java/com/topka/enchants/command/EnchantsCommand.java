package com.topka.enchants.command;

import com.topka.enchants.TopkaEnchants;
import com.topka.enchants.enchants.CustomEnchantment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class EnchantsCommand implements CommandExecutor, TabCompleter {

    private void say(CommandSender sender, String key) {
        String msg = TopkaEnchants.getInstance().getConfig().getString(key, key);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("topkaEnchants.admin") || args.length < 2) {
            say(sender, "usage");
            return false;
        }
        CustomEnchantment enchant;
        try {
            enchant = CustomEnchantment.valueOf(args[0].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            say(sender, "notFoundItem");
            return false;
        }
        Player player = Bukkit.getPlayerExact(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден: " + args[1]);
            return false;
        }

        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(enchant.getBookKey(), PersistentDataType.STRING, enchant.name());
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', enchant.getName()));
        List<String> lore = new ArrayList<>();
        for (String line : enchant.getDesc()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().addItem(item).values()
                .forEach(rest -> player.getWorld().dropItemNaturally(player.getLocation(), rest));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> out = new ArrayList<>();
        if (args.length == 1) {
            for (CustomEnchantment e : CustomEnchantment.values()) {
                if (e.name().toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))) {
                    out.add(e.name());
                }
            }
        } else if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))) {
                    out.add(p.getName());
                }
            }
        }
        return out;
    }
}
