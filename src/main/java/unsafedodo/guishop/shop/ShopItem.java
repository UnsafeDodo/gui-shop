package unsafedodo.guishop.shop;

import net.minecraft.nbt.NbtCompound;

public class ShopItem {
    private String itemName;
    private String itemMaterial;
    private float buyItemPrice;
    private float sellItemPrice;
    private String[] description;
    private NbtCompound nbt;
    private int[] quantities;


    public ShopItem(String itemName, String itemMaterial, float buyItemPrice, float sellItemPrice, String[] description, NbtCompound nbt, int[] quantities) {
        this.itemName = itemName;
        this.itemMaterial = itemMaterial;
        this.buyItemPrice = buyItemPrice;
        this.sellItemPrice = sellItemPrice;
        this.description = description;
        this.nbt = nbt;
        this.quantities = quantities;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemMaterial() {
        return itemMaterial;
    }

    public float getBuyItemPrice() {
        return buyItemPrice;
    }

    public float getSellItemPrice() {
        return sellItemPrice;
    }

    public String[] getDescription() {
        return description;
    }

    public NbtCompound getNbt() {
        return nbt;
    }

    public int[] getQuantities() {
        return quantities;
    }
}
