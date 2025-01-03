package io.github.megadoxs.megabotany.client.core.handler;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "megabotany", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MiscellaneousModels {
    private static final ResourceLocation[] goddessWingIconIds = IntStream.range(1, 3).mapToObj((i) -> new ResourceLocation("megabotany", ("icon/god_core_10_" + i))).toArray(ResourceLocation[]::new);
    private static final ResourceLocation[] elfWingIconIds = IntStream.range(1, 8).mapToObj((i) -> new ResourceLocation("megabotany", ("icon/god_core_12_" + i))).toArray(ResourceLocation[]::new);
    private static final ResourceLocation flandreWingIconId = new ResourceLocation("megabotany:icon/god_core_11");
    private static final ResourceLocation steamPunkWingIconId = new ResourceLocation("megabotany:icon/god_core_13");
    private static final ResourceLocation jimWingIconId = new ResourceLocation("megabotany:icon/god_core_14");

    public final BakedModel[] goddessWingIcons;
    public final BakedModel[] elfWingIcons;
    public BakedModel flandreWingIcon;
    public BakedModel steamPunkWingIcon;
    public BakedModel jimWingIcon;

    private final Map<ResourceLocation, Consumer<BakedModel>> modelConsumers;

    public static final MiscellaneousModels INSTANCE = new MiscellaneousModels();

    private MiscellaneousModels() {
        this.modelConsumers = new HashMap<>();
        this.goddessWingIcons = getBakedModels(this.modelConsumers, goddessWingIconIds);
        this.elfWingIcons = getBakedModels(this.modelConsumers, elfWingIconIds);
        this.modelConsumers.put(flandreWingIconId, (bakedModel) -> this.flandreWingIcon = bakedModel);
        this.modelConsumers.put(steamPunkWingIconId, (bakedModel) -> this.steamPunkWingIcon = bakedModel);
        this.modelConsumers.put(jimWingIconId, (bakedModel) -> this.jimWingIcon = bakedModel);
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.BakingCompleted event) {
        INSTANCE.modelConsumers.forEach((resourceLocation, bakedModelConsumer) -> bakedModelConsumer.accept(event.getModels().get(resourceLocation)));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional evt) {
        Consumer<ResourceLocation> consumer = evt::register;
        INSTANCE.modelConsumers.keySet().forEach(consumer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    private static BakedModel[] getBakedModels(Map<ResourceLocation, Consumer<BakedModel>> consumers, ResourceLocation[] ids) {
        BakedModel[] bakedModels = new BakedModel[ids.length];

        for (int i = 0; i < ids.length; ++i) {
            final int index = i;
            consumers.put(ids[i], (bakedModel) -> bakedModels[index] = bakedModel);
        }

        return bakedModels;
    }
}
