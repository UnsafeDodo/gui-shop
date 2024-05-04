package unsafedodo.guishop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.shop.ShopItem;
import unsafedodo.guishop.util.CommonMethods;

public class GUIShopAddHeldItemCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("guishop")
                .then(CommandManager.literal("addhelditem")
                        .then(CommandManager.argument("shopName", StringArgumentType.string())
                                .suggests(new CommonMethods.ShopNameSuggestionProvider())
                                .then(CommandManager.argument("itemName", StringArgumentType.string())
                                        .then(CommandManager.argument("buyItemPrice", FloatArgumentType.floatArg(-1.0f))
                                                .then(CommandManager.argument("sellItemPrice", FloatArgumentType.floatArg(-1.0f))
                                                        .then(CommandManager.argument("quantities", StringArgumentType.string())
                                                                .requires(Permissions.require("guishop.additem", 2))
                                                                .executes(GUIShopAddHeldItemCommand::run))))))));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String shopName = StringArgumentType.getString(context, "shopName");
        String itemName = StringArgumentType.getString(context, "itemName");
        float buyItemPrice = FloatArgumentType.getFloat(context, "buyItemPrice");
        float sellItemPrice = FloatArgumentType.getFloat(context, "sellItemPrice");
        String quantitiesString = StringArgumentType.getString(context, "quantities");

        Shop foundShop = CommonMethods.getShopByName(shopName);
        if (foundShop == null) {
            context.getSource().sendFeedback(() -> Text.literal(String.format("Shop %s not found", shopName)).formatted(Formatting.RED), false);
            return -1;
        }

        ItemStack heldItem = context.getSource().getPlayer().getMainHandStack();
        if (heldItem.isEmpty()) {
            context.getSource().sendFeedback(() -> Text.literal("You must be holding an item to add it to the shop").formatted(Formatting.RED), false);
            return -1;
        }

        String itemMaterial = heldItem.getItem().getRegistryEntry().registryKey().getValue().toString();

        try {
            String[] quantitiesStrings = quantitiesString.split(":");
            int quantitiesLength = quantitiesStrings.length;
            int[] quantities = new int[quantitiesLength];
            if (quantitiesLength > 4) {
                context.getSource().sendFeedback(() -> Text.literal("You may only input up to 4 quantities").formatted(Formatting.RED), false);
                return -1;
            }

            int maxStackCount = heldItem.getMaxCount();
            for (int i = 0; i < quantitiesLength; i++) {
                quantities[i] = Integer.parseInt(quantitiesStrings[i]);
                if (quantities[i] > maxStackCount) {
                    final int wrongQuantity = quantities[i];
                    context.getSource().sendFeedback(() -> Text.literal(String.format("Could not add item: Invalid quantity %d (exceeds max stack size)", wrongQuantity)).formatted(Formatting.RED), false);
                    return -1;
                }
            }

            foundShop.getItems().add(new ShopItem(
                    itemName,
                    itemMaterial,
                    buyItemPrice,
                    sellItemPrice,
                    new String[]{},
                    heldItem.getNbt() != null ? heldItem.getNbt() : StringNbtReader.parse("{}"),
                    quantities
            ));
            context.getSource().sendFeedback(() -> Text.literal("Item successfully added").formatted(Formatting.GREEN), false);
        } catch (NumberFormatException nfe) {
            context.getSource().sendFeedback(() -> Text.literal("Could not add item: Invalid quantities").formatted(Formatting.RED), false);
        }

        return 0;
    }
}