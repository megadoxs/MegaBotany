package io.github.megadoxs.megabotany.common.datagen.botania;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.megadoxs.megabotany.common.MegaBotany;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PottedPlantModelGenerator implements DataProvider {
    private final PackOutput packOutput;

    public PottedPlantModelGenerator(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<Tuple<String, JsonElement>> jsons = new ArrayList<>();
        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(b);
            if (MegaBotany.MOD_ID.equals(blockId.getNamespace()) && b instanceof FlowerPotBlock) {
                String name = blockId.getPath();
                String nonPotted = name.replace(LibBlockNames.POTTED_PREFIX, "");

                JsonObject obj = new JsonObject();
                obj.addProperty("parent", "minecraft:block/flower_pot_cross");
                obj.addProperty("render_type", "minecraft:cutout");
                JsonObject textures = new JsonObject();
                textures.addProperty("plant", MegaBotany.MOD_ID + ":block/" + nonPotted);
                obj.add("textures", textures);
                jsons.add(new Tuple<>(name, obj));
            }
        }
        List<CompletableFuture<?>> output = new ArrayList<>();
        PackOutput.PathProvider blocks = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
        for (Tuple<String, JsonElement> pair : jsons) {
            output.add(DataProvider.saveStable(cache, pair.getB(), blocks.json(new ResourceLocation(MegaBotany.MOD_ID, pair.getA()))));
        }

        return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
    }

    @NotNull
    @Override
    public String getName() {
        return "ExtraBotany: potted plant models";
    }
}
