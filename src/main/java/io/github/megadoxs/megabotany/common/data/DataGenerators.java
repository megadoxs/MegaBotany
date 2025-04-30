package io.github.megadoxs.megabotany.common.data;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.data.model.*;
import io.github.megadoxs.megabotany.common.data.recipe.ManaInfusionRecipeGenerator;
import io.github.megadoxs.megabotany.common.data.recipe.RunicAltarRecipeGenerator;
import io.github.megadoxs.megabotany.common.data.recipe.SpiritTradeRecipeGenerator;
import io.github.megadoxs.megabotany.common.data.recipe.TerrestrialAgglomerationRecipeGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MegaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new MegaBotanyRecipeGenerator(packOutput));
        generator.addProvider(event.includeServer(), new MegaBotanyAdvancementGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), MegaBotanyLootTableGenerator.create(packOutput));

        generator.addProvider(event.includeClient(), new MegaBotanyBlockStateGenerator(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new MegaBotanyItemModelGenerator(packOutput, existingFileHelper));

        generator.addProvider(event.includeServer(), new MegaBotanyGlobalLootModifiersGenerator(packOutput));

        MegaBotanyBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(), new MegaBotanyBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new MegaBotanyItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        // all bellow are botania stuff
        // will probably be included inside the ModRecipeGenerator eventually
        generator.addProvider(event.includeServer(), new ManaInfusionRecipeGenerator(packOutput));
        generator.addProvider(event.includeServer(), new RunicAltarRecipeGenerator(packOutput));
        generator.addProvider(event.includeServer(), new TerrestrialAgglomerationRecipeGenerator(packOutput));
        generator.addProvider(event.includeServer(), new SpiritTradeRecipeGenerator(packOutput));

        generator.addProvider(event.includeClient(), new FloatingFlowerModelGenerator(packOutput));
        generator.addProvider(event.includeClient(), new PottedPlantModelGenerator(packOutput));
    }
}
