package unsafedodo.guishop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import unsafedodo.guishop.util.ShopFileHandler;

import java.io.IOException;

public class GUIShopForceSaveCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("guishop")
                .then(CommandManager.literal("forcesave")
                        .requires(Permissions.require("guishop.forcesave", 3))
                        .executes(GUIShopForceSaveCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        ShopFileHandler fileHandler = new ShopFileHandler();
        try{
            fileHandler.saveToFile();
            context.getSource().sendFeedback(Text.literal("Shops successfully saved to config file!").formatted(Formatting.GREEN), false);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        return 0;
    }
}
