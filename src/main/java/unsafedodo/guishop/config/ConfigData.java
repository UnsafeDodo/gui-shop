package unsafedodo.guishop.config;

import unsafedodo.guishop.shop.Shop;

public class ConfigData {
        Shop[] shops;

    public ConfigData(Shop[] shops) {
        this.shops = shops;
    }

    public ConfigData(){
        this(null);
    }

}
