package unsafedodo.guishop.util;

import com.epherical.octoecon.api.Economy;
import com.epherical.octoecon.api.user.UniqueUser;
import net.minecraft.server.network.ServerPlayerEntity;
import unsafedodo.guishop.GUIShop;
import unsafedodo.guishop.shop.Shop;

public class CommonMethods {

    public static String arrayImplode(int[] array, String delimiter) {
        StringBuilder strBldr = new StringBuilder();

        for (int i = 0; i < array.length - 1; i++) {
            strBldr.append(array[i]);
            strBldr.append(delimiter);
        }
        strBldr.append(array[array.length - 1]);

        return strBldr.toString();
    }

    /**
     * Gets shop data by name.
     * @param name The name of the shop to look for
     * @return An object of class Shop from the list {@link unsafedodo.guishop.GUIShop#shops} with the same case-sensitive name as the one
     * passed by argument, or null if none is found.
     */
    public static Shop getShopByName(String name) {
        for(Shop shop: GUIShop.shops){
            if(shop.getName().equals(name)){
                return shop;
            }
        }
        return null;
    }

    public static double getBalance(ServerPlayerEntity player){
        Economy currentEconomy = GUIShop.transactionHandler.currentEconomy;
        UniqueUser playerAccount = currentEconomy.getOrCreatePlayerAccount(player.getUuid());
        if(playerAccount != null){
            return playerAccount.getBalance(currentEconomy.getDefaultCurrency());
        }
        return -1;
    }
}
