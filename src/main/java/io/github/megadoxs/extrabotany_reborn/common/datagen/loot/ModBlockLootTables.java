package io.github.megadoxs.extrabotany_reborn.common.datagen.loot;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
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

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.PHOTONIUM_BLOCK.get());
        this.dropSelf(ModBlocks.SHADOWIUM_BLOCK.get());
        this.dropSelf(ModBlocks.ORICHALCOS_BLOCK.get());

        //TODO all the flowers should save their important nbt, this current system doesn't do that
        this.dropSelf(MegaBotanyFlowerBlocks.bloodyEnchantress);
        this.dropSelf(MegaBotanyFlowerBlocks.bloodyEnchantressFloating);
        this.add(MegaBotanyFlowerBlocks.bloodyEnchantressPotted, createPotAndPlantItemTable(MegaBotanyFlowerBlocks.bloodyEnchantress));

        this.dropSelf(MegaBotanyFlowerBlocks.SunshineLily);
        this.dropSelf(MegaBotanyFlowerBlocks.SunshineLilyFloating);
        this.add(MegaBotanyFlowerBlocks.SunshineLilyPotted, createPotAndPlantItemTable(MegaBotanyFlowerBlocks.SunshineLily));

        this.dropSelf(ModBlocks.MORTAR.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new ArrayList<>();
        ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(blocks::add);

        for (Block b : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
            if (ExtraBotany_Reborn.MOD_ID.equals(id.getNamespace()) && (b instanceof FlowerBlock || b instanceof FloatingFlowerBlock || b instanceof FlowerPotBlock)) {
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
