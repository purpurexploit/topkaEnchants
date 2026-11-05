package com.topka.enchants;

import com.topka.enchants.command.EnchantsCommand;
import com.topka.enchants.enchants.impl.DrillEnchant;
import com.topka.enchants.enchants.impl.LightnerEnchant;
import com.topka.enchants.enchants.impl.StealerEnchant;
import com.topka.enchants.enchants.impl.TelepationEnchant;
import com.topka.enchants.enchants.impl.WisdomEnchant;
import com.topka.enchants.enchants.impl.WoodCutterEnchant;
import com.topka.enchants.hook.WGHook;
import com.topka.enchants.listener.AnvilListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TopkaEnchants extends JavaPlugin {
    private static TopkaEnchants instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        WGHook.init(this);

        Listener[] listeners = {
                new DrillEnchant(), new LightnerEnchant(), new TelepationEnchant(),
                new StealerEnchant(), new WoodCutterEnchant(), new WisdomEnchant(),
                new AnvilListener()
        };
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }

        EnchantsCommand command = new EnchantsCommand();
        getCommand("topkaEnchants").setExecutor(command);
        getCommand("topkaEnchants").setTabCompleter(command);
        getLogger().info("======TopkaEnchants loaded successfully=====");
    }

    @Override
    public void onDisable() {
        getLogger().info("======TopkaEnchants unloaded successfully=====");
    }

    public static TopkaEnchants getInstance() {
        return instance;
    }
}
