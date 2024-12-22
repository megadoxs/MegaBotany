package io.github.megadoxs.extrabotany_reborn.common;

import com.mojang.logging.LogUtils;
import io.github.megadoxs.extrabotany_reborn.client.model.ModLayerDefinition;
import io.github.megadoxs.extrabotany_reborn.client.renderer.ExplosiveMissileRenderer;
import io.github.megadoxs.extrabotany_reborn.client.renderer.GaiaGuardianIIIRenderer;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
import org.slf4j.Logger;
import vazkii.botania.common.PlayerAccess;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExtraBotany_Reborn.MOD_ID)
public class ExtraBotany_Reborn {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "extrabotany_reborn";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExtraBotany_Reborn() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

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

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> CoreGod.playerLoggedOut((ServerPlayer) e.getEntity()));
        bus.addListener((LivingEvent.LivingTickEvent e) -> {
            if (e.getEntity() instanceof Player player)
                CoreGod.updatePlayerFlyStatus(player);
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

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

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
        }

        @SubscribeEvent
        public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
            ModLayerDefinition.init(evt::registerLayerDefinition);
        }
    }
}
