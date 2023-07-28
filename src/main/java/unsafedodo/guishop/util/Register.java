package unsafedodo.guishop.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import unsafedodo.guishop.command.*;

public class Register {
    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(GUIShopMainCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopAddItemCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopDeleteCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopListCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopOpenCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopCreateCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopReloadCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopRemoveItemCommand::register);
        CommandRegistrationCallback.EVENT.register(GUIShopForceSaveCommand::register);
    }
}
