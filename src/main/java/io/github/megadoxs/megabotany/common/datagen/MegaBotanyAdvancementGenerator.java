package io.github.megadoxs.megabotany.common.datagen;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

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

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
            consumer.accept(ROOT);
            consumer.accept(EAT_NIGHTMARE_FUEL);
        }

        //General Advancement
        private static Advancement getAdvancement(String name, Advancement parent, ItemLike icon, FrameType frame, CriterionTriggerInstance criterion) {
            return Advancement.Builder.advancement().parent(parent).display(icon, Component.translatable("advancement.megabotany:" + name), Component.translatable("advancement.megabotany:" + name + ".desc"), null, frame, true, true, false).addCriterion("requirement", criterion).build(new ResourceLocation("megabotany:main/" + name));
        }

        public static CriterionTriggerInstance getEatCriterion(ItemLike item) {
            return ConsumeItemTrigger.TriggerInstance.usedItem(item);
        }
    }


}
