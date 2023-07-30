package unsafedodo.guishop.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import unsafedodo.guishop.GUIShop;
import unsafedodo.guishop.shop.ShopItem;
import unsafedodo.guishop.util.CommonMethods;
import unsafedodo.guishop.util.EconomyTransactionHandler;

public class NewQuantityGUI extends SimpleGui {

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     *
     *                              will be treated as slots of this gui
     */
    public NewQuantityGUI(ServerPlayerEntity player, ShopItem item, SlotGuiInterface parentGUI) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
        this.setLockPlayerInventory(true);
        this.setTitle(Text.of(item.getItemName()));

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE)
                    .setName(Text.empty()));
        }

        this.setSlot(45, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Your balance: ").setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.GREEN)
                        .append(Text.literal(String.format("%.2f $", CommonMethods.getBalance(player))).setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.YELLOW)))
                .setSkullOwner(HeadTextures.MONEY_SYMBOL, null, null));

        ItemStack guiItem = new ItemStack(Registries.ITEM.get(new Identifier(item.getItemMaterial())));
        guiItem.setNbt(item.getNbt());
        this.setSlot(4, GuiElementBuilder.from(guiItem)
                .setName(Text.literal(item.getItemName())));
        int k = 0;
        int[] quantities = item.getQuantities();
        for(int i = 11; i < 45 && k < quantities.length; i+=9){
            final int quantity = quantities[k];
            this.setSlot(i, new GuiElementBuilder(Items.LIME_CONCRETE)
                    .setName(Text.literal(String.format("Buy %d", quantities[k])).formatted(Formatting.GREEN))
                    .addLoreLine(Text.literal(""))
                    .addLoreLine(Text.literal(""))
                    .addLoreLine(Text.literal("Buy Price: ").formatted(Formatting.GREEN)
                            .append(Text.literal(String.format("%.2f $", item.getBuyItemPrice()*quantities[k])).formatted(Formatting.YELLOW)))
                    .addLoreLine(Text.literal("Quantity: ").formatted(Formatting.GREEN)
                            .append(Text.literal(String.format("%d", quantities[k])).formatted(Formatting.YELLOW)))
                    .setCount(quantities[k])
                    .setCallback(((index, type1, action) -> {
                        if(GUIShop.transactionHandler.buyFromShop(player, item.getBuyItemPrice()*quantity)){
                            ItemStack givenItem = new ItemStack(Registries.ITEM.get(new Identifier(item.getItemMaterial())), quantity);
                            givenItem.setNbt(item.getNbt());
                            player.getInventory().offerOrDrop(givenItem);
                            this.setSlot(45, new GuiElementBuilder()
                                    .setItem(Items.PLAYER_HEAD)
                                    .setName(Text.literal("Your balance: ").setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.GREEN)
                                            .append(Text.literal(String.format("%.2f $", CommonMethods.getBalance(player))).setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.YELLOW)))
                                    .setSkullOwner(HeadTextures.MONEY_SYMBOL, null, null));
                        }

                    })));
            this.setSlot(i+4, new GuiElementBuilder(Items.RED_CONCRETE)
                    .setName(Text.literal(String.format("Sell %d", quantities[k])).formatted(Formatting.RED))
                    .addLoreLine(Text.literal(""))
                    .addLoreLine(Text.literal(""))
                    .addLoreLine(Text.literal("Sell Price: ").formatted(Formatting.RED)
                            .append(Text.literal(String.format("%.2f $", item.getSellItemPrice()*quantities[k])).formatted(Formatting.YELLOW)))
                    .addLoreLine(Text.literal("Quantity: ").formatted(Formatting.RED)
                            .append(Text.literal(String.format("%d", quantities[k])).formatted(Formatting.YELLOW)))
                    .setCount(quantities[k])
                    .setCallback((index, type1, action) -> {
                        if(removeItemFromInventory(player, Registries.ITEM.get(new Identifier(item.getItemMaterial())), quantity)){
                            GUIShop.transactionHandler.sellToShop(player, item.getSellItemPrice()*quantity);
                            this.setSlot(45, new GuiElementBuilder()
                                    .setItem(Items.PLAYER_HEAD)
                                    .setName(Text.literal("Your balance: ").setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.GREEN)
                                            .append(Text.literal(String.format("%.2f $", CommonMethods.getBalance(player))).setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.YELLOW)))
                                    .setSkullOwner(HeadTextures.MONEY_SYMBOL, null, null));
                        }

                    }));
            k++;
        }

        this.setSlot(53, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Back").setStyle(Style.EMPTY.withItalic(true)))
                .setSkullOwner(HeadTextures.GUI_PREVIOUS_PAGE, null, null)
                .setCallback(((index, clickType, action) -> {
                    this.close();
                    parentGUI.setSlot(45, new GuiElementBuilder()
                            .setItem(Items.PLAYER_HEAD)
                            .setName(Text.literal("Your balance: ").setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.GREEN)
                                    .append(Text.literal(String.format("%.2f $", CommonMethods.getBalance(player))).setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.YELLOW)))
                            .setSkullOwner(HeadTextures.MONEY_SYMBOL, null, null));
                    parentGUI.open();
                })));
    }

    public boolean removeItemFromInventory (ServerPlayerEntity player, Item itemToRemove, int quantity){
        int i = 0;
        if(player.getInventory().count(itemToRemove) >= quantity) {
            //loop to remove items from player's inventory
            while(quantity > 0){
                if(player.getInventory().getStack(i).getItem().equals(itemToRemove)){
                    if(player.getInventory().getStack(i).getCount() == quantity){
                        player.getInventory().removeStack(i);
                        quantity = 0;
                    } else if (player.getInventory().getStack(i).getCount() > quantity) {
                        ItemStack newItem = new ItemStack(itemToRemove, player.getInventory().getStack(i).getCount() - quantity);
                        player.getInventory().removeStack(i);
                        player.getInventory().setStack(i, newItem);
                        quantity = 0;
                    } else if (player.getInventory().getStack(i).getCount() < quantity) {
                        quantity -= player.getInventory().getStack(i).getCount();
                        player.getInventory().removeStack(i, player.getInventory().getStack(i).getCount());
                    }
                }
                i++;
            }
            return true;
        } else
            return false;
    }
}
