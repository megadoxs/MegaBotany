package io.github.megadoxs.extrabotany_reborn.common.item.equipment.tool.hammer;

import vazkii.botania.api.BotaniaAPI;

public class TerrasteelHammer extends ManasteelHammer {

    private static final int manaPerDamage = 100;

    public TerrasteelHammer(Properties pProperties) {
        super(BotaniaAPI.instance().getTerrasteelItemTier(), 6, 0.8, pProperties);
    }

    @Override
    public int getManaPerDamage() {
        return manaPerDamage;
    }

    // Will give it an ability as well
}
