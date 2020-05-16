package de.flo56958.minetinkereconomyaddon;

import de.flo56958.MineTinker.Data.ToolType;
import de.flo56958.MineTinker.Modifiers.Modifier;
import de.flo56958.MineTinker.Utilities.ConfigurationManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PickPocket extends Modifier implements Listener {

	private static PickPocket instance;
	private int percentagePerLevel;

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
		config.addDefault("ModifierItemName", "Mischievous Gold bar");
		config.addDefault("Description", "Chance to steal money from a Player by hitting him!");
		config.addDefault("DescriptionModifierItem", "%WHITE%Modifier-Item for the Pick-Pocket-Modifier");
		config.addDefault("Color", "%DARK_GRAY%");
		config.addDefault("MaxLevel", 10);
		config.addDefault("SlotCost", 2);
		config.addDefault("PercentagePerLevel", 2);  //= 100% at Level 10

		config.addDefault("EnchantCost", 25);
		config.addDefault("Enchantable", true);

		config.addDefault("Recipe.Enabled", false);
		config.addDefault("OverrideLanguagesystem", true); //Must be set to true as this Modifier is not in the Language files

		ConfigurationManager.saveConfig(config);
		ConfigurationManager.loadConfig("Modifiers" + File.separator, getFileName());

		init(Material.GOLD_INGOT, true);

		this.percentagePerLevel = config.getInt("PercentagePerLevel", 10);
		this.description = this.description.replace("%chance", String.valueOf(this.percentagePerLevel));
	}

	//TODO: Implement function
}
