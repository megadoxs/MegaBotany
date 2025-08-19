package io.github.megadoxs.megabotany.common.advancements;

import vazkii.botania.mixin.CriteriaTriggersAccessor;

public class MegaBotanyCriteriaTriggers {
    public static void init() {
        CriteriaTriggersAccessor.botania_register(SpiritPortalTrigger.INSTANCE);
    }
}
