package io.github.megadoxs.extrabotany_reborn.common;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import io.github.megadoxs.extrabotany_reborn.client.model.ModLayerDefinition;
import io.github.megadoxs.extrabotany_reborn.client.renderer.ExplosiveMissileRenderer;
import io.github.megadoxs.extrabotany_reborn.client.renderer.GaiaGuardianIIIRenderer;
import io.github.megadoxs.extrabotany_reborn.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlockEntities;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import io.github.megadoxs.extrabotany_reborn.common.craft.ModRecipes;
import io.github.megadoxs.extrabotany_reborn.common.effect.ModEffects;
import io.github.megadoxs.extrabotany_reborn.common.entity.ModEntities;
import io.github.megadoxs.extrabotany_reborn.common.item.ModCreativeModTabs;
import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.armor.OrichalcosHelmetItem;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble.CoreGod;
import io.github.megadoxs.extrabotany_reborn.common.network.ModNetwork;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
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
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.common.PlayerAccess;
import vazkii.botania.forge.CapabilityUtil;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExtraBotany_Reborn.MOD_ID)
public class ExtraBotany_Reborn {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "extrabotany_reborn";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExtraBotany_Reborn() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        bind(Registries.BLOCK, MegaBotanyFlowerBlocks::registerBlocks);
        bindForItems(MegaBotanyFlowerBlocks::registerItemBlocks);
        bind(Registries.BLOCK_ENTITY_TYPE, MegaBotanyFlowerBlocks::registerTEs);

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModEffects.register(modEventBus);
        ModEntities.register(modEventBus);

        ModRecipes.register(modEventBus);

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


        // will be removed in 1.21.+ when my PR about AncientWillContainer is merged
        bus.addListener(EventPriority.LOW, (CriticalHitEvent e) -> {
            Event.Result result = e.getResult();
            if (e.getEntity().level().isClientSide
                    || result == Event.Result.DENY
                    || result == Event.Result.DEFAULT && !e.isVanillaCritical()
                    || !OrichalcosHelmetItem.hasOrichalcosArmorSet(e.getEntity())
                    || !(e.getTarget() instanceof LivingEntity target)) {
                return;
            }
            e.setDamageModifier(e.getDamageModifier() * OrichalcosHelmetItem.getCritDamageMult(e.getEntity()));
            ((PlayerAccess) e.getEntity()).botania$setCritTarget(target);
        });
        ModNetwork.register();
    }

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

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.AURA_FIRE.get(), NoopRenderer::new);
            EntityRenderers.register(ModEntities.EXPLOSIVE_MISSILE.get(), ExplosiveMissileRenderer::new);
            EntityRenderers.register(ModEntities.GAIA_GUARDIAN_III.get(), GaiaGuardianIIIRenderer::new);

            var bus = MinecraftForge.EVENT_BUS;
            bus.addGenericListener(BlockEntity.class, ExtraBotany_Reborn::attachBeCapabilities);
        }

        @SubscribeEvent
        public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
            ModLayerDefinition.init(evt::registerLayerDefinition);
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
//        BotaniaBlockEntities.registerWandHudCaps((factory, types) -> {
//            for (var type : types) {
//                ret.put(type, factory);
//            }
//        });
        return Collections.unmodifiableMap(ret);
    });
}
