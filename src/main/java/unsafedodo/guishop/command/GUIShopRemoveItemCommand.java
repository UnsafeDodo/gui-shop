package unsafedodo.guishop.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import unsafedodo.guishop.GUIShop;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.shop.ShopItem;

public class GUIShopRemoveItemCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("shop")
                .then(CommandManager.literal("removeitem")
                        .then(CommandManager.argument("shopName", StringArgumentType.string())
                                .then(CommandManager.argument("itemName", StringArgumentType.string())
                                        .requires(Permissions.require("guishop.removeitem", 3))
                                            .executes(GUIShopRemoveItemCommand::run)))));
    }

    public static int run(CommandContext<ServerCommandSource> context){
        String shopName = StringArgumentType.getString(context, "shopName");
        String itemName = StringArgumentType.getString(context, "itemName");
        Shop foundShop = null;
        ShopItem foundItem = null;

        for(Shop shop: GUIShop.shops){
            if(shop.getName().equals(shopName)){
                foundShop = shop;
                break;
            }
        }

        if(foundShop != null){
            for(ShopItem item: foundShop.getItems()){
                if(item.getItemName().equals(itemName)){
                    foundItem = item;
                    break;
                }
            }

            if(foundItem != null){
                foundShop.getItems().remove(foundItem);
                context.getSource().sendFeedback(()-> Text.literal("Item successfully removed".formatted(Formatting.GREEN)), false);
            } else
                context.getSource().sendFeedback(()-> Text.literal("Item not found").formatted(Formatting.RED), false);

        } else
            context.getSource().sendFeedback(()-> Text.literal("Shop not found").formatted(Formatting.RED), false);


        return 0;
    }
}
