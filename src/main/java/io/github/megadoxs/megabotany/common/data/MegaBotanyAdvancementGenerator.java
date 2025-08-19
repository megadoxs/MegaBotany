package io.github.megadoxs.megabotany.common.data;

import io.github.megadoxs.megabotany.common.advancements.SpiritPortalTrigger;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.entity.MegaBotanyEntities;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import vazkii.botania.common.advancements.RelicBindTrigger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MegaBotanyAdvancementGenerator extends ForgeAdvancementProvider {
    public MegaBotanyAdvancementGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Advancements()));
    }

    public static class Advancements implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
            Advancement root = Advancement.Builder.advancement().display(MegaBotanyItems.EARTH_ESSENCE.get(), Component.translatable("advancement.megabotany:root"), Component.translatable("advancement.megabotany:root.desc"), new ResourceLocation("botania:textures/block/dreamwood_log.png"), FrameType.TASK, true, true, false).addCriterion("obtain_earth_essence", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(MegaBotanyItems.EARTH_ESSENCE.get()).build())).save(consumer, "megabotany:root");

            Advancement spirit_portal = Advancement.Builder.advancement().parent(root).display(MegaBotanyBlocks.SPIRIT_PORTAL.get(), Component.translatable("advancement.megabotany:spirit_portal"), Component.translatable("advancement.megabotany:spirit_portal.desc"), null, FrameType.TASK, true, true, false).addCriterion("spirit_portal", new SpiritPortalTrigger.Instance(ContextAwarePredicate.ANY, ItemPredicate.ANY, LocationPredicate.ANY)).save(consumer, "megabotany:spirit_portal");
            armorSetAdvancement(MegaBotanyItems.SHADOWIUM_HELMET.get(), spirit_portal, consumer, "shadowium_set");
            armorSetAdvancement(MegaBotanyItems.PHOTONIUM_HELMET.get(), spirit_portal, consumer, "photonium_set");
            Advancement ticket = obtainItemAdvancement(MegaBotanyItems.TICKET.get(), spirit_portal, consumer);

            Advancement kill_gaia_iii = advancement("kill_gaia_iii", ticket, MegaBotanyItems.PANDORA_BOX.get(), FrameType.CHALLENGE, KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MegaBotanyEntities.GAIA_GUARDIAN_III.get())), consumer);
            relicAdvancement(MegaBotanyItems.INFINITE_DRINK.get(), kill_gaia_iii, consumer);
            relicAdvancement(MegaBotanyItems.EXCALIBUR.get(), kill_gaia_iii, consumer);
            relicAdvancement(MegaBotanyItems.ACHILLES_SHIELD.get(), kill_gaia_iii, consumer);
            relicAdvancement(MegaBotanyItems.ALL_FOR_ONE.get(), kill_gaia_iii, consumer);
            relicAdvancement(MegaBotanyItems.FAILNAUGHT.get(), kill_gaia_iii, consumer);
            relicAdvancement(MegaBotanyItems.ABSOLUTION_PENDANT.get(), kill_gaia_iii, consumer);

            Advancement orichalcos_ingot = obtainItemAdvancement(MegaBotanyItems.ORICHALCOS_INGOT.get(), kill_gaia_iii, consumer);
            codeTriggeredAdvancement(MegaBotanyItems.MASTER_BAND_OF_MANA.get(), orichalcos_ingot, FrameType.CHALLENGE, consumer, "mana_master");
            armorSetAdvancement(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get(), orichalcos_ingot, consumer, "female_orichalcos_set");
        }

        private static Advancement advancement(String name, Advancement parent, Item icon, FrameType frame, CriterionTriggerInstance criterion, Consumer<Advancement> consumer) {
            return Advancement.Builder.advancement().parent(parent).display(icon, Component.translatable("advancement.megabotany:" + name), Component.translatable("advancement.megabotany:" + name + ".desc"), null, frame, true, true, false).addCriterion("requirement", criterion).save(consumer, "megabotany:" + name);
        }

        private static Advancement obtainItemAdvancement(Item item, Advancement parent, Consumer<Advancement> consumer) {
            return advancement("obtain_" + BuiltInRegistries.ITEM.getKey(item).getPath(), parent, item, FrameType.TASK, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).build()), consumer);
        }

        private static Advancement codeTriggeredAdvancement(Item icon, Advancement parent, FrameType frame, Consumer<Advancement> consumer, String name) {
            return Advancement.Builder.advancement().parent(parent).display(icon, Component.translatable("advancement.megabotany:" + name), Component.translatable("advancement.megabotany:" + name + ".desc"), null, frame, true, true, false).addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance()).save(consumer, "megabotany:" + name);
        }

        private static void armorSetAdvancement(Item icon, Advancement parent, Consumer<Advancement> consumer, String name) {
            codeTriggeredAdvancement(icon, parent, FrameType.CHALLENGE, consumer, name);
        }

        private static void relicAdvancement(Item relic, Advancement parent, Consumer<Advancement> consumer) {
            Advancement.Builder.advancement().parent(parent).display(relic, Component.translatable("advancement.megabotany:" + BuiltInRegistries.ITEM.getKey(relic).getPath()), Component.translatable("advancement.megabotany:" + BuiltInRegistries.ITEM.getKey(relic).getPath() + ".desc"), null, FrameType.CHALLENGE, true, true, false).addCriterion("relic", new RelicBindTrigger.Instance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(relic).build())).save(consumer, "megabotany:" + BuiltInRegistries.ITEM.getKey(relic).getPath());
        }
    }


}