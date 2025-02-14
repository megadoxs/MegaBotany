package io.github.megadoxs.megabotany.common.datagen.loot;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MegaBotanyBlockLootTables extends BlockLootSubProvider {
    public MegaBotanyBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(MegaBotanyBlocks.PHOTONIUM_BLOCK.get());
        dropSelf(MegaBotanyBlocks.SHADOWIUM_BLOCK.get());
        dropSelf(MegaBotanyBlocks.ORICHALCOS_BLOCK.get());

        dropSelf(MegaBotanyBlocks.MORTAR.get());
        dropSelf(MegaBotanyBlocks.PEDESTAL.get());

        dropSelf(MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get());
        dropSelf(MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get());

        //TODO all the flowers should save their important nbt, this current system doesn't do that
        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (MegaBotany.MOD_ID.equals(id.getNamespace()) && (b instanceof FlowerBlock || b instanceof FloatingFlowerBlock)) {
                dropSelf(b);
            }
        }

        // will stay, don't need to save any data
        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (MegaBotany.MOD_ID.equals(id.getNamespace()) && b instanceof FlowerPotBlock) {
                add(b, createPotAndPlantItemTable(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id.getNamespace(), id.getPath().replace("potted_", "")))));
            }
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new ArrayList<>();
        MegaBotanyBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(blocks::add);

        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (MegaBotany.MOD_ID.equals(id.getNamespace()) && (b instanceof FlowerBlock || b instanceof FloatingFlowerBlock || b instanceof FlowerPotBlock)) {
                blocks.add(b);
            }
        }

        return blocks;
    }

    //from botania's BlockLootProvider
    protected static LootTable.Builder createPotAndPlantItemTable(ItemLike plant) {
        final var potPool = LootPool.lootPool().add(LootItem.lootTableItem(Blocks.FLOWER_POT))
                .setRolls(ConstantValue.exactly(1.0f))
                .when(ExplosionCondition.survivesExplosion());
        final var plantPool = LootPool.lootPool().add(LootItem.lootTableItem(plant))
                .setRolls(ConstantValue.exactly(1.0f))
                .when(ExplosionCondition.survivesExplosion());
        return LootTable.lootTable().withPool(potPool).withPool(plantPool);
    }
}
