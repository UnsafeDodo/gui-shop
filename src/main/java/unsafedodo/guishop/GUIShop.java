package unsafedodo.guishop;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unsafedodo.guishop.config.ConfigManager;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.util.Register;
import unsafedodo.guishop.util.ShopFileHandler;

import java.util.LinkedList;

public class GUIShop implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("gui-shop");

	public static final LinkedList<Shop> shops = new LinkedList<>();

	@Override
	public void onInitialize() {
		LOGGER.info("GUI Shop loaded!");

		if(!ConfigManager.loadConfig())
			throw new RuntimeException("Could not load config");

		Register.registerCommands();

		ShopFileHandler fileHandler = new ShopFileHandler();
		fileHandler.initialize();
	}
}