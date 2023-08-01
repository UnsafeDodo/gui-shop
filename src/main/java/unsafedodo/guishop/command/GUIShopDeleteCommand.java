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
import unsafedodo.guishop.GUIShop;
import unsafedodo.guishop.shop.Shop;

public class GUIShopDeleteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        /*dispatcher.register(CommandManager.literal("guishop")
                .then(CommandManager.literal("delete")
                        .then(CommandManager.argument("shopName", StringArgumentType.string())
                                .requires(Permissions.require("guishop.delete"))
                                    .executes(GUIShopDeleteCommand::run))));*/

        dispatcher.register(CommandManager.literal("guishop")
                .then(CommandManager.literal("delete")
                        .requires(Permissions.require("guishop.delete", 3))
                        .then(CommandManager.argument("shopName", StringArgumentType.string())
                                .executes(GUIShopDeleteCommand::run))));
    }

    public static int run(CommandContext<ServerCommandSource> context){
        String shopName = StringArgumentType.getString(context, "shopName");
        boolean found = false;

        for(Shop shop: GUIShop.shops){
            if(shop.getName().equals(shopName)){
                GUIShop.shops.remove(shop);
                found = true;
                break;
            }
        }

        if(found)
            context.getSource().sendFeedback(()-> Text.literal("Shop successfully removed!").formatted(Formatting.GREEN), false);
        else
            context.getSource().sendFeedback(()-> Text.literal("Shop not found!").formatted(Formatting.RED), false);

        return 0;
    }
}
