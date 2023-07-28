package unsafedodo.guishop.gui;

import eu.pb4.sgui.api.gui.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
        super(ScreenHandlerType.GENERIC_9X6, player, true);
        this.setLockPlayerInventory(true);
        this.setTitle(Text.of(shop.getName()));

        for(int i = 0; i < Math.min(shop.getItems().size(), 36); i++){

        }
    }
}
