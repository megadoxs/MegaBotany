package io.github.megadoxs.megabotany.common;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import io.github.megadoxs.megabotany.client.core.handler.ColorHandler;
import io.github.megadoxs.megabotany.client.model.MegaBotanyLayerDefinition;
import io.github.megadoxs.megabotany.client.renderer.GaiaGuardianBossBarHandler;
import io.github.megadoxs.megabotany.client.renderer.GaiaGuardianIIIRenderer;
import io.github.megadoxs.megabotany.client.renderer.LandMineRenderer;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlockEntities;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.block.MegaBotanyPOITypes;
import io.github.megadoxs.megabotany.common.crafting.MegaBotanyRecipes;
import io.github.megadoxs.megabotany.common.effect.MegaBotanyEffects;
import io.github.megadoxs.megabotany.common.entity.MegaBotanyEntities;
import io.github.megadoxs.megabotany.common.item.MegaBotanyCreativeModTabs;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import io.github.megadoxs.megabotany.common.item.equipment.armor.orichalcos.OrichalcosHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.photonium.PhotoniumHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.shadowium.ShadowiumHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.bauble.CoreGod;
import io.github.megadoxs.megabotany.common.item.equipment.bauble.MasterBandOfMana;
import io.github.megadoxs.megabotany.common.item.relic.*;
import io.github.megadoxs.megabotany.common.network.MegaBotanyNetwork;
import io.github.megadoxs.megabotany.common.util.MegaBotanyItemProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.PlayerAccess;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.forge.CapabilityUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MegaBotany.MOD_ID)
public class MegaBotany {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "megabotany";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MegaBotany() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        bind(Registries.BLOCK, MegaBotanyFlowerBlocks::registerBlocks);
        bindForItems(MegaBotanyFlowerBlocks::registerItemBlocks);
        bind(Registries.BLOCK_ENTITY_TYPE, MegaBotanyFlowerBlocks::registerBlockEntity);

        MegaBotanyCreativeModTabs.register(modEventBus);

        MegaBotanyItems.register(modEventBus);
        MegaBotanyBlocks.register(modEventBus);
        MegaBotanyBlockEntities.register(modEventBus);
        MegaBotanyPOITypes.register(modEventBus);

        MegaBotanyEffects.register(modEventBus);
        MegaBotanyEntities.register(modEventBus);

        MegaBotanyRecipes.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> CoreGod.playerLoggedOut((ServerPlayer) e.getEntity()));
        bus.addListener((LivingEvent.LivingTickEvent e) -> {
            if (e.getEntity() instanceof Player player)
                CoreGod.updatePlayerFlyStatus(player);
        });

        event.enqueueWork(() -> {
            BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer = (resourceLocation, blockSupplier) -> ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(resourceLocation, blockSupplier);
            MegaBotanyFlowerBlocks.registerFlowerPotPlants(consumer);
        });

        bus.addGenericListener(BlockEntity.class, this::attachBeCaps);
        bus.addGenericListener(ItemStack.class, this::attachItemCaps);
        // will be removed in 1.21.+ when my PR about AncientWillContainer is merged
        bus.addListener(EventPriority.LOW, (CriticalHitEvent e) -> {
            Event.Result result = e.getResult();
            if (e.getEntity().level().isClientSide
                    || result == Event.Result.DENY
                    || result == Event.Result.DEFAULT && !e.isVanillaCritical()
                    || (!OrichalcosHelmetItem.hasOrichalcosArmorSet(e.getEntity()) && !ShadowiumHelmetItem.hasShadowiumArmorSet(e.getEntity()) && !PhotoniumHelmetItem.hasPhotoniumArmorSet(e.getEntity()))
                    || !(e.getTarget() instanceof LivingEntity target)) {
                return;
            }

            Item item = e.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem();

            if (item instanceof OrichalcosHelmetItem)
                e.setDamageModifier(e.getDamageModifier() * OrichalcosHelmetItem.getCritDamageMult(e.getEntity()));
            else if (item instanceof ShadowiumHelmetItem)
                e.setDamageModifier(e.getDamageModifier() * ShadowiumHelmetItem.getCritDamageMult(e.getEntity()));
            else if (item instanceof PhotoniumHelmetItem)
                e.setDamageModifier(e.getDamageModifier() * PhotoniumHelmetItem.getCritDamageMult(e.getEntity()));
            ((PlayerAccess) e.getEntity()).botania$setCritTarget(target);
        });
        MegaBotanyNetwork.register();
    }

    private void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
        var be = e.getObject();

        if (be.getType() == MegaBotanyFlowerBlocks.MANALINKIUM) {
            e.addCapability(prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
        }
    }

    private void attachItemCaps(AttachCapabilitiesEvent<ItemStack> e) {
        var stack = e.getObject();

        var makeRelic = RELIC.get().get(stack.getItem());
        if (makeRelic != null) {
            e.addCapability(prefix("relic"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.RELIC, makeRelic.apply(stack)));
        }

        var makeManaItem = MANA_ITEM.get().get(stack.getItem());
        if (makeManaItem != null) {
            e.addCapability(prefix("mana_item"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_ITEM, makeManaItem.apply(stack)));
        }
    }

    private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
            MegaBotanyItems.FAILNAUGHT.get(), Failnaught::makeRelic,
            MegaBotanyItems.EXCALIBER.get(), Excaliber::makeRelic,
            MegaBotanyItems.ACHILLED_SHIELD.get(), AchilledShield::makeRelic,
            MegaBotanyItems.ALL_FOR_ONE.get(), AFORing::makeRelic,
            MegaBotanyItems.INFINITE_DRINK.get(), InfiniteDrink::makeRelic,
            MegaBotanyItems.INFINITE_BREW.get(), InfiniteBrew::makeRelic,
            MegaBotanyItems.INFINITE_SPLASH_BREW.get(), InfiniteBrew::makeRelic,
            MegaBotanyItems.INFINITE_LINGERING_BREW.get(), InfiniteBrew::makeRelic,
            MegaBotanyItems.ABSOLUTION_PENDANT.get(), DamageNullification::makeRelic,
            MegaBotanyItems.PANDORA_BOX.get(), PandoraBox::makeRelic
    ));

    private static final Supplier<Map<Item, Function<ItemStack, ManaItem>>> MANA_ITEM = Suppliers.memoize(() -> Map.of(
            MegaBotanyItems.MASTER_BAND_OF_MANA.get(), MasterBandOfMana.MasterManaImpl::new
    ));

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }


    private void bindForItems(Consumer<BiConsumer<Item, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.ITEM)) {
                source.accept((t, rl) -> event.register(Registries.ITEM, rl, () -> t));
            }
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MegaBotanyItemProperties.addItemProperties((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));

            EntityRenderers.register(MegaBotanyEntities.AURA_FIRE.get(), NoopRenderer::new);
            EntityRenderers.register(MegaBotanyEntities.LAND_MINE.get(), LandMineRenderer::new);
            EntityRenderers.register(MegaBotanyEntities.GAIA_GUARDIAN_III.get(), GaiaGuardianIIIRenderer::new);
            EntityRenderers.register(MegaBotanyEntities.THROWN_BREW.get(), ThrownItemRenderer::new);

            var bus = MinecraftForge.EVENT_BUS;
            bus.addGenericListener(BlockEntity.class, MegaBotany::attachBeCapabilities);
            bus.addListener((CustomizeGuiOverlayEvent.BossEventProgress e) -> {
                var result = GaiaGuardianBossBarHandler.onBarRender(e.getGuiGraphics(), e.getX(), e.getY(),
                        e.getBossEvent(), true);
                result.ifPresent(increment -> {
                    e.setCanceled(true);
                    e.setIncrement(increment);
                });
            });
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            MegaBotanyFlowerBlocks.registerBlockEntityRenderer(event);
        }

        @SubscribeEvent
        public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
            MegaBotanyLayerDefinition.init(evt::registerLayerDefinition);
        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            ColorHandler.submitItems(event::register);
        }
    }

    private static void attachBeCapabilities(AttachCapabilitiesEvent<BlockEntity> e) {
        var be = e.getObject();

        var makeWandHud = WAND_HUD.get().get(be.getType());
        if (makeWandHud != null) {
            e.addCapability(prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, makeWandHud.apply(be)));
        }
    }

    private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, WandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
        var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, WandHUD>>();
        MegaBotanyFlowerBlocks.registerWandHudCaps((factory, types) -> {
            for (var type : types) {
                ret.put(type, factory);
            }
        });
        return Collections.unmodifiableMap(ret);
    });

    @Mod.EventBusSubscriber(modid = MOD_ID)
    private static class BotaniaEventSubscriber {
        @SubscribeEvent
        public static void botaniaWandTooltip(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof WandOfTheForestItem) {
                List<Component> tooltip = event.getToolTip();
                int y = ItemNBTHelper.getInt(stack, "boundTileY", Integer.MIN_VALUE);
                if (y != Integer.MIN_VALUE) {
                    tooltip.add(Component.literal("X: " + ItemNBTHelper.getInt(stack, "boundTileX", 0)).withStyle(ChatFormatting.GRAY));
                    tooltip.add(Component.literal("Y: " + y).withStyle(ChatFormatting.GRAY));
                    tooltip.add(Component.literal("Z: " + ItemNBTHelper.getInt(stack, "boundTileZ", 0)).withStyle(ChatFormatting.GRAY));
                    String dimensionId = ItemNBTHelper.getString(stack, "dimensionId", "");
                    String dimension;
                    if (!dimensionId.isEmpty())
                        dimension = dimensionId;
                    else
                        dimension = event.getEntity().level().dimension().location().toString();
                    tooltip.add(Component.literal("Dimension: " + Arrays.stream(dimension.substring(dimension.indexOf(':') + 1).replace('_', ' ').split(" "))
                            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                            .collect(Collectors.joining(" "))).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
}
