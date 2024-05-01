package unsafedodo.guishop.command.sell;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.shop.ShopItem;
import unsafedodo.guishop.util.CommonMethods;
import unsafedodo.guishop.util.EconomyHandler;

public class SellCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
		dispatcher.register(CommandManager.literal("sell")
				.then(CommandManager.literal("hand")
						.requires(Permissions.require("guishop.sell.hand", 2))
						.executes(SellCommand::sellHand))
				.then(CommandManager.literal("all")
						.requires(Permissions.require("guishop.sell.all", 2))
						.executes(SellCommand::sellAll)));
	}

	private static boolean isItemEqual(ItemStack itemStack, ShopItem shopItem) {
		if (!itemStack.getItem().getRegistryEntry().registryKey().getValue().toString().equals(shopItem.getItemMaterial())) {
			return false;
		}

		NbtCompound shopItemNbt = shopItem.getNbt();
		NbtCompound itemStackNbt = itemStack.getNbt();

		if ((shopItemNbt == null || shopItemNbt.isEmpty()) && itemStackNbt == null) {
			return true;
		}

//		if (shopItemNbt == null || itemStackNbt == null) {
//			return false;
//		}

		// Compare only the relevant NBT tags
		NbtCompound strippedShopItemNbt = stripIrrelevantTags(shopItemNbt);
		NbtCompound strippedItemStackNbt = stripIrrelevantTags(itemStackNbt);

		return strippedShopItemNbt.equals(strippedItemStackNbt);
	}

	private static NbtCompound stripIrrelevantTags(NbtCompound nbt) {
		if (nbt == null) {
			return new NbtCompound();
		}
		NbtCompound strippedNbt = new NbtCompound();

		// Add only the relevant tags to the stripped NBT
		if (nbt.contains("Enchantments")) {
			strippedNbt.put("Enchantments", nbt.get("Enchantments"));
		}
		if (nbt.contains("CustomModelData")) {
			strippedNbt.putInt("CustomModelData", nbt.getInt("CustomModelData"));
		}
		if (nbt.contains("display")) {
			strippedNbt.put("display", nbt.getCompound("display"));
		}
		// Add more relevant tags as needed

		return strippedNbt;
	}

	private static int sellHand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		ItemStack itemStack = player.getMainHandStack();

		if (itemStack.isEmpty()) {
			context.getSource().sendFeedback(() -> Text.literal("You are not holding any item in your main hand.").styled(style -> style.withColor(Formatting.RED)), false);
			return 0;
		}

		for (Shop shop : CommonMethods.getAllShops()) {
			for (ShopItem shopItem : shop.getItems()) {
				if (isItemEqual(itemStack, shopItem) && shopItem.getSellItemPrice() > 0) {
					int count = player.getInventory().count(itemStack.getItem());
					double totalPrice = shopItem.getSellItemPrice() * count;
					player.getInventory().remove(stack -> isItemEqual(stack, shopItem), count, player.getInventory());
					Account account = EconomyHandler.getAccount(player.getUuid());
					EconomyHandler.add(account, totalPrice);
					MutableText message = Text.literal("Sold " + count + " " + shopItem.getItemName() + " for $" + String.format("%.2f", totalPrice)).styled(style -> style.withColor(Formatting.GREEN));
					context.getSource().sendFeedback(() -> message, false);
					return 1;
				}
			}
		}

		context.getSource().sendFeedback(() -> Text.literal("The item you are holding cannot be sold.").styled(style -> style.withColor(Formatting.RED)), false);
		return 0;
	}

	private static int sellAll(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		double totalPrice = 0;
		int totalCount = 0;

		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack itemStack = player.getInventory().getStack(i);
			if (!itemStack.isEmpty()) {
				for (Shop shop : CommonMethods.getAllShops()) {
					for (ShopItem shopItem : shop.getItems()) {
						if (isItemEqual(itemStack, shopItem) && shopItem.getSellItemPrice() > 0) {
							int count = player.getInventory().count(itemStack.getItem());
							double price = shopItem.getSellItemPrice() * count;
							player.getInventory().remove(stack -> isItemEqual(stack, shopItem), count, player.getInventory());
							totalPrice += price;
							totalCount += count;
							break;
						}
					}
				}
			}
		}

		if (totalCount > 0) {
			Account account = EconomyHandler.getAccount(player.getUuid());
			EconomyHandler.add(account, totalPrice);
			MutableText message = Text.literal("Sold " + totalCount + " items for $" + String.format("%.2f", totalPrice)).styled(style -> style.withColor(Formatting.GREEN));
			context.getSource().sendFeedback(() -> message, false);
		} else {
			context.getSource().sendFeedback(() -> Text.literal("You don't have any sellable items in your inventory.").styled(style -> style.withColor(Formatting.RED)), false);
		}
		return totalCount;
	}

}
