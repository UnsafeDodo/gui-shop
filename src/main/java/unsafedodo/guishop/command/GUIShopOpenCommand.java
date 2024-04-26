package unsafedodo.guishop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import unsafedodo.guishop.gui.PagedShopGUI;
import unsafedodo.guishop.gui.ShopGUI;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.util.CommonMethods;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GUIShopOpenCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("guishop")
                .then(CommandManager.literal("open")
                        .then(CommandManager.argument("shopName", StringArgumentType.string())
                                .suggests(new CommonMethods.ShopNameSuggestionProvider())
                                .then(CommandManager.argument("playerName", EntityArgumentType.player())
                                .requires(Permissions.require("guishop.open",2))
                                .executes(GUIShopOpenCommand::run)))));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String shopName = StringArgumentType.getString(context, "shopName");
        Shop selectedShop = CommonMethods.getShopByName(shopName);

        if(selectedShop != null){
            if(selectedShop.getItems().size() > 0){
                if(selectedShop.getItems().size() <= PagedShopGUI.MAX_PAGE_ITEMS){
                    try{
                        ShopGUI shopGUI = new ShopGUI(EntityArgumentType.getPlayer(context, "playerName"), selectedShop);
                        shopGUI.open();
                    } catch (ExecutionException  | InterruptedException ignored){

                    }

                } else {
                    try{
                        PagedShopGUI pagedShopGUI = new PagedShopGUI(EntityArgumentType.getPlayer(context, "playerName"), selectedShop);
                        pagedShopGUI.open();
                    } catch (ExecutionException  | InterruptedException ignored){

                    }

                }
            }else{
                context.getSource().sendFeedback(()->Text.literal("The shop does not contain any items").formatted(Formatting.RED), false);
                return -1;
            }

        }else
            context.getSource().sendFeedback(()-> Text.literal("Shop not found").formatted(Formatting.RED), false);
        return 0;
    }
}
