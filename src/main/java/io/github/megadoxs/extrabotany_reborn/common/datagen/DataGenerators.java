package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.datagen.botania.ManaInfusionRecipeGenerator;
import io.github.megadoxs.extrabotany_reborn.common.datagen.botania.RunicAltarRecipeGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ExtraBotany_Reborn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModRecipeGenerator(packOutput));
        generator.addProvider(event.includeServer(), new ModAdvancementGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), ModLootTableGenerator.create(packOutput));

        generator.addProvider(event.includeClient(), new ModBlockStateGenerator(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelGenerator(packOutput, existingFileHelper));

        ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(), new ModBlockTagGenerator(packOutput, lookupProvider,existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        // will probably be included inside the ModRecipeGenerator eventually
        generator.addProvider(event.includeServer(), new ManaInfusionRecipeGenerator(packOutput));
        generator.addProvider(event.includeServer(), new RunicAltarRecipeGenerator(packOutput));
    }
}
