package unsafedodo.guishop.gui;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
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

        this.setSlot(45, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Your balance: ").setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.GREEN)
                        .append(Text.literal(String.format("balance $")).setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.YELLOW)))
                .setSkullOwner(HeadTextures.MONEY_SYMBOL, null, null));

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
                        NewQuantityGUI qGUI = new NewQuantityGUI(player, item, gui);
                        gui.close();
                        qGUI.open();
                    }));
        }
    }
}


