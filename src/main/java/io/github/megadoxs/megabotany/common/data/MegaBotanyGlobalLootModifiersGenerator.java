package io.github.megadoxs.megabotany.common.data;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import io.github.megadoxs.megabotany.common.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import vazkii.botania.common.loot.TrueGuardianKiller;

public class MegaBotanyGlobalLootModifiersGenerator extends GlobalLootModifierProvider {
    static final TrueGuardianKiller TRUE_GUARDIAN_KILLER_INSTANCE = new TrueGuardianKiller();

    public MegaBotanyGlobalLootModifiersGenerator(PackOutput output) {
        super(output, MegaBotany.MOD_ID);
    }

    @Override
    protected void start() {
        add("earth_essence_from_gaia_2", new AddItemModifier(
                new LootItemCondition[]{new LootTableIdCondition.Builder(new ResourceLocation("botania:gaia_guardian_2")).build(), new TrueGuardianKiller()},
                new LootItemFunction[]{SetItemCountFunction.setCount(ConstantValue.exactly(8)).when(trueKiller()).build()},
                new ItemStack(MegaBotanyItems.EARTH_ESSENCE.get(), 6)
        ));
    }

    //Botania I love you, please implement the methods that are in the default game.
    public static LootItemCondition.Builder trueKiller() {
        return () -> TRUE_GUARDIAN_KILLER_INSTANCE;
    }
}
