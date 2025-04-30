package io.github.megadoxs.megabotany.common.data;

import io.github.megadoxs.megabotany.common.entity.MegaBotanyEntities;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.entity.BotaniaEntities;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MegaBotanyAdvancementGenerator extends ForgeAdvancementProvider {
    public MegaBotanyAdvancementGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Advancements()));
    }

    public static class Advancements implements AdvancementGenerator {

        public static final Advancement ROOT = Advancement.Builder.advancement().display(MegaBotanyItems.EARTH_ESSENCE.get(), Component.translatable("advancement.megabotany:root"), Component.translatable("advancement.megabotany:root.desc"), new ResourceLocation("botania:textures/block/livingwood_log.png"), FrameType.TASK, false, false, false).addCriterion("kill_gaia_2", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(BotaniaEntities.DOPPLEGANGER).nbt(new NbtPredicate(new CompoundTag() {{ putBoolean("hardMode", true); }})).build())).build(new ResourceLocation("megabotany:main/root"));
        public static final Advancement OBTAIN_NIGHTMARE_FUEL = getObtainItemAdvancement("obtain_nightmare_fuel", ROOT, MegaBotanyItems.NIGHTMARE_FUEL.get());
        public static final Advancement OBTAIN_SPIRIT_FUEL = getObtainItemAdvancement("obtain_spirit_fuel", ROOT, MegaBotanyItems.SPIRIT_FUEL.get());
        public static final Advancement OBTAIN_SHADOWIUM_INGOT = getObtainItemAdvancement("obtain_shadowium_ingot", OBTAIN_NIGHTMARE_FUEL, MegaBotanyItems.SHADOWIUM_INGOT.get());
        public static final Advancement OBTAIN_PHOTINIUM_INGOT = getObtainItemAdvancement("obtain_photonium_ingot", OBTAIN_SPIRIT_FUEL, MegaBotanyItems.PHOTONIUM_INGOT.get());
//        public static final Advancement WEAR_FULL_SHADOWIUM_ARMOR = getAdvancement("wear_full_shadowium_armor", OBTAIN_SHADOWIUM_INGOT, MegaBotanyItems.SHADOWIUM_CHESTPLATE.get(), FrameType.TASK, );
//        public static final Advancement WEAR_FULL_PHOTONIUM_ARMOR = getAdvancement("wear_full_photonium_armor", OBTAIN_PHOTINIUM_INGOT, MegaBotanyItems.PHOTONIUM_CHESTPLATE.get(), FrameType.TASK, InventoryChangeTrigger.TriggerInstance.hasItems());
        public static final Advancement KILL_GAIA_III = getAdvancement("kill_gaia_iii", ROOT, BotaniaBlocks.gaiaHead, FrameType.CHALLENGE, KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MegaBotanyEntities.GAIA_GUARDIAN_III.get()))); //TODO change temp advancement icon
        public static final Advancement OBTAIN_ORICHALCOS_INGOT = getObtainItemAdvancement("obtain_orichalcos_ingot", KILL_GAIA_III, MegaBotanyItems.ORICHALCOS_INGOT.get());

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {

            Advancement challenge = Advancement.Builder.advancement().display(MegaBotanyItems.PANDORA_BOX.get(), Component.translatable("advancement.megabotany.challenge"), Component.translatable("advancement.megabotany.challenge.desc"), new ResourceLocation("botania:textures/block/livingrock_bricks.png"), FrameType.CHALLENGE, false, false, false).addCriterion("kill_gaia_2", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(BotaniaEntities.DOPPLEGANGER).nbt(new NbtPredicate(new CompoundTag() {{ putBoolean("hardMode", true); }})).build())).save(consumer, new ResourceLocation("megabotany:challenge/root"), existingFileHelper);
            getRelicAdvancement(MegaBotanyItems.INFINITE_DRINK.get(), challenge);
            getRelicAdvancement(MegaBotanyItems.EXCALIBER.get(), challenge);
            getRelicAdvancement(MegaBotanyItems.ACHILLED_SHIELD.get(), challenge);
            getRelicAdvancement(MegaBotanyItems.ALL_FOR_ONE.get(), challenge);
            getRelicAdvancement(MegaBotanyItems.FAILNAUGHT.get(), challenge);
            getRelicAdvancement(MegaBotanyItems.ABSOLUTION_PENDANT.get(), challenge);

            consumer.accept(ROOT);
            consumer.accept(OBTAIN_NIGHTMARE_FUEL);
            consumer.accept(OBTAIN_SPIRIT_FUEL);
            consumer.accept(OBTAIN_SHADOWIUM_INGOT);
            consumer.accept(OBTAIN_PHOTINIUM_INGOT);
//            consumer.accept(WEAR_FULL_SHADOWIUM_ARMOR);
//            consumer.accept(WEAR_FULL_PHOTONIUM_ARMOR);
            consumer.accept(KILL_GAIA_III);
            consumer.accept(OBTAIN_ORICHALCOS_INGOT);
        }

        //General Advancement
        private static Advancement getAdvancement(String name, Advancement parent, ItemLike icon, FrameType frame, CriterionTriggerInstance criterion) {
            return Advancement.Builder.advancement().parent(parent).display(icon, Component.translatable("advancement.megabotany:" + name), Component.translatable("advancement.megabotany:" + name + ".desc"), null, frame, true, true, false).addCriterion("requirement", criterion).build(new ResourceLocation("megabotany:main/" + name));
        }

        private static Advancement getObtainItemAdvancement(String name, Advancement parent, ItemLike item) {
            return getAdvancement(name, parent, item, FrameType.TASK, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build()));
        }

        private static Advancement getRelicAdvancement(Item relic, Advancement parent) {
            return Advancement.Builder.advancement().parent(parent).display(relic, Component.translatable("advancement.megabotany:" + BuiltInRegistries.ITEM.getKey(relic).getPath()), Component.translatable("advancement.megabotany:" + BuiltInRegistries.ITEM.getKey(relic).getPath() + ".desc"), null, FrameType.CHALLENGE, true, true, false).addCriterion("relic", new RelicBindTrigger.Instance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(relic).build())).build(new ResourceLocation("megabotany:challenge/" + relic));
        }
    }


}
