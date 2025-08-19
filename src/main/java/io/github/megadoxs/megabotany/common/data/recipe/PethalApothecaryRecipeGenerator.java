package io.github.megadoxs.megabotany.common.data.recipe;

import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.data.recipes.PetalApothecaryProvider;

import java.util.function.Consumer;

public class PethalApothecaryRecipeGenerator extends PetalApothecaryProvider {
    public PethalApothecaryRecipeGenerator(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
        Ingredient white = tagIngr("petals/white");
        Ingredient orange = tagIngr("petals/orange");
        Ingredient magenta = tagIngr("petals/magenta");
        Ingredient lightBlue = tagIngr("petals/light_blue");
        Ingredient yellow = tagIngr("petals/yellow");
        Ingredient lime = tagIngr("petals/lime");
        Ingredient pink = tagIngr("petals/pink");
        Ingredient gray = tagIngr("petals/gray");
        Ingredient lightGray = tagIngr("petals/light_gray");
        Ingredient cyan = tagIngr("petals/cyan");
        Ingredient purple = tagIngr("petals/purple");
        Ingredient blue = tagIngr("petals/blue");
        Ingredient brown = tagIngr("petals/brown");
        Ingredient green = tagIngr("petals/green");
        Ingredient red = tagIngr("petals/red");
        Ingredient black = tagIngr("petals/black");
        Ingredient runeWater = Ingredient.of(BotaniaItems.runeWater);
        Ingredient runeFire = Ingredient.of(BotaniaItems.runeFire);
        Ingredient runeEarth = Ingredient.of(BotaniaItems.runeEarth);
        Ingredient runeAir = Ingredient.of(BotaniaItems.runeAir);
        Ingredient runeSpring = Ingredient.of(BotaniaItems.runeSpring);
        Ingredient runeSummer = Ingredient.of(BotaniaItems.runeSummer);
        Ingredient runeAutumn = Ingredient.of(BotaniaItems.runeAutumn);
        Ingredient runeWinter = Ingredient.of(BotaniaItems.runeWinter);
        Ingredient runeMana = Ingredient.of(BotaniaItems.runeMana);
        Ingredient runeLust = Ingredient.of(BotaniaItems.runeLust);
        Ingredient runeGluttony = Ingredient.of(BotaniaItems.runeGluttony);
        Ingredient runeGreed = Ingredient.of(BotaniaItems.runeGreed);
        Ingredient runeSloth = Ingredient.of(BotaniaItems.runeSloth);
        Ingredient runeWrath = Ingredient.of(BotaniaItems.runeWrath);
        Ingredient runeEnvy = Ingredient.of(BotaniaItems.runeEnvy);
        Ingredient runePride = Ingredient.of(BotaniaItems.runePride);

        Ingredient pixieDust = Ingredient.of(BotaniaItems.pixieDust);
        Ingredient redstoneRoot = Ingredient.of(BotaniaItems.redstoneRoot);
        Ingredient gaiaSpirit = Ingredient.of(BotaniaItems.lifeEssence);
        Ingredient overgrowthSeed = Ingredient.of(BotaniaItems.overgrowthSeed);

        consumer.accept(make(MegaBotanyFlowerBlocks.sunshineLily, yellow, yellow, orange, orange));
        consumer.accept(make(MegaBotanyFlowerBlocks.moonlightLily, purple, purple, black, black));
        consumer.accept(make(MegaBotanyFlowerBlocks.tinkle, lightBlue, red, yellow, green));
        consumer.accept(make(MegaBotanyFlowerBlocks.bellFlower, runeAir, yellow, yellow, green, green));
        consumer.accept(make(MegaBotanyFlowerBlocks.geminiOrchid, runeFire, runeWater, orange, orange, lightBlue, lightBlue));
        consumer.accept(make(MegaBotanyFlowerBlocks.edelweiss, runeWinter, white, white, lightBlue));
        consumer.accept(make(MegaBotanyFlowerBlocks.bloodyEnchantress, runeGluttony, runeSloth, red, red, red, redstoneRoot));
        consumer.accept(make(MegaBotanyFlowerBlocks.omniviolet, runeMana, runeEnvy, blue, blue, purple, purple, yellow, pixieDust));
        consumer.accept(make(MegaBotanyFlowerBlocks.reikarLily, runeMana, runeGreed, blue, blue, lightBlue, lightBlue, white, pixieDust));

        consumer.accept(make(MegaBotanyFlowerBlocks.annoyingFlower, runeGluttony, runeWater, redstoneRoot, red, white, white, black, black));
        consumer.accept(make(MegaBotanyFlowerBlocks.necrofleur, runeWrath, runeEnvy, redstoneRoot, pink, pink, green, green, brown));
        consumer.accept(make(MegaBotanyFlowerBlocks.mirrortunia, runeEnvy, runeAir, white, white, lightBlue, lightBlue, redstoneRoot));
        consumer.accept(make(MegaBotanyFlowerBlocks.enchantedOrchid, runeSpring, runePride, runeSloth, purple, purple, black, black, magenta, gaiaSpirit, overgrowthSeed, overgrowthSeed, overgrowthSeed));
        consumer.accept(make(MegaBotanyFlowerBlocks.stardustLotus, runeMana, runeSloth, runeGreed, runePride, purple, purple, black, black, magenta, gaiaSpirit, redstoneRoot));
        consumer.accept(make(MegaBotanyFlowerBlocks.manalinkium, runeMana, runeSloth, runeGreed, runePride, lightBlue, lightBlue, cyan, cyan, green, gaiaSpirit, redstoneRoot));
    }
}
