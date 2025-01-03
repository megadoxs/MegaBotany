package io.github.megadoxs.megabotany.common.datagen;

import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.crafting.builder.CrushingRecipeBuilder;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
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

public class MegaBotanyRecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public MegaBotanyRecipeGenerator(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.MANASTEEL_SHIELD.get())
                .pattern("M M")
                .pattern("MSM")
                .pattern("M M")
                .define('M', BotaniaItems.manaSteel)
                .define('S', Items.SHIELD)
                .unlockedBy(getHasName(BotaniaItems.manaSteel), has(BotaniaItems.manaSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.ELEMENTIUM_SHIELD.get())
                .pattern("E E")
                .pattern("ESE")
                .pattern("E E")
                .define('E', BotaniaItems.elementium)
                .define('S', Items.SHIELD)
                .unlockedBy(getHasName(BotaniaItems.elementium), has(BotaniaItems.elementium))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.TERRASTEEL_SHIELD.get())
                .pattern("T T")
                .pattern("TST")
                .pattern("T T")
                .define('T', BotaniaItems.terrasteel)
                .define('S', Items.SHIELD)
                .unlockedBy(getHasName(BotaniaItems.terrasteel), has(BotaniaItems.terrasteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.MANASTEEL_HAMMER.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern(" L ")
                .define('M', BotaniaItems.manaSteel)
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(BotaniaItems.manaSteel), has(BotaniaItems.manaSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.ELEMENTIUM_HAMMER.get())
                .pattern("EEE")
                .pattern("EEE")
                .pattern(" L ")
                .define('E', BotaniaItems.elementium)
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(BotaniaItems.elementium), has(BotaniaItems.elementium))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.TERRASTEEL_HAMMER.get())
                .pattern("TTT")
                .pattern("TTT")
                .pattern(" L ")
                .define('T', BotaniaItems.terrasteel)
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(BotaniaItems.terrasteel), has(BotaniaItems.terrasteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaBotanyItems.NATURE_ORB.get())
                .pattern("TDT")
                .pattern("DMD")
                .pattern("TDT")
                .define('T', BotaniaItems.terrasteel)
                .define('D', BotaniaItems.dragonstone)
                .define('M', BotaniaItems.manaPearl)
                .unlockedBy(getHasName(BotaniaItems.dragonstone), has(BotaniaItems.dragonstone))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MegaBotanyBlocks.PHOTONIUM_BLOCK.get())
                .requires(MegaBotanyItems.PHOTONIUM_INGOT.get(), 9)
                .unlockedBy(getHasName(MegaBotanyItems.PHOTONIUM_INGOT.get()), has(MegaBotanyItems.PHOTONIUM_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.PHOTONIUM_INGOT.get(), 9)
                .requires(MegaBotanyBlocks.PHOTONIUM_BLOCK.get())
                .unlockedBy(getHasName(MegaBotanyBlocks.PHOTONIUM_BLOCK.get()), has(MegaBotanyBlocks.PHOTONIUM_BLOCK.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MegaBotanyBlocks.SHADOWIUM_BLOCK.get())
                .requires(MegaBotanyItems.SHADOWIUM_INGOT.get(), 9)
                .unlockedBy(getHasName(MegaBotanyItems.SHADOWIUM_INGOT.get()), has(MegaBotanyItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.SHADOWIUM_INGOT.get(), 9)
                .requires(MegaBotanyBlocks.SHADOWIUM_BLOCK.get())
                .unlockedBy(getHasName(MegaBotanyBlocks.SHADOWIUM_BLOCK.get()), has(MegaBotanyBlocks.SHADOWIUM_BLOCK.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MegaBotanyBlocks.ORICHALCOS_BLOCK.get())
                .requires(MegaBotanyItems.ORICHALCOS_INGOT.get(), 9)
                .unlockedBy(getHasName(MegaBotanyItems.ORICHALCOS_INGOT.get()), has(MegaBotanyItems.ORICHALCOS_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.ORICHALCOS_INGOT.get(), 9)
                .requires(MegaBotanyBlocks.ORICHALCOS_BLOCK.get())
                .unlockedBy(getHasName(MegaBotanyBlocks.ORICHALCOS_BLOCK.get()), has(MegaBotanyBlocks.ORICHALCOS_BLOCK.get()))
                .save(consumer);

        CrushingRecipeBuilder.crushing(MegaBotanyItems.GILDED_MASHED_POTATO.get())
                .addIngredient(MegaBotanyItems.GILDED_POTATO.get())
                .strikes(10)
                .save(consumer);

        CrushingRecipeBuilder.crushing(MegaBotanyItems.SPIRIT_FRAGMENT.get())
                .addIngredient(MegaBotanyItems.SPIRIT_FUEL.get())
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
        Item[] items = {Items.QUARTZ, BotaniaItems.darkQuartz, BotaniaItems.manaQuartz, BotaniaItems.blazeQuartz, BotaniaItems.lavenderQuartz, BotaniaItems.redQuartz, BotaniaItems.elfQuartz, BotaniaItems.sunnyQuartz};
        for (int i = 0; i < items.length; i++) {
            int godCoreType = i + 1;
            ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, MegaBotanyItems.GOD_CORE.get())
                    .requires(MegaBotanyItems.GOD_CORE.get())
                    .requires(items[i])
                    .unlockedBy(getHasName(MegaBotanyItems.GOD_CORE.get()), has(MegaBotanyItems.GOD_CORE.get()))
                    .save(NbtOutputResult.with(consumer, tag -> tag.putInt("variant", godCoreType)),
                            "megabotany:god_core_" + godCoreType);
        }

        //ManaInfusionRecipe test = new ManaInfusionRecipe(new ResourceLocation("test"), new ItemStack(ModItems.NIGHTMARE_FUEL.get()), Ingredient.of(Items.COAL), 2000, null, null);
    }
}
