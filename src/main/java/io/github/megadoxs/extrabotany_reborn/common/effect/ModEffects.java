package io.github.megadoxs.extrabotany_reborn.common.effect;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ExtraBotany_Reborn.MOD_ID);

    public static final RegistryObject<MobEffect> BLOOD_DEFICIENCY = MOB_EFFECTS.register("blood_deficiency", () -> new BloodDeficiencyEffect(MobEffectCategory.HARMFUL, 0XDC143C));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}