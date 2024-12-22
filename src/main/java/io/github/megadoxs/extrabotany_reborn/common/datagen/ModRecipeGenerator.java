package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import io.github.megadoxs.extrabotany_reborn.common.craft.builder.CrushingRecipeBuilder;
import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.data.recipes.NbtOutputResult;

import java.util.function.Consumer;

public class ModRecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public ModRecipeGenerator(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MANASTEEL_SHIELD.get())
                .pattern("M M")
                .pattern("MSM")
                .pattern("M M")
                .define('M', BotaniaItems.manaSteel)
                .define('S', Items.SHIELD)
                .unlockedBy(getHasName(BotaniaItems.manaSteel), has(BotaniaItems.manaSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ELEMENTIUM_SHIELD.get())
                .pattern("E E")
                .pattern("ESE")
                .pattern("E E")
                .define('E', BotaniaItems.elementium)
                .define('S', Items.SHIELD)
                .unlockedBy(getHasName(BotaniaItems.elementium), has(BotaniaItems.elementium))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TERRASTEEL_SHIELD.get())
                .pattern("T T")
                .pattern("TST")
                .pattern("T T")
                .define('T', BotaniaItems.terrasteel)
                .define('S', Items.SHIELD)
                .unlockedBy(getHasName(BotaniaItems.terrasteel), has(BotaniaItems.terrasteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MANASTEEL_HAMMER.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern(" L ")
                .define('M', BotaniaItems.manaSteel)
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(BotaniaItems.manaSteel), has(BotaniaItems.manaSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ELEMENTIUM_HAMMER.get())
                .pattern("EEE")
                .pattern("EEE")
                .pattern(" L ")
                .define('E', BotaniaItems.elementium)
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(BotaniaItems.elementium), has(BotaniaItems.elementium))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TERRASTEEL_HAMMER.get())
                .pattern("TTT")
                .pattern("TTT")
                .pattern(" L ")
                .define('T', BotaniaItems.terrasteel)
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(BotaniaItems.terrasteel), has(BotaniaItems.terrasteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NATURE_ORB.get())
                .pattern("TDT")
                .pattern("DMD")
                .pattern("TDT")
                .define('T', BotaniaItems.terrasteel)
                .define('D', BotaniaItems.dragonstone)
                .define('M', BotaniaItems.manaPearl)
                .unlockedBy(getHasName(BotaniaItems.dragonstone), has(BotaniaItems.dragonstone))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.PHOTONIUM_BLOCK.get())
                .requires(ModItems.PHOTONIUM_INGOT.get(), 9)
                .unlockedBy(getHasName(ModItems.PHOTONIUM_INGOT.get()), has(ModItems.PHOTONIUM_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PHOTONIUM_INGOT.get(), 9)
                .requires(ModBlocks.PHOTONIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.PHOTONIUM_BLOCK.get()), has(ModBlocks.PHOTONIUM_BLOCK.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.SHADOWIUM_BLOCK.get())
                .requires(ModItems.SHADOWIUM_INGOT.get(), 9)
                .unlockedBy(getHasName(ModItems.SHADOWIUM_INGOT.get()), has(ModItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SHADOWIUM_INGOT.get(), 9)
                .requires(ModBlocks.SHADOWIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.SHADOWIUM_BLOCK.get()), has(ModBlocks.SHADOWIUM_BLOCK.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.ORICHALCOS_BLOCK.get())
                .requires(ModItems.ORICHALCOS_INGOT.get(), 9)
                .unlockedBy(getHasName(ModItems.ORICHALCOS_INGOT.get()), has(ModItems.ORICHALCOS_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ORICHALCOS_INGOT.get(), 9)
                .requires(ModBlocks.ORICHALCOS_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.ORICHALCOS_BLOCK.get()), has(ModBlocks.ORICHALCOS_BLOCK.get()))
                .save(consumer);

        CrushingRecipeBuilder.crushing(ModItems.GILDED_MASHED_POTATO.get())
                .addIngredient(ModItems.GILDED_POTATO.get())
                .strikes(10)
                .save(consumer);

        CrushingRecipeBuilder.crushing(ModItems.SPIRIT_FRAGMENT.get())
                .addIngredient(ModItems.SPIRIT_FUEL.get())
                .strikes(10)
                .save(consumer);

        CrushingRecipeBuilder.crushing(Blocks.GRAVEL.asItem())
                .addIngredient(Blocks.COBBLESTONE.asItem())
                .strikes(10)
                .save(consumer);

        CrushingRecipeBuilder.crushing(Items.FLINT)
                .addIngredient(Blocks.GRAVEL.asItem())
                .strikes(10)
                .save(consumer);

        CrushingRecipeBuilder.crushing(Items.GUNPOWDER)
                .addIngredient(Items.FLINT)
                .strikes(10)
                .save(consumer);

        // needs to add item to craft the ones unique to the god's core
        Item[] items = { Items.QUARTZ, BotaniaItems.darkQuartz, BotaniaItems.manaQuartz, BotaniaItems.blazeQuartz, BotaniaItems.lavenderQuartz, BotaniaItems.redQuartz, BotaniaItems.elfQuartz, BotaniaItems.sunnyQuartz };
        for (int i = 0; i < items.length; i++) {
            int godCoreType = i + 1;
            ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.GOD_CORE.get())
                    .requires(ModItems.GOD_CORE.get())
                    .requires(items[i])
                    .unlockedBy(getHasName(ModItems.GOD_CORE.get()), has(ModItems.GOD_CORE.get()))
                    .save(NbtOutputResult.with(consumer, tag -> tag.putInt("variant", godCoreType)),
                            "extrabotany_reborn:god_core_" + godCoreType);
        }

        //ManaInfusionRecipe test = new ManaInfusionRecipe(new ResourceLocation("test"), new ItemStack(ModItems.NIGHTMARE_FUEL.get()), Ingredient.of(Items.COAL), 2000, null, null);
    }
}
