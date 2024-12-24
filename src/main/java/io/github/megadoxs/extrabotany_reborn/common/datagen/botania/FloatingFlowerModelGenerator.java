package io.github.megadoxs.extrabotany_reborn.common.datagen.botania;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.xplat.ClientXplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class FloatingFlowerModelGenerator implements DataProvider {
    private final PackOutput packOutput;

    public FloatingFlowerModelGenerator(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<Tuple<String, JsonElement>> jsons = new ArrayList<>();
        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (ExtraBotany_Reborn.MOD_ID.equals(id.getNamespace()) && b instanceof FloatingFlowerBlock) {
                String name = id.getPath();
                String nonFloat;
                if (name.endsWith("_floating_flower")) {
                    nonFloat = name.replace("_floating_flower", "_mystical_flower");
                } else {
                    nonFloat = name.replace("floating_", "");
                }

                JsonObject obj = new JsonObject();
                obj.addProperty("parent", "minecraft:block/block");
                obj.addProperty("loader", ClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID.toString());
                JsonObject flower = new JsonObject();
                flower.addProperty("parent",  ExtraBotany_Reborn.MOD_ID + ":block/" + nonFloat);
                obj.add("flower", flower);
                jsons.add(new Tuple<>(name, obj));
            }
        }
        List<CompletableFuture<?>> output = new ArrayList<>();
        PackOutput.PathProvider blocks = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
        PackOutput.PathProvider items = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
        for (Tuple<String, JsonElement> pair : jsons) {
            output.add(DataProvider.saveStable(cache, pair.getB(), blocks.json(new ResourceLocation(ExtraBotany_Reborn.MOD_ID , pair.getA()))));
            output.add(DataProvider.saveStable(cache, pair.getB(), items.json(new ResourceLocation(ExtraBotany_Reborn.MOD_ID , pair.getA()))));
        }

        return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "ExtraBotany: Floating Flower Models";
    }
}
