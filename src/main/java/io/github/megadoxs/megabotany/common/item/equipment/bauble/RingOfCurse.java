package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class RingOfCurse extends BaubleItem {

    private static final int RANGE = 6; // will probably be reduced

    public RingOfCurse(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        Level level = entity.level();
        AABB area = new AABB(entity.blockPosition().offset(-RANGE, -RANGE, -RANGE), entity.blockPosition().offset(RANGE + 1, RANGE + 1, RANGE + 1));

        for (Entity living : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (living instanceof Player player && living != entity && level.getGameTime() % 30 == 0 && ManaItemHandler.instance().requestManaExact(stack, player, 50, true)) {
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
                player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 60, 1));
            }
        }
    }
}
