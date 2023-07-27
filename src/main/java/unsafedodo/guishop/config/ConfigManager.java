package unsafedodo.guishop.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import unsafedodo.guishop.GUIShop;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.shop.ShopItem;
import unsafedodo.guishop.util.ShopItemSerializer;
import unsafedodo.guishop.util.ShopSerializer;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ShopItem.class, new ShopItemSerializer()).registerTypeAdapter(Shop.class, new ShopSerializer()).setPrettyPrinting().disableHtmlEscaping().create();

    public static boolean loadConfig(){
        boolean success;
        try {

            File configDir = Paths.get("", "config").toFile();
            File configFile = new File(configDir, "guishop.json");

            ConfigData configData = configFile.exists() ? GSON.fromJson(new InputStreamReader(new FileInputStream(configFile), "UTF-8"), ConfigData.class) : new ConfigData();

            {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));
                writer.write(GSON.toJson(configData));
                writer.close();
            }

            //new addition


            success = true;

        } catch (IOException e){
            success = false;
        }

        return success;
    }
}
