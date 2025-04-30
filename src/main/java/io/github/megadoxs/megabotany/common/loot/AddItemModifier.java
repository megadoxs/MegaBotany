package io.github.megadoxs.megabotany.common.loot;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootModifierManager;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Codec<LootItemFunction[]> LOOT_FUNCTIONS_CODEC = Codec.PASSTHROUGH.flatXmap(
            d ->
            {
                try
                {
                    LootItemFunction[] functions = LootModifierManager.GSON_INSTANCE.fromJson(IGlobalLootModifier.getJson(d), LootItemFunction[].class);
                    return DataResult.success(functions);
                }
                catch (JsonSyntaxException e)
                {
                    LootModifierManager.LOGGER.warn("Unable to decode loot functions", e);
                    return DataResult.error(e::getMessage);
                }
            },
            functions ->
            {
                try
                {
                    JsonElement element = LootModifierManager.GSON_INSTANCE.toJsonTree(functions);
                    return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, element));
                }
                catch (JsonSyntaxException e)
                {
                    LootModifierManager.LOGGER.warn("Unable to encode loot functions", e);
                    return DataResult.error(e::getMessage);
                }
            }
    );

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(LOOT_FUNCTIONS_CODEC.fieldOf("functions").forGetter(m -> m.functions))
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("id").forGetter(m -> m.item.getItem()))
            .and(Codec.INT.fieldOf("count").forGetter(m -> m.item.getCount()))
            .and(CompoundTag.CODEC.optionalFieldOf("tag").forGetter(m -> Optional.ofNullable(m.item.getTag())))
            .apply(inst, (conditions, functions, item, count, tag) -> new AddItemModifier(conditions, functions, new ItemStack(item, count, tag.orElse(null))))));

    private final ItemStack item;
    private final LootItemFunction[] functions;

    public AddItemModifier(LootItemCondition[] conditions, LootItemFunction[] functions, ItemStack item) {
        super(conditions);
        this.item = item;
        this.functions = functions;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for(LootItemCondition condition : conditions) {
            if(!condition.test(context)) {
                return generatedLoot;
            }
        }

        ItemStack stack = item.copy();

        for (LootItemFunction function : functions) {
            stack = function.apply(stack, context);
        }

        generatedLoot.add(stack);

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
