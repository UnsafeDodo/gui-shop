/*package unsafedodo.guishop.util;

import com.epherical.octoecon.api.Currency;
import com.epherical.octoecon.api.Economy;
import com.epherical.octoecon.api.event.EconomyEvents;
import com.epherical.octoecon.api.user.UniqueUser;
import net.minecraft.server.network.ServerPlayerEntity;

public class EconomyTransactionHandler implements EconomyEvents.EconomyChange {

    Economy currentEconomy;

    public boolean buyFromShop(ServerPlayerEntity player, double price){
        Currency currency = currentEconomy.getDefaultCurrency();
        UniqueUser userBalance = currentEconomy.getOrCreatePlayerAccount(player.getUuid());
        if (userBalance == null) {
            return false;
        }
        if(userBalance.getBalance(currency) >= price){
            userBalance.withdrawMoney(currency, price, "Purchase from shop");
            return true;
        }

        return false;
    }

    public boolean sellToShop(ServerPlayerEntity player, double price){
        Currency currency = currentEconomy.getDefaultCurrency();
        UniqueUser userBalance = currentEconomy.getOrCreatePlayerAccount(player.getUuid());
        if (userBalance == null) {
            return false;
        }
        userBalance.depositMoney(currency, price, "Item sold to shop");
        return true;
    }

    @Override
    public void onEconomyChanged(Economy economy) {
        currentEconomy = economy;
    }
}*/
