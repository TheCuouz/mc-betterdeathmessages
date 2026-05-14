package com.cristian.betterdeathmessages.integration;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private Economy economy;

    public boolean setup() {
        RegisteredServiceProvider<Economy> rsp =
            Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public boolean isAvailable() { return economy != null; }

    /**
     * Counts total non-air items in the inventory as a proxy for value.
     * Vault's Economy API has no standard item-worth method; this gives
     * server admins a rough indicator for the {inventory_value} token.
     */
    public double estimateInventoryValue(ItemStack[] contents) {
        if (!isAvailable() || contents == null) return 0.0;
        int total = 0;
        for (ItemStack stack : contents) {
            if (stack != null && !stack.getType().isAir()) {
                total += stack.getAmount();
            }
        }
        return total;
    }
}
