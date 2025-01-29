package io.github.megadoxs.megabotany.common.datagen;

import io.github.megadoxs.megabotany.common.entity.MegaBotanyEntities;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.checkerframework.checker.units.qual.A;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MegaBotanyAdvancementGenerator extends ForgeAdvancementProvider {
    public MegaBotanyAdvancementGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Advancements()));
    }

    public static class Advancements implements AdvancementGenerator {

        public static final Advancement ROOT = Advancement.Builder.advancement().display(MegaBotanyItems.TERRASTEEL_HAMMER.get(), Component.translatable("advancement.megabotany:root"), Component.translatable("advancement.megabotany:root.desc"), new ResourceLocation("botania:textures/block/livingwood_log.png"), FrameType.TASK, false, false, false).addCriterion("test", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(MegaBotanyItems.NIGHTMARE_FUEL.get()).build())).build(new ResourceLocation("megabotany:main/root"));
        public static final Advancement EAT_NIGHTMARE_FUEL = getAdvancement("eat_nightmare_fuel", ROOT, MegaBotanyItems.NIGHTMARE_FUEL.get(), FrameType.TASK, getEatCriterion(MegaBotanyItems.NIGHTMARE_FUEL.get()));
        public static final Advancement KILL_GAIA_III = getAdvancement("kill_gaia_iii", ROOT, BotaniaBlocks.gaiaHead, FrameType.CHALLENGE, KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MegaBotanyEntities.GAIA_GUARDIAN_III.get()))); //TODO change temp advancement icon
        public static final Advancement UNLOCK_INFINITE_DRINK = getRelicAdvancement(MegaBotanyItems.INFINITE_DRINK.get(), KILL_GAIA_III);
        public static final Advancement UNLOCK_EXCALIBER = getRelicAdvancement(MegaBotanyItems.EXCALIBER.get(), KILL_GAIA_III);
        public static final Advancement UNLOCK_ACHILLED_SHIELD = getRelicAdvancement(MegaBotanyItems.ACHILLED_SHIELD.get(), KILL_GAIA_III);
        public static final Advancement UNLOCK_ALL_FOR_ONE = getRelicAdvancement(MegaBotanyItems.ALL_FOR_ONE.get(), KILL_GAIA_III);
        public static final Advancement UNLOCK_FAILNAUGHT = getRelicAdvancement(MegaBotanyItems.FAILNAUGHT.get(), KILL_GAIA_III);
        public static final Advancement UNLOCK_ABSOLUTION_PENDANT = getRelicAdvancement(MegaBotanyItems.ABSOLUTION_PENDANT.get(), KILL_GAIA_III);

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
            consumer.accept(ROOT);
            consumer.accept(EAT_NIGHTMARE_FUEL);
            consumer.accept(KILL_GAIA_III);
            consumer.accept(UNLOCK_INFINITE_DRINK);
            consumer.accept(UNLOCK_EXCALIBER);
            consumer.accept(UNLOCK_ACHILLED_SHIELD);
            consumer.accept(UNLOCK_ALL_FOR_ONE);
            consumer.accept(UNLOCK_FAILNAUGHT);
            consumer.accept(UNLOCK_ABSOLUTION_PENDANT);
        }

        //General Advancement
        private static Advancement getAdvancement(String name, Advancement parent, ItemLike icon, FrameType frame, CriterionTriggerInstance criterion) {
            return Advancement.Builder.advancement().parent(parent).display(icon, Component.translatable("advancement.megabotany:" + name), Component.translatable("advancement.megabotany:" + name + ".desc"), null, frame, true, true, false).addCriterion("requirement", criterion).build(new ResourceLocation("megabotany:main/" + name));
        }

        public static CriterionTriggerInstance getEatCriterion(ItemLike item) {
            return ConsumeItemTrigger.TriggerInstance.usedItem(item);
        }

        public static Advancement getRelicAdvancement(Item relic, Advancement parent) {
            return Advancement.Builder.advancement().parent(parent).display(relic, Component.translatable("advancement.megabotany:" + BuiltInRegistries.ITEM.getKey(relic).getPath()), Component.translatable("advancement.megabotany:" + BuiltInRegistries.ITEM.getKey(relic).getPath() + ".desc"), null, FrameType.CHALLENGE, true, true, false).addCriterion("relic", new RelicBindTrigger.Instance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(relic).build())).build(new ResourceLocation("megabotany:main/" + relic));
        }
    }


}
