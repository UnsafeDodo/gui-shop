package unsafedodo.guishop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GUIShopMainCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("guishop")
                .requires(Permissions.require("guishop.main", 2))
                .then(CommandManager.literal("help")
                        .requires(Permissions.require("guishop.help", 2))
                        .executes(GUIShopMainCommand::runHelp))
                .executes(GUIShopMainCommand::run));
    }

    private static int runHelp(CommandContext<ServerCommandSource> context) {
        String msg = """
                /shop additem <shopName> <itemName> <itemId> <buyPrice> <sellPrice> [Description] [NBT] [qty1:qt2:...:qt5]
                /shop removeitem <shopName> <itemName>
                /shop create <shopName>
                /shop delete <shopName>
                /shop list
                /shop list [shopName]
                /shop open <shopName>
                /shop forcesave
                /shop reload""";

        context.getSource().sendFeedback(()->Text.literal(msg).formatted(Formatting.YELLOW), false);

        return 0;
    }

    public static int run(CommandContext<ServerCommandSource> context){
        String git = "https://github.com/UnsafeDodo/gui-shop";
        context.getSource().sendFeedback(()-> Text.literal("GUIShop by UnsafeDodo is running!\nCheck the GitHub repository for usage:").formatted(Formatting.GREEN), false);
        context.getSource().sendFeedback(()->Text.literal(git).setStyle(Style.EMPTY.withUnderline(true)).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, git))).formatted(Formatting.BLUE), false);
        return 0;
    }
}
