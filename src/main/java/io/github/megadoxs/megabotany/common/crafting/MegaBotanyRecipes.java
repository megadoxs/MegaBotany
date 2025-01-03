package io.github.megadoxs.megabotany.common.crafting;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.crafting.recipe.CrushingRecipe;
import io.github.megadoxs.megabotany.common.crafting.recipe.GeminiOrchidSourceRecipe;
import io.github.megadoxs.megabotany.common.crafting.recipe.ItemUpgradeRecipe;
import io.github.megadoxs.megabotany.common.crafting.recipe.StonesiaRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MegaBotanyRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MegaBotany.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CrushingRecipe>> CRUSHING_SERIALIZER = SERIALIZERS.register("crushing", () -> CrushingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ItemUpgradeRecipe>> ITEM_UPGRADE_SERIALIZER = SERIALIZERS.register("item_upgrade", () -> ItemUpgradeRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<GeminiOrchidSourceRecipe>> GEMINI_ORCHID_SERIALIZER = SERIALIZERS.register("gemini_orchid_source", () -> GeminiOrchidSourceRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<StonesiaRecipe>> STONESIA_SERIALIZER = SERIALIZERS.register("stonesia", () -> StonesiaRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
