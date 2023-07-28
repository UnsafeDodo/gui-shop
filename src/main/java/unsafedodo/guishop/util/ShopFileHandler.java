package unsafedodo.guishop.util;

import unsafedodo.guishop.GUIShop;
import unsafedodo.guishop.config.ConfigData;
import unsafedodo.guishop.config.ConfigManager;
import unsafedodo.guishop.shop.Shop;

import java.io.*;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ShopFileHandler implements Runnable{

    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private static final long TIME = 30;

    public boolean initialize(){
        executorService.scheduleAtFixedRate(this, TIME, TIME, TimeUnit.MINUTES);
        return true;
    }

    public void saveToFile() throws IOException {
        Shop[] shops = new Shop[GUIShop.shops.size()];
        String jsonString = ConfigManager.GSON.toJson(new ConfigData(GUIShop.shops.toArray(shops)));

        File configDir = Paths.get("", "config").toFile();
        File configFile = new File(configDir, "guishop.json");

        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));
            writer.write(jsonString);
            writer.close();
        }
    }

    public void killTask(){
        executorService.shutdown();
    }

    @Override
    public void run() {
        try {
            saveToFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
