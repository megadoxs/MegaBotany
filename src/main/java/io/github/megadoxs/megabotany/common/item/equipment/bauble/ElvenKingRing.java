package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BaubleBoxItem;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.item.relic.RelicBaubleItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElvenKingRing extends BaubleItem {

    private final Map<ItemStack, Map<Item, Multimap<Attribute, AttributeModifier>>> attributeModifiers = new HashMap<>();

    public ElvenKingRing(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        //hasOnlyOneEquippedItem will probably cause issue
        if (entity instanceof ServerPlayer player)
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() instanceof BaubleBoxItem) {
                    SimpleContainer boxInventory = BaubleBoxItem.getInventory(player.getInventory().getItem(i));
                    List<Item> items = Lists.newArrayList();
                    for (int j = 0; j < getSize(); j++) {
                        if (boxInventory.getItem(j).getItem() instanceof BaubleItem item && !items.contains(item) && !(boxInventory.getItem(j).getItem() instanceof ElvenKingRing) && (!(item instanceof RelicBaubleItem relic) || relic.canEquip(boxInventory.getItem(j), player))) {
                            items.add(item);
                            item.onWornTick(boxInventory.getItem(j), entity);

                            if(!attributeModifiers.containsKey(stack) || !attributeModifiers.get(stack).containsKey(item)) {
                                player.getAttributes().addTransientAttributeModifiers(item.getEquippedAttributeModifiers(boxInventory.getItem(j)));
                                Map<Item, Multimap<Attribute, AttributeModifier>> modifiersMap = new HashMap<>();
                                modifiersMap.put(item, item.getEquippedAttributeModifiers(boxInventory.getItem(j)));
                                attributeModifiers.put(stack, modifiersMap);
                            }
                        }
                    }

                    if(!attributeModifiers.containsKey(stack))
                        break;

                    boolean changed = attributeModifiers.get(stack).keySet().removeIf(item -> {
                        if (!items.contains(item)) {
                            player.getAttributes().removeAttributeModifiers(attributeModifiers.get(stack).get(item));
                            return true;
                        }
                        return false;
                    });

                    if (changed)
                        onEquipped(stack, entity);

                    break;
                }
            }
        //idea
        //stack.getAttributeModifiers(stack.getEquipmentSlot()).put()
    }

    public boolean hasOnlyOneEquippedItem(Container container) {
        int $$1 = 0;

        for(int $$2 = 0; $$2 < container.getContainerSize(); ++$$2) {
            ItemStack $$3 = container.getItem($$2);
            if ($$3.getItem() instanceof ElvenKingRing) {
                $$1 += $$3.getCount();
            }
        }

        return $$1 == 1;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();

        if(attributeModifiers.containsKey(stack))
            for (Multimap<Attribute, AttributeModifier> modifiers : attributeModifiers.get(stack).values()) {
                attributes.putAll(modifiers);
            }

        return attributes;
    }

    @Override
    public void onUnequipped(ItemStack stack, LivingEntity entity) {
        super.onUnequipped(stack, entity);
        attributeModifiers.clear();
    }

    public int getSize() {
        return 3;
    }
}
