package io.github.megadoxs.megabotany.common.data;

import io.github.megadoxs.megabotany.common.data.loot.MegaBotanyBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class MegaBotanyLootTableGenerator {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(MegaBotanyBlockLootTables::new, LootContextParamSets.BLOCK)));
    }
}
