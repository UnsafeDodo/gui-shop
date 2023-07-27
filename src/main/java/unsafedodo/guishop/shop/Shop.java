package unsafedodo.guishop.shop;

import java.util.List;

public class Shop {
    private String name;
    private List<ShopItem> items;

    public String getName() {
        return name;
    }

    public List<ShopItem> getItems() {
        return items;
    }

    public Shop(String name, List<ShopItem> items) {
        this.name = name;
        this.items = items;
    }
}
