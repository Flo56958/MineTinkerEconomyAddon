package de.flo56958.minetinkereconomyaddon;

import de.flo56958.MineTinker.Data.ToolType;
import de.flo56958.MineTinker.Events.MTEntityDamageByEntityEvent;
import de.flo56958.MineTinker.Modifiers.Modifier;
import de.flo56958.MineTinker.Utilities.ChatWriter;
import de.flo56958.MineTinker.Utilities.ConfigurationManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PickPocket extends Modifier implements Listener {

	private static PickPocket instance;
	private int percentagePerLevel;
	private double moneyAmount;

	public static PickPocket instance() {
		synchronized (PickPocket.class) {
			if (instance == null) {
				instance = new PickPocket();
			}
		}

		return instance;
	}

	private PickPocket() {
		super(MineTinkerEconomyAddon.getPlugin());
	}

	@Override
	public String getKey() {
		return "Pick-Pocket";
	}

	@Override
	public List<ToolType> getAllowedTools() {
		return Arrays.asList(ToolType.AXE, ToolType.SWORD);
	}

	@Override
	public void reload() {
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);

		config.addDefault("Allowed", true);
		config.addDefault("Name", "Pick-Pocket");
		config.addDefault("ModifierItemName", "Mischievous Gold Bar");
		config.addDefault("Description", "%chance chance to steal %amount money from a Player by hitting him!");
		config.addDefault("DescriptionModifierItem", "%WHITE%Modifier-Item for the Pick-Pocket-Modifier");
		config.addDefault("Color", "%DARK_GRAY%");
		config.addDefault("MaxLevel", 5);
		config.addDefault("SlotCost", 2);
		config.addDefault("PercentagePerLevel", 5);
		config.addDefault("MoneyAmount", 5.0d);

		config.addDefault("EnchantCost", 25);
		config.addDefault("Enchantable", true);

		config.addDefault("Recipe.Enabled", false);

		ConfigurationManager.saveConfig(config);
		ConfigurationManager.loadConfig("Modifiers" + File.separator, getFileName());

		init(Material.GOLD_INGOT, true);

		this.percentagePerLevel = config.getInt("PercentagePerLevel", 5);
		this.moneyAmount = config.getDouble("MoneyAmount", 5.00d);
		this.description = this.description.replace("%chance", String.valueOf(this.percentagePerLevel))
				.replace("%amount", MineTinkerEconomyAddon.getEcon().format(this.moneyAmount));
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityHit(MTEntityDamageByEntityEvent event) {
		//TODO: Add logging and Permissions (also for pick-pocket safety)
		if (!this.isAllowed()) return;

		ItemStack tool = event.getTool();
		Player p = event.getPlayer();
		if (!p.equals(event.getEvent().getDamager())) return;

		if (!(event.getEvent().getEntity() instanceof Player)) return;

		Player p2 = (Player) event.getEvent().getEntity();
		if (!modManager.hasMod(tool, this)) return;

		if(!MineTinkerEconomyAddon.getEcon().has(p2, this.moneyAmount)) return;

		int n = new Random().nextInt(100);
		if (n < this.percentagePerLevel * modManager.getModLevel(tool, this)) {
			MineTinkerEconomyAddon.getEcon().depositPlayer(p, this.moneyAmount);
			MineTinkerEconomyAddon.getEcon().withdrawPlayer(p2, this.moneyAmount);
		}
	}
}
