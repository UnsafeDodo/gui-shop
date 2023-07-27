package unsafedodo.guishop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import unsafedodo.guishop.config.ConfigManager;

public class GUIShopReloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("shop")
                .then(CommandManager.literal("reload")
                        .requires(Permissions.require("guishop.reload", 3))
                        .executes(GUIShopReloadCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context){
        if (ConfigManager.loadConfig()) {
            context.getSource().sendFeedback(() -> Text.literal("Reloaded config!").formatted(Formatting.GREEN), false);
        } else {
            context.getSource().sendError(Text.literal("Error accrued while reloading config!").formatted(Formatting.RED));
        }

        return 0;
    }
}
