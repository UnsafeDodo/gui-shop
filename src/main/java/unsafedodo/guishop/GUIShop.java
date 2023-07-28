package unsafedodo.guishop;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unsafedodo.guishop.config.ConfigManager;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.util.Register;
import unsafedodo.guishop.util.ShopFileHandler;

import java.io.IOException;
import java.util.LinkedList;

public class GUIShop implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("gui-shop");

	public static final LinkedList<Shop> shops = new LinkedList<>();

	static {
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			try {
				onServerShutdown();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public void onInitialize() {
		LOGGER.info("GUI Shop loaded!");

		if(!ConfigManager.loadConfig())
			throw new RuntimeException("Could not load config");

		Register.registerCommands();

		ShopFileHandler fileHandler = new ShopFileHandler();
		if (!fileHandler.initialize()) {
			String msg = "Could not initialize shops-to-file save daemon";
			System.out.println(msg);
			LOGGER.info(msg);
		}
	}



	public static void onServerShutdown() throws IOException {
		ShopFileHandler fileHandler = new ShopFileHandler();
		fileHandler.saveToFile();
		fileHandler.killTask();
	}
}