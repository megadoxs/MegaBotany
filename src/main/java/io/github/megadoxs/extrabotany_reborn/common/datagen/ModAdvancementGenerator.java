package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
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

public class ModAdvancementGenerator extends ForgeAdvancementProvider {
    public ModAdvancementGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Advancements()));
    }

    public static class Advancements implements AdvancementGenerator{

        public static final Advancement ROOT = Advancement.Builder.advancement().display(ModItems.TERRASTEEL_HAMMER.get(), Component.translatable("advancement.extrabotany_reborn:root"), Component.translatable("advancement.extrabotany_reborn:root.desc"), new ResourceLocation("botania:textures/block/livingwood_log.png"), FrameType.TASK, false, false, false).addCriterion("test", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItems.NIGHTMARE_FUEL.get()).build())).build(new ResourceLocation("extrabotany_reborn:main/root"));
        public static final Advancement EAT_NIGHTMARE_FUEL = getAdvancement("eat_nightmare_fuel", ROOT, ModItems.NIGHTMARE_FUEL.get(), FrameType.TASK, getEatCriterion(ModItems.NIGHTMARE_FUEL.get()));
        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
            consumer.accept(ROOT);
            consumer.accept(EAT_NIGHTMARE_FUEL);
        }

        //General Advancement
        private static Advancement getAdvancement(String name, Advancement parent, ItemLike icon, FrameType frame, CriterionTriggerInstance criterion){
            return Advancement.Builder.advancement().parent(parent).display(icon, Component.translatable("advancement.extrabotany_reborn:" + name), Component.translatable("advancement.extrabotany_reborn:" + name + ".desc"), null, frame, true, true, false).addCriterion("requirement", criterion).build(new ResourceLocation("extrabotany_reborn:main/" + name));
        }

        public static CriterionTriggerInstance getEatCriterion(ItemLike item){
            return ConsumeItemTrigger.TriggerInstance.usedItem(item);
        }
    }



}
