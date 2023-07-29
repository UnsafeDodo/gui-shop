package unsafedodo.guishop.util;

import com.epherical.octoecon.OctoEconomy;
import com.epherical.octoecon.api.Currency;
import com.epherical.octoecon.api.Economy;
import com.epherical.octoecon.api.user.EconomyIdentity;
import com.epherical.octoecon.api.user.UniqueUser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EconomyTransactionHandler {
    static Economy octoEconomy = OctoEconomy.getInstance().getCurrentEconomy();

    public static boolean buyFromShop(ServerPlayerEntity player, double price){
       /*UniqueUser userBalance = octoEconomy.getOrCreatePlayerAccount(player.getUuid());
        if(userBalance.getBalance(currency) >= price){
            userBalance.withdrawMoney(currency, price, "Purchase from shop");
            return true;
        }

        return false;*/
        return true;
    }

    public static boolean sellToShop(ServerPlayerEntity player, double price){
        /*UniqueUser userBalance = octoEconomy.getOrCreatePlayerAccount(player.getUuid());
        userBalance.depositMoney(currency, price, "Item sold to shop");*/
        return true;
    }
}
