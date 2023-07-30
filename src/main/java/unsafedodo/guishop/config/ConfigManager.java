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

public class ConfigManager {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ShopItem.class, new ShopItemSerializer()).registerTypeAdapter(Shop.class, new ShopSerializer()).setPrettyPrinting().disableHtmlEscaping().create();

    public static ConfigData getConfigData(File configFile) throws FileNotFoundException, UnsupportedEncodingException {

        ConfigData configData = configFile.exists() ? GSON.fromJson(new InputStreamReader(new FileInputStream(configFile), "UTF-8"), ConfigData.class) : new ConfigData();

        return configData;
    }

    public static boolean loadConfig(){
        boolean success;
        try {
            File configDir = Paths.get("", "config").toFile();
            File configFile = new File(configDir, "guishop.json");

            ConfigData configData = getConfigData(configFile);

            {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));
                writer.write(GSON.toJson(configData));
                writer.close();
            }

            //new addition
            GUIShop.shops.clear();
            if(configData.shops != null){
                for(Shop shop: configData.shops)
                    GUIShop.shops.addLast(shop);
            }


            success = true;

        } catch (IOException e){
            success = false;
        }

        return success;
    }
}
