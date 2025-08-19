package io.github.megadoxs.megabotany.common.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class SpiritPortalTrigger extends SimpleCriterionTrigger<SpiritPortalTrigger.Instance> {
    public static final ResourceLocation ID = prefix("open_spirit_portal");
    public static final SpiritPortalTrigger INSTANCE = new SpiritPortalTrigger();

    private SpiritPortalTrigger() {
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @NotNull
    @Override
    public SpiritPortalTrigger.Instance createInstance(@NotNull JsonObject json, ContextAwarePredicate playerPred, DeserializationContext conditions) {
        return new SpiritPortalTrigger.Instance(playerPred, ItemPredicate.fromJson(json.get("wand")), LocationPredicate.fromJson(json.get("location")));
    }

    public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos, ItemStack wand) {
        trigger(player, instance -> instance.test(world, pos, wand));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate wand;
        private final LocationPredicate pos;

        public Instance(ContextAwarePredicate playerPred, ItemPredicate predicate, LocationPredicate pos) {
            super(ID, playerPred);
            this.wand = predicate;
            this.pos = pos;
        }

        @NotNull
        @Override
        public ResourceLocation getCriterion() {
            return ID;
        }

        boolean test(ServerLevel world, BlockPos pos, ItemStack wand) {
            return this.wand.matches(wand) && this.pos.matches(world, pos.getX(), pos.getY(), pos.getZ());
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = super.serializeToJson(context);
            if (wand != ItemPredicate.ANY) {
                json.add("wand", wand.serializeToJson());
            }
            if (pos != LocationPredicate.ANY) {
                json.add("location", pos.serializeToJson());
            }
            return json;
        }

        public ItemPredicate getWand() {
            return this.wand;
        }

        public LocationPredicate getPos() {
            return this.pos;
        }
    }
}
