package io.github.megadoxs.megabotany.common.data.recipe;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.data.recipes.TerrestrialAgglomerationProvider;

import java.util.function.Consumer;

public class TerrestrialAgglomerationRecipeGenerator extends TerrestrialAgglomerationProvider {
    public TerrestrialAgglomerationRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    protected ResourceLocation id(String s) {
        return ResourceLocationHelper.prefix("terra_plate/" + s);
    }

    @Override
    public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe(id(ForgeRegistries.ITEMS.getKey(MegaBotanyItems.PHOTONIUM_INGOT.get()).getPath()), ManaPoolBlockEntity.MAX_MANA, new ItemStack(MegaBotanyItems.PHOTONIUM_INGOT.get()), Ingredient.of(MegaBotanyItems.SPIRIT_FUEL.get()), Ingredient.of(MegaBotanyItems.EARTH_ESSENCE.get()), Ingredient.of(BotaniaItems.gaiaIngot)));
        consumer.accept(new FinishedRecipe(id(ForgeRegistries.ITEMS.getKey(MegaBotanyItems.SHADOWIUM_INGOT.get()).getPath()), ManaPoolBlockEntity.MAX_MANA, new ItemStack(MegaBotanyItems.SHADOWIUM_INGOT.get()), Ingredient.of(MegaBotanyItems.NIGHTMARE_FUEL.get()), Ingredient.of(MegaBotanyItems.EARTH_ESSENCE.get()), Ingredient.of(BotaniaItems.gaiaIngot)));
        consumer.accept(new FinishedRecipe(id(ForgeRegistries.ITEMS.getKey(MegaBotanyItems.ORICHALCOS_INGOT.get()).getPath()), ManaPoolBlockEntity.MAX_MANA * 3, new ItemStack(MegaBotanyItems.ORICHALCOS_INGOT.get()), Ingredient.of(MegaBotanyItems.SHADOWIUM_INGOT.get()), Ingredient.of(MegaBotanyItems.PHOTONIUM_INGOT.get()), Ingredient.of(MegaBotanyItems.MEDAL_OF_HEROISM.get())));
    }
}
