package io.github.megadoxs.megabotany.common.data;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.crafting.recipe.RelicRecipe;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import io.github.megadoxs.megabotany.common.util.MegaBotanyTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.crafting.recipe.ShapelessManaUpgradeRecipe;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.data.recipes.NbtOutputResult;
import vazkii.botania.data.recipes.WrapperResult;

import java.util.function.Consumer;

public class MegaBotanyRecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public MegaBotanyRecipeGenerator(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MegaBotanyItems.FROST_RING.get())
                .pattern("IM ")
                .pattern("M M")
                .pattern(" MW")
                .define('I', Items.ICE)
                .define('M', BotaniaItems.manaSteel)
                .define('W', BotaniaItems.runeWinter)
                .unlockedBy(getHasName(BotaniaItems.manaSteel), has(BotaniaItems.manaSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MegaBotanyItems.WALL_CLIMB_AMULET.get())
                .pattern("VM ")
                .pattern("M M")
                .pattern(" ME")
                .define('V', Items.VINE)
                .define('M', BotaniaItems.manaSteel)
                .define('E', BotaniaItems.runeEarth)
                .unlockedBy(getHasName(BotaniaItems.manaSteel), has(BotaniaItems.manaSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MegaBotanyItems.WALL_JUMP_AMULET.get())
                .pattern("SM ")
                .pattern("M M")
                .pattern(" ME")
                .define('S', Items.SLIME_BALL)
                .define('M', BotaniaItems.manaSteel)
                .define('E', BotaniaItems.runeEarth)
                .unlockedBy(getHasName(BotaniaItems.manaSteel), has(BotaniaItems.manaSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MegaBotanyItems.MANA_DRIVE_RING.get())
                .pattern("ME ")
                .pattern("E E")
                .pattern(" EA")
                .define('M', BotaniaItems.runeMana)
                .define('E', BotaniaItems.elementium)
                .define('A', BotaniaItems.runeAir)
                .unlockedBy(getHasName(BotaniaItems.elementium), has(BotaniaItems.elementium))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, MegaBotanyItems.MASTER_BAND_OF_MANA.get())
                .requires(MegaBotanyItems.ORICHALCOS_INGOT.get())
                .requires(BotaniaItems.manaRingGreater)
                .unlockedBy(getHasName(MegaBotanyItems.ORICHALCOS_INGOT.get()), has(MegaBotanyItems.ORICHALCOS_INGOT.get()))
                .save(WrapperResult.ofType(ShapelessManaUpgradeRecipe.SERIALIZER, consumer));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.WALKING_CANE.get())
                .pattern(" RG")
                .pattern(" TR")
                .pattern("T  ")
                .define('R', BotaniaBlocks.livingrock)
                .define('G', Items.GOLD_INGOT)
                .define('T', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(BotaniaItems.livingwoodTwig), has(BotaniaItems.livingwoodTwig))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaBotanyItems.BOTTLED_FLAME.get())
                .pattern("PPP")
                .pattern("MTM")
                .pattern("MMM")
                .define('P', BotaniaItems.redPetal)
                .define('T', Items.TORCH)
                .define('M', BotaniaBlocks.manaGlass)
                .unlockedBy(getHasName(BotaniaBlocks.manaGlass), has(BotaniaBlocks.manaGlass))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaBotanyItems.PURE_DAISY_PENDANT.get())
                .pattern("DMD")
                .pattern("MPM")
                .pattern("PMP")
                .define('P', BotaniaFlowerBlocks.pureDaisy)
                .define('D', BotaniaItems.pixieDust)
                .define('M', BotaniaItems.manaString)
                .unlockedBy(getHasName(BotaniaItems.elementium), has(BotaniaItems.elementium))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaBotanyItems.SUPER_CROWN.get())
                .pattern("F F")
                .pattern("GOG")
                .pattern("GPG")
                .define('F', BotaniaItems.pinkPetal)
                .define('P', BotaniaItems.runePride)
                .define('G', Items.GOLD_INGOT)
                .define('O', MegaBotanyItems.ORICHALCOS_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.ORICHALCOS_INGOT.get()), has(MegaBotanyItems.ORICHALCOS_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.JINGWEI_FEATHER.get())
                .requires(MegaBotanyItems.ORICHALCOS_INGOT.get())
                .requires(Items.LAVA_BUCKET)
                .requires(Items.FEATHER)
                .unlockedBy(getHasName(MegaBotanyItems.ORICHALCOS_INGOT.get()), has(MegaBotanyItems.ORICHALCOS_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.SMELTING_LENS.get())
                .requires(BotaniaItems.lensNormal)
                .requires(BotaniaItems.runeFire)
                .requires(MegaBotanyTags.Items.MANA_FUEL)
                .unlockedBy(getHasName(BotaniaItems.lensNormal), has(BotaniaItems.lensNormal))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.MANA_LENS.get())
                .requires(BotaniaItems.lensNormal)
                .requires(BotaniaItems.runeMana)
                .unlockedBy(getHasName(BotaniaItems.lensNormal), has(BotaniaItems.lensNormal))
                .save(consumer);

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaBotanyItems.TICKET.get())
                .pattern("ESE")
                .pattern("PGP")
                .pattern("ESE")
                .define('G', BotaniaItems.gaiaIngot)
                .define('E', BotaniaItems.lifeEssence)
                .define('P', MegaBotanyItems.PHOTONIUM_INGOT.get())
                .define('S', MegaBotanyItems.SHADOWIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.PHOTONIUM_INGOT.get()), has(MegaBotanyItems.PHOTONIUM_INGOT.get()))
                .unlockedBy(getHasName(MegaBotanyItems.SHADOWIUM_INGOT.get()), has(MegaBotanyItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaBotanyBlocks.PEDESTAL.get())
                .pattern("RRR")
                .pattern(" R ")
                .pattern("RRR")
                .define('R', BotaniaBlocks.livingrock)
                .unlockedBy(getHasName(BotaniaBlocks.livingrock), has(BotaniaBlocks.livingrock))
                .save(consumer);

        // needs to add item to craft the ones unique to the god's core
        Item[] items = {Items.QUARTZ, BotaniaItems.darkQuartz, BotaniaItems.manaQuartz, BotaniaItems.blazeQuartz, BotaniaItems.lavenderQuartz, BotaniaItems.redQuartz, BotaniaItems.elfQuartz, BotaniaItems.sunnyQuartz};
        for (int i = 0; i < items.length; i++) {
            int godCoreType = i + 1;
            ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, MegaBotanyItems.GOD_CORE.get())
                    .requires(MegaBotanyItems.GOD_CORE.get())
                    .requires(items[i])
                    .unlockedBy(getHasName(MegaBotanyItems.GOD_CORE.get()), has(MegaBotanyItems.GOD_CORE.get()))
                    .save(NbtOutputResult.with(consumer, tag -> tag.putInt("variant", godCoreType)), "megabotany:god_core_" + godCoreType);
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.INFINITE_DRINK.get())
                .requires(MegaBotanyItems.INFINITE_BREW.get())
                .unlockedBy(getHasName(MegaBotanyItems.INFINITE_BREW.get()), has(MegaBotanyItems.INFINITE_BREW.get()))
                .save(WrapperResult.ofType(RelicRecipe.SERIALIZER, consumer), new ResourceLocation(MegaBotany.MOD_ID, MegaBotanyItems.INFINITE_DRINK.getId().getPath() + "_from_" + MegaBotanyItems.INFINITE_BREW.getId().getPath()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.INFINITE_DRINK.get())
                .requires(MegaBotanyItems.INFINITE_SPLASH_BREW.get())
                .unlockedBy(getHasName(MegaBotanyItems.INFINITE_SPLASH_BREW.get()), has(MegaBotanyItems.INFINITE_SPLASH_BREW.get()))
                .save(WrapperResult.ofType(RelicRecipe.SERIALIZER, consumer), new ResourceLocation(MegaBotany.MOD_ID, MegaBotanyItems.INFINITE_DRINK.getId().getPath() + "_from_" + MegaBotanyItems.INFINITE_SPLASH_BREW.getId().getPath()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MegaBotanyItems.INFINITE_DRINK.get())
                .requires(MegaBotanyItems.INFINITE_LINGERING_BREW.get())
                .unlockedBy(getHasName(MegaBotanyItems.INFINITE_LINGERING_BREW.get()), has(MegaBotanyItems.INFINITE_LINGERING_BREW.get()))
                .save(WrapperResult.ofType(RelicRecipe.SERIALIZER, consumer), new ResourceLocation(MegaBotany.MOD_ID, MegaBotanyItems.INFINITE_DRINK.getId().getPath() + "_from_" + MegaBotanyItems.INFINITE_LINGERING_BREW.getId().getPath()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get())
                .requires(BotaniaBlocks.elvenSpreader)
                .requires(Items.REDSTONE)
                .unlockedBy(getHasName(BotaniaBlocks.elvenSpreader), has(BotaniaBlocks.elvenSpreader))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get())
                .requires(BotaniaBlocks.gaiaSpreader)
                .requires(Items.REDSTONE)
                .unlockedBy(getHasName(BotaniaBlocks.gaiaSpreader), has(BotaniaBlocks.gaiaSpreader))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.manaSpreader)
                .requires(BotaniaBlocks.redstoneSpreader)
                .unlockedBy(getHasName(BotaniaBlocks.redstoneSpreader), has(BotaniaBlocks.redstoneSpreader))
                .save(consumer, new ResourceLocation(MegaBotany.MOD_ID, LibBlockNames.SPREADER));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.elvenSpreader)
                .requires(MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get())
                .unlockedBy(getHasName(MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get()), has(MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get()))
                .save(consumer, new ResourceLocation(MegaBotany.MOD_ID, LibBlockNames.SPREADER_ELVEN));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BotaniaBlocks.gaiaSpreader)
                .requires(MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get())
                .unlockedBy(getHasName(MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get()), has(MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get()))
                .save(consumer, new ResourceLocation(MegaBotany.MOD_ID, LibBlockNames.SPREADER_GAIA));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MegaBotanyBlocks.SPIRIT_PORTAL.get())
                .pattern("DED")
                .pattern("DED")
                .pattern("DED")
                .define('D', BotaniaTags.Items.DREAMWOOD_LOGS)
                .define('E', BotaniaItems.lifeEssence)
                .unlockedBy(getHasName(BotaniaItems.lifeEssence), has(BotaniaItems.lifeEssence))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.MANAWEAVEDSTEEL_HELMET.get())
                .pattern("ESE")
                .pattern("SHS")
                .define('S', BotaniaItems.manaSteel)
                .define('H', BotaniaItems.manaweaveHelm)
                .define('E', BotaniaItems.lifeEssence)
                .unlockedBy(getHasName(BotaniaItems.lifeEssence), has(BotaniaItems.lifeEssence))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.MANAWEAVEDSTEEL_CHESTPLATE.get())
                .pattern("ESE")
                .pattern("SCS")
                .define('S', BotaniaItems.manaSteel)
                .define('C', BotaniaItems.manaweaveChest)
                .define('E', BotaniaItems.lifeEssence)
                .unlockedBy(getHasName(BotaniaItems.lifeEssence), has(BotaniaItems.lifeEssence))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.MANAWEAVEDSTEEL_LEGGINGS.get())
                .pattern("ESE")
                .pattern("SLS")
                .define('S', BotaniaItems.manaSteel)
                .define('L', BotaniaItems.manaweaveLegs)
                .define('E', BotaniaItems.lifeEssence)
                .unlockedBy(getHasName(BotaniaItems.lifeEssence), has(BotaniaItems.lifeEssence))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.MANAWEAVEDSTEEL_BOOTS.get())
                .pattern("ESE")
                .pattern("SBS")
                .define('S', BotaniaItems.manaSteel)
                .define('B', BotaniaItems.manaweaveBoots)
                .define('E', BotaniaItems.lifeEssence)
                .unlockedBy(getHasName(BotaniaItems.lifeEssence), has(BotaniaItems.lifeEssence))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.SHADOWIUM_HELMET.get())
                .pattern("SSS")
                .pattern("S S")
                .define('S', MegaBotanyItems.SHADOWIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.SHADOWIUM_INGOT.get()), has(MegaBotanyItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.SHADOWIUM_CHESTPLATE.get())
                .pattern("S S")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', MegaBotanyItems.SHADOWIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.SHADOWIUM_INGOT.get()), has(MegaBotanyItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.SHADOWIUM_LEGGINGS.get())
                .pattern("SSS")
                .pattern("S S")
                .pattern("S S")
                .define('S', MegaBotanyItems.SHADOWIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.SHADOWIUM_INGOT.get()), has(MegaBotanyItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.SHADOWIUM_BOOTS.get())
                .pattern("S S")
                .pattern("S S")
                .define('S', MegaBotanyItems.SHADOWIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.SHADOWIUM_INGOT.get()), has(MegaBotanyItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.SHADOWIUM_KATANA.get())
                .pattern("S")
                .pattern("S")
                .pattern("L")
                .define('S', MegaBotanyItems.SHADOWIUM_INGOT.get())
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(MegaBotanyItems.SHADOWIUM_INGOT.get()), has(MegaBotanyItems.SHADOWIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.PHOTONIUM_HELMET.get())
                .pattern("SSS")
                .pattern("S S")
                .define('S', MegaBotanyItems.PHOTONIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.PHOTONIUM_INGOT.get()), has(MegaBotanyItems.PHOTONIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.PHOTONIUM_CHESTPLATE.get())
                .pattern("S S")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', MegaBotanyItems.PHOTONIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.PHOTONIUM_INGOT.get()), has(MegaBotanyItems.PHOTONIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.PHOTONIUM_LEGGINGS.get())
                .pattern("SSS")
                .pattern("S S")
                .pattern("S S")
                .define('S', MegaBotanyItems.PHOTONIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.PHOTONIUM_INGOT.get()), has(MegaBotanyItems.PHOTONIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.PHOTONIUM_BOOTS.get())
                .pattern("S S")
                .pattern("S S")
                .define('S', MegaBotanyItems.PHOTONIUM_INGOT.get())
                .unlockedBy(getHasName(MegaBotanyItems.PHOTONIUM_INGOT.get()), has(MegaBotanyItems.PHOTONIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MegaBotanyItems.SHORT_PHOTONIUM_SWORD.get())
                .pattern("S")
                .pattern("S")
                .pattern("L")
                .define('S', MegaBotanyItems.PHOTONIUM_INGOT.get())
                .define('L', BotaniaItems.livingwoodTwig)
                .unlockedBy(getHasName(MegaBotanyItems.PHOTONIUM_INGOT.get()), has(MegaBotanyItems.PHOTONIUM_INGOT.get()))
                .save(consumer);
    }
}
