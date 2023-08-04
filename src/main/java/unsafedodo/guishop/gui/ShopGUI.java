package unsafedodo.guishop.gui;

import eu.pb4.placeholders.api.TextParserUtils;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import unsafedodo.guishop.shop.Shop;
import unsafedodo.guishop.shop.ShopItem;
import unsafedodo.guishop.util.CommonMethods;

public class ShopGUI extends SimpleGui{

    protected Shop shop;

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param shop                  the shop
     *                              will be treated as slots of this gui
     */
    public ShopGUI(ServerPlayerEntity player, Shop shop) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
        this.shop = shop;
        this.setLockPlayerInventory(true);
        this.setTitle(Text.of(shop.getName()));

        for(int i = 0; i < 54; i++){
            this.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(Text.empty()));
        }

        this.setSlot(45, new GuiElementBuilder()
                .setItem(Items.PLAYER_HEAD)
                .setName(Text.literal("Your balance: ").setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.GREEN)
                        .append(Text.literal(String.format("%.2f $", CommonMethods.getBalance(player))).setStyle(Style.EMPTY.withItalic(true)).formatted(Formatting.YELLOW)))
                .setSkullOwner(HeadTextures.MONEY_SYMBOL, null, null));

        this.setSlot(53, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.literal("Exit").setStyle(Style.EMPTY.withItalic(true)))
                .setCallback(((index, clickType, action) -> this.close())));

        for(int i = 0; i < Math.min(shop.getItems().size(), 36); i++){
            ShopItem item = shop.getItems().get(i);
            ItemStack guiItem = new ItemStack(Registry.ITEM.get(new Identifier(item.getItemMaterial())));
            guiItem.setNbt(item.getNbt());
            Text name = TextParserUtils.formatText(item.getItemName());
            this.setSlot(i, GuiElementBuilder.from(guiItem)
                    .setName(name)
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


