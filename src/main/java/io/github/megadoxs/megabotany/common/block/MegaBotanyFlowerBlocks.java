package io.github.megadoxs.megabotany.common.block;

import io.github.megadoxs.megabotany.api.block_entity.FlowerBindableFunctionalFlowerBlockEntity;
import io.github.megadoxs.megabotany.common.block.flower.functional.*;
import io.github.megadoxs.megabotany.common.block.flower.generating.*;
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
    public static final Block bloodyEnchantressPotted = MegaBotanyBlocks.flowerPot(bloodyEnchantress, 0);

    public static final Block sunshineLily = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.SUNSHINE_LILY);
    public static final Block sunshineLilyFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.SUNSHINE_LILY);
    public static final Block sunshineLilyPotted = MegaBotanyBlocks.flowerPot(sunshineLily, 0);

    public static final Block moonlightLily = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.MOONLIGHT_LILY);
    public static final Block moonlightLilyFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.MOONLIGHT_LILY);
    public static final Block moonlightLilyPotted = MegaBotanyBlocks.flowerPot(moonlightLily, 0);

    public static final Block omniviolet = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.OMNIVIOLET);
    public static final Block omnivioletFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.OMNIVIOLET);
    public static final Block omnivioletPotted = MegaBotanyBlocks.flowerPot(omniviolet, 0);

    public static final Block edelweiss = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.EDELWEISS);
    public static final Block edelweissFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.EDELWEISS);
    public static final Block edelweissPotted = MegaBotanyBlocks.flowerPot(edelweiss, 0);

    public static final Block tinkle = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.TINKLE);
    public static final Block tinkleFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.TINKLE);
    public static final Block tinklePotted = MegaBotanyBlocks.flowerPot(tinkle, 0);

    public static final Block bellFlower = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.BELL_FLOWER);
    public static final Block bellFlowerFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.BELL_FLOWER);
    public static final Block bellFlowerPotted = MegaBotanyBlocks.flowerPot(bellFlower, 0);

    public static final Block reikarLily = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.REIKAR_LILY);
    public static final Block reikarLilyFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.REIKAR_LILY);
    public static final Block reikarLilyPotted = MegaBotanyBlocks.flowerPot(reikarLily, 0);

    public static final Block stonesia = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.STONESIA);
    public static final Block stonesiaFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.STONESIA);
    public static final Block stonesiaPotted = MegaBotanyBlocks.flowerPot(stonesia, 0);

    public static final Block geminiOrchid = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.GEMINI_ORCHID);
    public static final Block geminiOrchidFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.GEMINI_ORCHID);
    public static final Block geminiOrchidPotted = MegaBotanyBlocks.flowerPot(geminiOrchid, 0);

    public static final Block enchantedOrchid = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.ENCHANTED_ORCHID);
    public static final Block enchantedOrchidFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.ENCHANTED_ORCHID);
    public static final Block enchantedOrchidPotted = MegaBotanyBlocks.flowerPot(enchantedOrchid, 0);

    public static final Block mirrortunia = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.MIRRORTUNIA);
    public static final Block mirrortuniaFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.MIRRORTUNIA);
    public static final Block mirrortuniaPotted = MegaBotanyBlocks.flowerPot(mirrortunia, 0);

    public static final Block annoyingFlower = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.ANNOYING_FLOWER);
    public static final Block annoyingFlowerFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.ANNOYING_FLOWER);
    public static final Block annoyingFlowerPotted = MegaBotanyBlocks.flowerPot(annoyingFlower, 0);

    public static final Block necrofleur = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.NECROFLEUR);
    public static final Block necrofleurFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.NECROFLEUR);
    public static final Block necrofleurPotted = MegaBotanyBlocks.flowerPot(necrofleur, 0);
    public static final Block necrofleurChibi = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.NECROFLEUR_CHIBI);
    public static final Block necrofleurChibiFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.NECROFLEUR_CHIBI);

    public static final Block stardustLotus = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.STARDUST_LOTUS);
    public static final Block stardustLotusFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.STARDUST_LOTUS);
    public static final Block stardustLotusPotted = MegaBotanyBlocks.flowerPot(stardustLotus, 0);

    public static final Block manalinkium = createSpecialFlowerBlock(BotaniaMobEffects.clear, 1, FLOWER_PROPS, () -> MegaBotanyFlowerBlocks.MANALINKIUM);
    public static final Block manalinkiumFloating = new FloatingSpecialFlowerBlock(FLOATING_PROPS, () -> MegaBotanyFlowerBlocks.MANALINKIUM);
    public static final Block manalinkiumPotted = MegaBotanyBlocks.flowerPot(manalinkium, 0);

    public static final BlockEntityType<BloodyEnchantressBlockEntity> BLOODY_ENCHANTRESS = ForgeXplatImpl.INSTANCE.createBlockEntityType(BloodyEnchantressBlockEntity::new, bloodyEnchantress, bloodyEnchantressFloating);
    public static final BlockEntityType<SunshineLilyBlockEntity> SUNSHINE_LILY = ForgeXplatImpl.INSTANCE.createBlockEntityType(SunshineLilyBlockEntity::new, sunshineLily, sunshineLilyFloating);
    public static final BlockEntityType<MoonlightLilyBlockEntity> MOONLIGHT_LILY = ForgeXplatImpl.INSTANCE.createBlockEntityType(MoonlightLilyBlockEntity::new, moonlightLily, moonlightLilyFloating);
    public static final BlockEntityType<OmnivioletBlockEntity> OMNIVIOLET = ForgeXplatImpl.INSTANCE.createBlockEntityType(OmnivioletBlockEntity::new, omniviolet, omnivioletFloating);
    public static final BlockEntityType<EdelweissBlockEntity> EDELWEISS = ForgeXplatImpl.INSTANCE.createBlockEntityType(EdelweissBlockEntity::new, edelweiss, edelweissFloating);
    public static final BlockEntityType<TinkleBlockEntity> TINKLE = ForgeXplatImpl.INSTANCE.createBlockEntityType(TinkleBlockEntity::new, tinkle, tinkleFloating);
    public static final BlockEntityType<BellFlowerBlockEntity> BELL_FLOWER = ForgeXplatImpl.INSTANCE.createBlockEntityType(BellFlowerBlockEntity::new, bellFlower, bellFlowerFloating);
    public static final BlockEntityType<ReikarLilyBlockEntity> REIKAR_LILY = ForgeXplatImpl.INSTANCE.createBlockEntityType(ReikarLilyBlockEntity::new, reikarLily, reikarLilyFloating);
    public static final BlockEntityType<StonesiaBlockEntity> STONESIA = ForgeXplatImpl.INSTANCE.createBlockEntityType(StonesiaBlockEntity::new, stonesia, stonesiaFloating);
    public static final BlockEntityType<GeminiOrchidBlockEntity> GEMINI_ORCHID = ForgeXplatImpl.INSTANCE.createBlockEntityType(GeminiOrchidBlockEntity::new, geminiOrchid, geminiOrchidFloating);
    public static final BlockEntityType<EnchantedOrchidBlockEntity> ENCHANTED_ORCHID = ForgeXplatImpl.INSTANCE.createBlockEntityType(EnchantedOrchidBlockEntity::new, enchantedOrchid, enchantedOrchidFloating);
    public static final BlockEntityType<MirrortuniaBlockEntity> MIRRORTUNIA = ForgeXplatImpl.INSTANCE.createBlockEntityType(MirrortuniaBlockEntity::new, mirrortunia, mirrortuniaFloating);
    public static final BlockEntityType<AnnoyingFlowerBlockEntity> ANNOYING_FLOWER = ForgeXplatImpl.INSTANCE.createBlockEntityType(AnnoyingFlowerBlockEntity::new, annoyingFlower, annoyingFlowerFloating);
    public static final BlockEntityType<NecrofleurBlockEntity> NECROFLEUR = ForgeXplatImpl.INSTANCE.createBlockEntityType(NecrofleurBlockEntity::new, necrofleur, necrofleurFloating);
    public static final BlockEntityType<NecrofleurBlockEntity.Mini> NECROFLEUR_CHIBI = ForgeXplatImpl.INSTANCE.createBlockEntityType(NecrofleurBlockEntity.Mini::new, necrofleurChibi, necrofleurChibiFloating);
    public static final BlockEntityType<StardustLotusBlockEntity> STARDUST_LOTUS = ForgeXplatImpl.INSTANCE.createBlockEntityType(StardustLotusBlockEntity::new, stardustLotus, stardustLotusFloating);
    public static final BlockEntityType<ManalinkiumBlockEntity> MANALINKIUM = ForgeXplatImpl.INSTANCE.createBlockEntityType(ManalinkiumBlockEntity::new, manalinkium, manalinkium);

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
        r.accept(bloodyEnchantress, new ResourceLocation("megabotany", "bloody_enchantress"));
        r.accept(bloodyEnchantressFloating, floating(new ResourceLocation("megabotany", "bloody_enchantress")));
        r.accept(bloodyEnchantressPotted, potted(new ResourceLocation("megabotany", "bloody_enchantress")));

        r.accept(sunshineLily, new ResourceLocation("megabotany", "sunshine_lily"));
        r.accept(sunshineLilyFloating, floating(new ResourceLocation("megabotany", "sunshine_lily")));
        r.accept(sunshineLilyPotted, potted(new ResourceLocation("megabotany", "sunshine_lily")));

        r.accept(moonlightLily, new ResourceLocation("megabotany", "moonlight_lily"));
        r.accept(moonlightLilyFloating, floating(new ResourceLocation("megabotany", "moonlight_lily")));
        r.accept(moonlightLilyPotted, potted(new ResourceLocation("megabotany", "moonlight_lily")));

        r.accept(omniviolet, new ResourceLocation("megabotany", "omniviolet"));
        r.accept(omnivioletFloating, floating(new ResourceLocation("megabotany", "omniviolet")));
        r.accept(omnivioletPotted, potted(new ResourceLocation("megabotany", "omniviolet")));

        r.accept(edelweiss, new ResourceLocation("megabotany", "edelweiss"));
        r.accept(edelweissFloating, floating(new ResourceLocation("megabotany", "edelweiss")));
        r.accept(edelweissPotted, potted(new ResourceLocation("megabotany", "edelweiss")));

        r.accept(tinkle, new ResourceLocation("megabotany", "tinkle"));
        r.accept(tinkleFloating, floating(new ResourceLocation("megabotany", "tinkle")));
        r.accept(tinklePotted, potted(new ResourceLocation("megabotany", "tinkle")));

        r.accept(bellFlower, new ResourceLocation("megabotany", "bell_flower"));
        r.accept(bellFlowerFloating, floating(new ResourceLocation("megabotany", "bell_flower")));
        r.accept(bellFlowerPotted, potted(new ResourceLocation("megabotany", "bell_flower")));

        r.accept(reikarLily, new ResourceLocation("megabotany", "reikar_lily"));
        r.accept(reikarLilyFloating, floating(new ResourceLocation("megabotany", "reikar_lily")));
        r.accept(reikarLilyPotted, potted(new ResourceLocation("megabotany", "reikar_lily")));

        r.accept(stonesia, new ResourceLocation("megabotany", "stonesia"));
        r.accept(stonesiaFloating, floating(new ResourceLocation("megabotany", "stonesia")));
        r.accept(stonesiaPotted, potted(new ResourceLocation("megabotany", "stonesia")));

        r.accept(geminiOrchid, new ResourceLocation("megabotany", "gemini_orchid"));
        r.accept(geminiOrchidFloating, floating(new ResourceLocation("megabotany", "gemini_orchid")));
        r.accept(geminiOrchidPotted, potted(new ResourceLocation("megabotany", "gemini_orchid")));

        r.accept(enchantedOrchid, new ResourceLocation("megabotany", "enchanted_orchid"));
        r.accept(enchantedOrchidFloating, floating(new ResourceLocation("megabotany", "enchanted_orchid")));
        r.accept(enchantedOrchidPotted, potted(new ResourceLocation("megabotany", "enchanted_orchid")));

        r.accept(mirrortunia, new ResourceLocation("megabotany", "mirrortunia"));
        r.accept(mirrortuniaFloating, floating(new ResourceLocation("megabotany", "mirrortunia")));
        r.accept(mirrortuniaPotted, potted(new ResourceLocation("megabotany", "mirrortunia")));

        r.accept(annoyingFlower, new ResourceLocation("megabotany", "annoying_flower"));
        r.accept(annoyingFlowerFloating, floating(new ResourceLocation("megabotany", "annoying_flower")));
        r.accept(annoyingFlowerPotted, potted(new ResourceLocation("megabotany", "annoying_flower")));

        r.accept(necrofleur, new ResourceLocation("megabotany", "necrofleur"));
        r.accept(necrofleurFloating, floating(new ResourceLocation("megabotany", "necrofleur")));
        r.accept(necrofleurPotted, potted(new ResourceLocation("megabotany", "necrofleur")));
        r.accept(necrofleurChibi, chibi(new ResourceLocation("megabotany", "necrofleur")));
        r.accept(necrofleurChibiFloating, floating(chibi(new ResourceLocation("megabotany", "necrofleur"))));

        r.accept(stardustLotus, new ResourceLocation("megabotany", "stardust_lotus"));
        r.accept(stardustLotusFloating, floating(new ResourceLocation("megabotany", "stardust_lotus")));
        r.accept(stardustLotusPotted, potted(new ResourceLocation("megabotany", "stardust_lotus")));

        r.accept(manalinkium, new ResourceLocation("megabotany", "manalinkium"));
        r.accept(manalinkiumFloating, floating(new ResourceLocation("megabotany", "manalinkium")));
        r.accept(manalinkiumPotted, potted(new ResourceLocation("megabotany", "manalinkium")));
    }

    public static void registerTEs(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        r.accept(BLOODY_ENCHANTRESS, getId(bloodyEnchantress));
        r.accept(SUNSHINE_LILY, getId(sunshineLily));
        r.accept(MOONLIGHT_LILY, getId(moonlightLily));
        r.accept(OMNIVIOLET, getId(omniviolet));
        r.accept(EDELWEISS, getId(edelweiss));
        r.accept(TINKLE, getId(tinkle));
        r.accept(BELL_FLOWER, getId(bellFlower));
        r.accept(REIKAR_LILY, getId(reikarLily));
        r.accept(STONESIA, getId(stonesia));
        r.accept(GEMINI_ORCHID, getId(geminiOrchid));
        r.accept(ENCHANTED_ORCHID, getId(enchantedOrchid));
        r.accept(MIRRORTUNIA, getId(mirrortunia));
        r.accept(ANNOYING_FLOWER, getId(annoyingFlower));
        r.accept(NECROFLEUR, getId(necrofleur));
        r.accept(NECROFLEUR_CHIBI, getId(necrofleurChibi));
        r.accept(STARDUST_LOTUS, getId(stardustLotus));
        r.accept(MANALINKIUM, getId(manalinkium));
    }

    public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
        Item.Properties props = BotaniaItems.defaultBuilder();

        r.accept(new SpecialFlowerBlockItem(bloodyEnchantress, props), getId(bloodyEnchantress));
        r.accept(new SpecialFlowerBlockItem(bloodyEnchantressFloating, props), getId(bloodyEnchantressFloating));

        r.accept(new SpecialFlowerBlockItem(sunshineLily, props), getId(sunshineLily));
        r.accept(new SpecialFlowerBlockItem(sunshineLilyFloating, props), getId(sunshineLilyFloating));

        r.accept(new SpecialFlowerBlockItem(moonlightLily, props), getId(moonlightLily));
        r.accept(new SpecialFlowerBlockItem(moonlightLilyFloating, props), getId(moonlightLilyFloating));

        r.accept(new SpecialFlowerBlockItem(omniviolet, props), getId(omniviolet));
        r.accept(new SpecialFlowerBlockItem(omnivioletFloating, props), getId(omnivioletFloating));

        r.accept(new SpecialFlowerBlockItem(edelweiss, props), getId(edelweiss));
        r.accept(new SpecialFlowerBlockItem(edelweissFloating, props), getId(edelweissFloating));

        r.accept(new SpecialFlowerBlockItem(tinkle, props), getId(tinkle));
        r.accept(new SpecialFlowerBlockItem(tinkleFloating, props), getId(tinkleFloating));

        r.accept(new SpecialFlowerBlockItem(bellFlower, props), getId(bellFlower));
        r.accept(new SpecialFlowerBlockItem(bellFlowerFloating, props), getId(bellFlowerFloating));

        r.accept(new SpecialFlowerBlockItem(reikarLily, props), getId(reikarLily));
        r.accept(new SpecialFlowerBlockItem(reikarLilyFloating, props), getId(reikarLilyFloating));

        r.accept(new SpecialFlowerBlockItem(stonesia, props), getId(stonesia));
        r.accept(new SpecialFlowerBlockItem(stonesiaFloating, props), getId(stonesiaFloating));

        r.accept(new SpecialFlowerBlockItem(geminiOrchid, props), getId(geminiOrchid));
        r.accept(new SpecialFlowerBlockItem(geminiOrchidFloating, props), getId(geminiOrchidFloating));

        r.accept(new SpecialFlowerBlockItem(enchantedOrchid, props), getId(enchantedOrchid));
        r.accept(new SpecialFlowerBlockItem(enchantedOrchidFloating, props), getId(enchantedOrchidFloating));

        r.accept(new SpecialFlowerBlockItem(mirrortunia, props), getId(mirrortunia));
        r.accept(new SpecialFlowerBlockItem(mirrortuniaFloating, props), getId(mirrortuniaFloating));

        r.accept(new SpecialFlowerBlockItem(annoyingFlower, props), getId(annoyingFlower));
        r.accept(new SpecialFlowerBlockItem(annoyingFlowerFloating, props), getId(annoyingFlowerFloating));

        r.accept(new SpecialFlowerBlockItem(necrofleur, props), getId(necrofleur));
        r.accept(new SpecialFlowerBlockItem(necrofleurFloating, props), getId(necrofleurFloating));
        r.accept(new SpecialFlowerBlockItem(necrofleurChibi, props), getId(necrofleurChibi));
        r.accept(new SpecialFlowerBlockItem(necrofleurChibiFloating, props), getId(necrofleurChibiFloating));

        r.accept(new SpecialFlowerBlockItem(stardustLotus, props), getId(stardustLotus));
        r.accept(new SpecialFlowerBlockItem(stardustLotusFloating, props), getId(stardustLotusFloating));

        r.accept(new SpecialFlowerBlockItem(manalinkium, props), getId(manalinkium));
        r.accept(new SpecialFlowerBlockItem(manalinkiumFloating, props), getId(manalinkiumFloating));
    }

    private static ResourceLocation getId(Block b) {
        return ForgeRegistries.BLOCKS.getKey(b);
    }

    public static void registerWandHudCaps(BotaniaBlockEntities.BECapConsumer<WandHUD> consumer) {
        consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((GeneratingFlowerBlockEntity) be),
                BLOODY_ENCHANTRESS, SUNSHINE_LILY, MOONLIGHT_LILY, OMNIVIOLET, EDELWEISS, TINKLE, BELL_FLOWER, REIKAR_LILY, STONESIA, GEMINI_ORCHID);
        consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((FunctionalFlowerBlockEntity) be),
                ENCHANTED_ORCHID, MIRRORTUNIA, ANNOYING_FLOWER, NECROFLEUR, NECROFLEUR_CHIBI);
        consumer.accept(be -> new FlowerBindableFunctionalFlowerBlockEntity.WandHud<>((FlowerBindableFunctionalFlowerBlockEntity<? extends FunctionalFlowerBlockEntity>) be),
                STARDUST_LOTUS);
        consumer.accept(be -> new ManalinkiumBlockEntity.WandHud<>((ManalinkiumBlockEntity) be),
                MANALINKIUM);
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
