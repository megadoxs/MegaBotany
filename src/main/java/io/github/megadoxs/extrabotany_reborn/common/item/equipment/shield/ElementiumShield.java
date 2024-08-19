package io.github.megadoxs.extrabotany_reborn.common.item.equipment.shield;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.handler.PixieHandler;

public class ElementiumShield extends ManasteelShield {

    // original elementium shield also ignites the target.

    public ElementiumShield(Properties pProperties) {
        super(pProperties);
    }

    @NotNull
    @SuppressWarnings("deprecation")
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> ret = super.getDefaultAttributeModifiers(slot);
        if (slot == EquipmentSlot.MAINHAND) {
            ret = HashMultimap.create(ret);
            ret.put(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Shield modifier", 0.08));
        }
        return ret;
    }
}
