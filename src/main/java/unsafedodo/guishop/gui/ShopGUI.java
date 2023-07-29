package unsafedodo.guishop.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.*;
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
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.shop.ShopItem;
import unsafedodo.guishop.util.EconomyTransactionHandler;

public class ShopGUI extends SimpleGui{


    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param shop                  the shop
     *                              will be treated as slots of this gui
     */
    public ShopGUI(ServerPlayerEntity player, Shop shop) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
        this.setLockPlayerInventory(true);
        this.setTitle(Text.of(shop.getName()));

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(53, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true)))
                .setCallback(((index, clickType, action) -> this.close())));

        for(int i = 0; i < Math.min(shop.getItems().size(), 36); i++){
            ShopItem item = shop.getItems().get(i);
            this.setSlot(i, new GuiElementBuilder(Registries.ITEM.get(new Identifier(item.getItemMaterial())))
                    .setName(Text.literal(item.getItemName()))
                    .setLore(item.getDescriptionAsText())
                    .addLoreLine(Text.literal(""))
                    .addLoreLine(Text.literal(""))
                    .addLoreLine(Text.literal(""))
                    .addLoreLine(item.getLoreBuyPrice(1))
                    .addLoreLine(item.getLoreSellPrice(1))
                    .setCallback((index, type1, action, gui) -> {
                        gui.close();
                        QuantityGUI quantityGUI = new QuantityGUI(player, item, gui);
                        quantityGUI.open();
                    }));
        }

    }
}

class QuantityGUI extends SimpleGui{

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param item                  the item sold or bought
     */
    public QuantityGUI(ServerPlayerEntity player, ShopItem item, SlotGuiInterface parentGUI) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
        this.setLockPlayerInventory(true);
        this.setTitle(Text.of(item.getItemName()));

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE)
                    .setName(Text.empty()));
        }

        this.setSlot(4, new GuiElementBuilder(Registries.ITEM.get(new Identifier(item.getItemMaterial())))
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
                        EconomyTransactionHandler.buyFromShop(player, item.getBuyItemPrice()*quantity);
                        player.getInventory().offerOrDrop(new ItemStack(Registries.ITEM.get(new Identifier(item.getItemMaterial())), quantity));
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
                        if(removeItemFromInventory(player, Registries.ITEM.get(new Identifier(item.getItemMaterial())), quantity))
                            EconomyTransactionHandler.sellToShop(player, item.getSellItemPrice()*quantity);
                    }));
            k++;
        }

        this.setSlot(53, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Back").setStyle(Style.EMPTY.withItalic(true)))
                .setSkullOwner(HeadTextures.GUI_PREVIOUS_PAGE, null, null)
                .setCallback(((index, clickType, action) -> {
                    this.close();
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
