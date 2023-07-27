package unsafedodo.guishop.util;

import com.google.gson.*;
import net.minecraft.nbt.NbtCompound;
import unsafedodo.guishop.shop.ShopItem;

import java.lang.reflect.Type;

public class ShopItemSerializer implements JsonSerializer<ShopItem>, JsonDeserializer<ShopItem> {
    @Override
    public ShopItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonShop = jsonElement.getAsJsonObject();

        String itemName = jsonShop.get("name").toString();
        String itemMaterial = jsonShop.get("material").toString();

        JsonArray jsonDescription = jsonShop.getAsJsonArray("description");
        String[] description = new String[jsonDescription.size()];
        for(int i = 0; i < jsonDescription.size(); i++){
            description[i] = jsonDescription.get(i).toString();
        }

        float buyItemPrice = jsonShop.get("buyPrice").getAsFloat();
        float sellItemPrice = jsonShop.get("sellPrice").getAsFloat();

        //NbtCompound nbt = jsonShop.get("nbt");

        JsonArray jsonQuantities = jsonShop.getAsJsonArray("quantityList");
        int[] quantities = new int[jsonQuantities.size()];
        for(int i = 0; i < jsonQuantities.size(); i++){
            quantities[i] = jsonQuantities.get(i).getAsInt();
        }

        ShopItem finalResult = new ShopItem(itemName, itemMaterial, buyItemPrice, sellItemPrice, description, null, quantities);

        return finalResult;
    }

    @Override
    public JsonElement serialize(ShopItem shopItem, Type type, JsonSerializationContext jsonSerializationContext) {
        String itemName = shopItem.getItemName();
        String itemMaterial = shopItem.getItemMaterial();
        float buyItemPrice = shopItem.getBuyItemPrice();
        float sellItemPrice = shopItem.getSellItemPrice();
        String[] description = shopItem.getDescription();
        NbtCompound nbt = shopItem.getNbt();
        int[] quantities = shopItem.getQuantities();

        JsonObject finalResult = new JsonObject();
        finalResult.add("name", new JsonPrimitive(itemName));
        finalResult.add("material", new JsonPrimitive(itemMaterial));

        JsonArray jsonDescription = new JsonArray(description.length);
        JsonArray jsonQuantities = new JsonArray(quantities.length);
        int count = Math.max(description.length, quantities.length);

        for(int i = 0; i < count; i++){
            if(i < description.length)
                jsonDescription.add(description[i]);
            if(i < quantities.length)
                jsonQuantities.add(quantities[i]);
        }

        finalResult.add("description", jsonDescription);
        finalResult.add("buyPrice", new JsonPrimitive(buyItemPrice));
        finalResult.add("sellPrice", new JsonPrimitive(sellItemPrice));
        finalResult.add("nbt", new JsonPrimitive(nbt.toString()));
        finalResult.add("quantityList", jsonQuantities);

        return finalResult;
    }
}
