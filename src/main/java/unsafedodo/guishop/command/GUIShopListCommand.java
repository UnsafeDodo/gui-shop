package unsafedodo.guishop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;
import unsafedodo.guishop.GUIShop;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.shop.ShopItem;
import unsafedodo.guishop.util.CommonMethods;

import static unsafedodo.guishop.util.CommonMethods.arrayImplode;

public class GUIShopListCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("shop")
                .then(CommandManager.literal("list")
                        .requires(Permissions.require("guishop.list", 3))
                            .executes(GUIShopListCommand::runAllShops)
                .then(CommandManager.argument("shopName", StringArgumentType.greedyString())
                        .requires(Permissions.require("guishop.list.items", 3))
                            .executes(GUIShopListCommand::runSpecificShop))));
        //list gives you the list of shop names
        //list <shopName> gives you the items in the shop
    }

    public static int runAllShops(CommandContext<ServerCommandSource> context){
        if(GUIShop.shops.size() > 0){
            StringBuilder msgBldr = new StringBuilder();

            for(Shop shop: GUIShop.shops){
                msgBldr.append(shop.getName()).append("\n");
            }
            String msg = StringUtils.chomp(msgBldr.toString());

            context.getSource().sendFeedback(()-> Text.literal(msg).formatted(Formatting.AQUA), false);

            return 0;
        } else
            context.getSource().sendFeedback(()->Text.literal("No shops available").formatted(Formatting.RED), false);

        return -1;

    }

    public static int runSpecificShop(CommandContext<ServerCommandSource> context){
        String shopName = StringArgumentType.getString(context, "shopName");
        Shop foundShop = CommonMethods.getShopByName(shopName);

        if(foundShop != null){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n").append(foundShop.getName()).append(" items list:\n\n");
            for(ShopItem item: foundShop.getItems()){
                stringBuilder.append("Item name: ").append(item.getItemName()).append(", ")
                    .append("Buy price: ").append(String.format("%.2f", item.getBuyItemPrice())).append(", ")
                        .append("Sell price: ").append(String.format("%.2f", item.getSellItemPrice())).append(", ")
                            .append("Quantities: ").append(arrayImplode(item.getQuantities(), ":")).append("\n\n");
            }

            String msg = StringUtils.chomp(stringBuilder.toString());
            context.getSource().sendFeedback(()-> Text.literal(msg).formatted(Formatting.AQUA), false);
        } else
            context.getSource().sendFeedback(()->Text.literal("Shop not found!").formatted(Formatting.RED), false);

        return 0;
    }
}
