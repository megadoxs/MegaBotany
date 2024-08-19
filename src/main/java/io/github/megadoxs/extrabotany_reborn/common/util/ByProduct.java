package io.github.megadoxs.extrabotany_reborn.common.util;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

public record ByProduct(ItemStack itemStack, float chance) {
    public static ByProduct fromJson(JsonObject jsonObject){
        ItemStack itemStack = CraftingHelper.getItemStack(jsonObject, true, true);
        float chance = GsonHelper.getAsFloat(jsonObject, "chance", 1f);
        return new ByProduct(itemStack, chance);
    }
}
