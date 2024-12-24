package io.github.megadoxs.extrabotany_reborn.common.craft;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.craft.recipe.CrushingRecipe;
import io.github.megadoxs.extrabotany_reborn.common.craft.recipe.ItemUpgradeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExtraBotany_Reborn.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CrushingRecipe>> CRUSHING_SERIALIZER = SERIALIZERS.register("crushing", () -> CrushingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ItemUpgradeRecipe>> ITEM_UPGRADE = SERIALIZERS.register("item_upgrade", () -> ItemUpgradeRecipe.SERIALIZER);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
