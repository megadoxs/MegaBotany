package io.github.megadoxs.extrabotany_reborn.common.block;

import io.github.megadoxs.extrabotany_reborn.common.block.flower.generating.BloodyEnchantressBlockEntity;
import io.github.megadoxs.extrabotany_reborn.common.block.flower.generating.SunshineLilyBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.FloatingSpecialFlowerBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.brew.BotaniaMobEffects;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.forge.xplat.ForgeXplatImpl;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MegaBotanyFlowerBlocks {
    public static final BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY);
    private static final BlockBehaviour.Properties FLOATING_PROPS = BotaniaBlocks.FLOATING_PROPS;

    public static final Block bloodyEnchantress = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.BLOODY_ENCHANTRESS);
    public static final Block bloodyEnchantressFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.BLOODY_ENCHANTRESS);
    public static final Block bloodyEnchantressPotted = ModBlocks.flowerPot(bloodyEnchantress, 0);

    public static final Block SunshineLily = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.SUNSHINE_LILY);
    public static final Block SunshineLilyFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.SUNSHINE_LILY);
    public static final Block SunshineLilyPotted = ModBlocks.flowerPot(SunshineLily, 0);

    public static final BlockEntityType<SunshineLilyBlockEntity> SUNSHINE_LILY = ForgeXplatImpl.INSTANCE.createBlockEntityType(SunshineLilyBlockEntity::new, SunshineLily, SunshineLilyFloating);
    public static final BlockEntityType<BloodyEnchantressBlockEntity> BLOODY_ENCHANTRESS = ForgeXplatImpl.INSTANCE.createBlockEntityType(BloodyEnchantressBlockEntity::new, bloodyEnchantress, bloodyEnchantressFloating);

    private static ResourceLocation floating(ResourceLocation orig) {
        return new ResourceLocation(orig.getNamespace(), "floating_" + orig.getPath());
    }

    private static ResourceLocation potted(ResourceLocation orig) {
        return new ResourceLocation(orig.getNamespace(), "potted_" + orig.getPath());
    }

    private static ResourceLocation chibi(ResourceLocation orig) {
        return new ResourceLocation(orig.getNamespace(), orig.getPath() + "_chibi");
    }

    public static FlowerBlock createSpecialFlowerBlock(
            MobEffect effect, int effectDuration,
            BlockBehaviour.Properties props,
            Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType) {
        return ForgeXplatImpl.INSTANCE.createSpecialFlowerBlock(
                effect, effectDuration, props, beType);
    }

    public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
        r.accept(bloodyEnchantress, new ResourceLocation("extrabotany_reborn", "bloody_enchantress"));
        r.accept(bloodyEnchantressFloating, floating(new ResourceLocation("extrabotany_reborn", "bloody_enchantress")));
        r.accept(bloodyEnchantressPotted, potted(new ResourceLocation("extrabotany_reborn", "bloody_enchantress")));

        r.accept(SunshineLily, new ResourceLocation("extrabotany_reborn", "sunshine_lily"));
        r.accept(SunshineLilyFloating, floating(new ResourceLocation("extrabotany_reborn", "sunshine_lily")));
        r.accept(SunshineLilyPotted, potted(new ResourceLocation("extrabotany_reborn", "sunshine_lily")));
    }

    public static void registerTEs(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        r.accept(BLOODY_ENCHANTRESS, getId(bloodyEnchantress));
        r.accept(SUNSHINE_LILY, getId(SunshineLily));
    }

    public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
        Item.Properties props = BotaniaItems.defaultBuilder();

        r.accept(new SpecialFlowerBlockItem(bloodyEnchantress, props), getId(bloodyEnchantress));
        r.accept(new SpecialFlowerBlockItem(bloodyEnchantressFloating, props), getId(bloodyEnchantressFloating));

        r.accept(new SpecialFlowerBlockItem(SunshineLily, props), getId(SunshineLily));
        r.accept(new SpecialFlowerBlockItem(SunshineLilyFloating, props), getId(SunshineLilyFloating));
    }

    private static ResourceLocation getId(Block b) {
        return ForgeRegistries.BLOCKS.getKey(b);
    }

    public static void registerWandHudCaps(BotaniaBlockEntities.BECapConsumer<WandHUD> consumer) {
        consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((GeneratingFlowerBlockEntity) be),
                BLOODY_ENCHANTRESS, SUNSHINE_LILY);
        //consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((FunctionalFlowerBlockEntity) be), BLOODY_ENCHANTRESS);
    }

    public static void registerFlowerPotPlants(BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer) {
        registerBlocks((block, resourceLocation) -> {
            if (block instanceof FlowerPotBlock) {
                var id = getId(block);
                consumer.accept(new ResourceLocation(id.getNamespace(), id.getPath().substring(LibBlockNames.POTTED_PREFIX.length())), () -> block);
            }
        });
    }
}
