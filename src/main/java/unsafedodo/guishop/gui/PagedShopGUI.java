package unsafedodo.guishop.gui;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import unsafedodo.guishop.shop.Shop;

public class PagedShopGUI extends SimpleGui {
    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player                the player to server this gui to
     * @param shop                  the shop opened
     *
     */
    public PagedShopGUI(ScreenHandlerType<?> type, ServerPlayerEntity player, Shop shop) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
    }
}
