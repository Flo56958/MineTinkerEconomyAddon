package de.flo56958.minetinkereconomyaddon;

import de.flo56958.MineTinker.api.MineTinkerAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineTinkerEconomyAddon extends JavaPlugin {

	private static Economy econ;
	private static JavaPlugin plugin;

	public static Economy getEcon() { return econ; }

	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		econ = rsp.getProvider();

		if (econ == null) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		plugin = this;

		MineTinkerAPI.instance().registerModifier(PickPocket.instance());
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}

	@Override
	public void onDisable() {
		MineTinkerAPI.instance().unregisterModifier(PickPocket.instance());
	}
}
