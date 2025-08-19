package io.github.megadoxs.megabotany.common.item.relic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.BaubleBoxItem;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.item.relic.RelicBaubleItem;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO this item has to change, it is flawed in so many different ways
public class AFORing extends BaubleItem {

    public final int SIZE = 24;

    public AFORing(Properties props) {
        super(props);
    }

    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null)
                relic.tickBinding(player);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }

    @Override
    public boolean canEquip(ItemStack stack, LivingEntity entity) {
        var relic = XplatAbstractions.INSTANCE.findRelic(stack);
        return entity instanceof Player player && relic != null && relic.isRightPlayer(player) && super.canEquip(stack, entity);
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, new ResourceLocation(MegaBotany.MOD_ID, MegaBotanyItems.ALL_FOR_ONE.getId().getPath()));
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            var relic1 = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic1 != null)
                relic1.tickBinding(player);

            Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
            Multimap<Attribute, AttributeModifier> toRemove = HashMultimap.create();
            Multimap<Attribute, AttributeModifier> oldAttributeModifiers = getAttributeModifiers(stack);
            verifyOldAttributeModifiers(oldAttributeModifiers, player);
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() instanceof BaubleBoxItem) {
                    SimpleContainer boxInventory = BaubleBoxItem.getInventory(player.getInventory().getItem(i));
                    List<Item> items = Lists.newArrayList();
                    for (int j = 0; j < SIZE; j++) {
                        ItemStack itemStack = boxInventory.getItem(j).copy();
                        if (itemStack.getItem() instanceof BaubleItem item && items.stream().noneMatch(it -> it.getClass().isInstance(item.asItem()) || item.asItem().getClass().isInstance(it)) && !(itemStack.getItem() instanceof AFORing) && CuriosApi.getCuriosInventory(player).orElse(null).findFirstCurio(curio -> item.getClass().isInstance(curio.getItem()) || curio.getItem().getClass().isInstance(item)).isEmpty() && (!(item instanceof RelicBaubleItem relic) || relic.canEquip(boxInventory.getItem(j), player))) {
                            items.add(item);
                            item.onWornTick(itemStack, player);

                            if (!itemStack.equals(boxInventory.getItem(j), false))
                                boxInventory.setItem(j, itemStack);

                            for (Map.Entry<Attribute, AttributeModifier> entry : item.getEquippedAttributeModifiers(itemStack).entries()) {
                                if (!oldAttributeModifiers.containsEntry(entry.getKey(), entry.getValue()))
                                    storeAttributeModifier(stack, entry);
                                attributeModifiers.put(entry.getKey(), entry.getValue());
                            }

                        }
                    }

                    for (Map.Entry<Attribute, AttributeModifier> entry : oldAttributeModifiers.entries()) {
                        if (!attributeModifiers.containsEntry(entry.getKey(), entry.getValue())) {
                            removeAttributeModifier(stack, entry);
                            toRemove.put(entry.getKey(), entry.getValue());
                        }
                    }

                    attributeModifiers.entries().removeIf(entry -> oldAttributeModifiers.containsEntry(entry.getKey(), entry.getValue()));

                    break;
                }
            }
            player.getAttributes().removeAttributeModifiers(toRemove);
            player.getAttributes().addTransientAttributeModifiers(attributeModifiers);
        }
    }

    @Override
    public void onUnequipped(ItemStack stack, LivingEntity entity) {
        if (entity instanceof ServerPlayer player && CuriosApi.getCuriosInventory(player).orElse(null).findFirstCurio(curio -> curio.getItem() instanceof AFORing).isEmpty()) {
            player.getAttributes().removeAttributeModifiers(getAttributeModifiers(stack));
        }
    }

    @SubscribeEvent
    public static void addAttributeTooltip(CurioAttributeModifierEvent event) {
        if (event.getSlotContext().entity() != null && event.getSlotContext().entity().level().isClientSide && event.getItemStack().getItem() instanceof AFORing ring && event.getSlotContext().entity() instanceof Player player && CuriosApi.getCuriosInventory(player).orElse(null).findFirstCurio(curio -> curio.getItem() instanceof AFORing && curio.equals(event.getItemStack())).isPresent()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() instanceof BaubleBoxItem) {
                    Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
                    SimpleContainer boxInventory = BaubleBoxItem.getInventory(player.getInventory().getItem(i));
                    List<Item> items = Lists.newArrayList();
                    event.clearModifiers();

                    for (int j = 0; j < ring.SIZE; j++) {
                        if (boxInventory.getItem(j).getItem() instanceof BaubleItem item && items.stream().noneMatch(it -> it.getClass().isInstance(item.asItem()) || item.asItem().getClass().isInstance(it)) && !(boxInventory.getItem(j).getItem() instanceof AFORing) && CuriosApi.getCuriosInventory(player).orElse(null).findFirstCurio(curio -> item.getClass().isInstance(curio.getItem()) /*|| curio.getItem().getClass().isInstance(item)*/).isEmpty() && (!(item instanceof RelicBaubleItem relic) || relic.canEquip(boxInventory.getItem(j), player))) {
                            items.add(item);

                            for (Map.Entry<Attribute, AttributeModifier> entry : item.getEquippedAttributeModifiers(boxInventory.getItem(j)).entries()) {
                                if (!attributeModifiers.containsEntry(entry.getKey(), entry.getValue())) {
                                    attributeModifiers.put(entry.getKey(), entry.getValue());
                                    event.addModifier(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }

                    break;
                }
            }
        }
    }

    public static void verifyOldAttributeModifiers(Multimap<Attribute, AttributeModifier> oldAttributeModifiers, Player player) {
        List<Map.Entry<Attribute, AttributeModifier>> toRemove = new ArrayList<>();
        for (Map.Entry<Attribute, AttributeModifier> entry : oldAttributeModifiers.entries()) {
            if (!player.getAttributes().hasModifier(entry.getKey(), entry.getValue().getId())) {
                toRemove.add(entry);
            }
        }
        toRemove.forEach(e -> oldAttributeModifiers.remove(e.getKey(), e.getValue()));
    }

    public static void storeAttributeModifier(ItemStack stack, Map.Entry<Attribute, AttributeModifier> entry) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("oldAttributeModifiers", 9)) {
            tag.put("oldAttributeModifiers", new ListTag());
        }
        ListTag listtag = tag.getList("oldAttributeModifiers", 10);
        if (ForgeRegistries.ATTRIBUTES.getKey(entry.getKey()) == null)
            return;

        CompoundTag compoundtag = new CompoundTag();

        compoundtag.putString("Name", entry.getValue().getName());
        compoundtag.putDouble("Amount", entry.getValue().getAmount());
        compoundtag.putInt("Operation", entry.getValue().getOperation().toValue());
        compoundtag.putUUID("UUID", entry.getValue().getId());
        compoundtag.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(entry.getKey()).toString());

        listtag.add(compoundtag);
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();

        if (stack.getTag() != null && stack.getTag().contains("oldAttributeModifiers", 9)) {
            ListTag listnbt = stack.getTag().getList("oldAttributeModifiers", 10);

            for (int i = 0; i < listnbt.size(); i++) {
                CompoundTag compoundtag = listnbt.getCompound(i);
                multimap.put(ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(compoundtag.getString("AttributeName"))), new AttributeModifier(compoundtag.getUUID("UUID"), compoundtag.getString("Name"), compoundtag.getInt("Amount"), AttributeModifier.Operation.fromValue(compoundtag.getInt("Operation"))));
            }
        }

        return multimap;
    }

    public static void removeAttributeModifier(ItemStack stack, Map.Entry<Attribute, AttributeModifier> entry) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("oldAttributeModifiers", 9)) {
            return;
        }
        ListTag listnbt = tag.getList("oldAttributeModifiers", 10);
        CompoundTag compoundtag = new CompoundTag();

        compoundtag.putString("Name", entry.getValue().getName());
        compoundtag.putDouble("Amount", entry.getValue().getAmount());
        compoundtag.putInt("Operation", entry.getValue().getOperation().toValue());
        compoundtag.putUUID("UUID", entry.getValue().getId());
        compoundtag.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(entry.getKey()).toString());

        listnbt.remove(compoundtag);
    }
}
