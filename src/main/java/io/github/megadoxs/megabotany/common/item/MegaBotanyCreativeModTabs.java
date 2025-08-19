package io.github.megadoxs.megabotany.common.item;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyBlocks;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.item.CustomCreativeTabContents;

public class MegaBotanyCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MegaBotany.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MEGABOTANY = CREATIVE_MODE_TABS.register("megabotany_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(MegaBotanyItems.NATURE_ORB.get()))
            .title(Component.translatable("creativetab.megabotany_tab"))
            .displayItems((itemDisplayParameters, output) -> {

                output.accept(MegaBotanyFlowerBlocks.bloodyEnchantress);
                output.accept(MegaBotanyFlowerBlocks.bloodyEnchantressFloating);
                output.accept(MegaBotanyFlowerBlocks.sunshineLily);
                output.accept(MegaBotanyFlowerBlocks.sunshineLilyFloating);
                output.accept(MegaBotanyFlowerBlocks.moonlightLily);
                output.accept(MegaBotanyFlowerBlocks.moonlightLilyFloating);
                output.accept(MegaBotanyFlowerBlocks.omniviolet);
                output.accept(MegaBotanyFlowerBlocks.omnivioletFloating);
                output.accept(MegaBotanyFlowerBlocks.edelweiss);
                output.accept(MegaBotanyFlowerBlocks.edelweissFloating);
                output.accept(MegaBotanyFlowerBlocks.tinkle);
                output.accept(MegaBotanyFlowerBlocks.tinkleFloating);
                output.accept(MegaBotanyFlowerBlocks.bellFlower);
                output.accept(MegaBotanyFlowerBlocks.bellFlowerFloating);
                output.accept(MegaBotanyFlowerBlocks.reikarLily);
                output.accept(MegaBotanyFlowerBlocks.reikarLilyFloating);
                output.accept(MegaBotanyFlowerBlocks.geminiOrchid);
                output.accept(MegaBotanyFlowerBlocks.geminiOrchidFloating);
                output.accept(MegaBotanyFlowerBlocks.enchantedOrchid);
                output.accept(MegaBotanyFlowerBlocks.enchantedOrchidFloating);
                output.accept(MegaBotanyFlowerBlocks.mirrortunia);
                output.accept(MegaBotanyFlowerBlocks.mirrortuniaFloating);
                output.accept(MegaBotanyFlowerBlocks.annoyingFlower);
                output.accept(MegaBotanyFlowerBlocks.annoyingFlowerFloating);
                output.accept(MegaBotanyFlowerBlocks.necrofleur);
                output.accept(MegaBotanyFlowerBlocks.necrofleurFloating);
                output.accept(MegaBotanyFlowerBlocks.necrofleurChibi);
                output.accept(MegaBotanyFlowerBlocks.necrofleurChibiFloating);
                output.accept(MegaBotanyFlowerBlocks.stardustLotus);
                output.accept(MegaBotanyFlowerBlocks.stardustLotusFloating);
                output.accept(MegaBotanyFlowerBlocks.manalinkium);
                output.accept(MegaBotanyFlowerBlocks.manalinkiumFloating);

                output.accept(MegaBotanyItems.MANASTEEL_SHIELD.get());
                output.accept(MegaBotanyItems.ELEMENTIUM_SHIELD.get());
                output.accept(MegaBotanyItems.TERRASTEEL_SHIELD.get());
                output.accept(MegaBotanyItems.WALKING_CANE.get());

                output.accept(MegaBotanyItems.PANDORA_BOX.get());
                output.accept(MegaBotanyItems.FAILNAUGHT.get());
                output.accept(MegaBotanyItems.EXCALIBUR.get());
                output.accept(MegaBotanyItems.ACHILLES_SHIELD.get());
                output.accept(MegaBotanyItems.ALL_FOR_ONE.get());
                output.accept(MegaBotanyItems.INFINITE_DRINK.get());
                output.accept(MegaBotanyItems.ABSOLUTION_PENDANT.get());

                output.accept(MegaBotanyItems.PHOTONIUM_INGOT.get());
                output.accept(MegaBotanyBlocks.PHOTONIUM_BLOCK.get());
                output.accept(MegaBotanyItems.SHADOWIUM_INGOT.get());
                output.accept(MegaBotanyBlocks.SHADOWIUM_BLOCK.get());
                output.accept(MegaBotanyItems.ORICHALCOS_INGOT.get());
                output.accept(MegaBotanyBlocks.ORICHALCOS_BLOCK.get());
                output.accept(MegaBotanyItems.TICKET.get());
                output.accept(MegaBotanyItems.EARTH_ESSENCE.get());
                output.accept(MegaBotanyItems.MEDAL_OF_HEROISM.get());

                output.accept(MegaBotanyItems.NIGHTMARE_FUEL.get());
                output.accept(MegaBotanyItems.SPIRIT_FUEL.get());
                output.accept(MegaBotanyBlocks.PEDESTAL.get());
                output.accept(MegaBotanyBlocks.SPIRIT_PORTAL.get());
                output.accept(MegaBotanyBlocks.REDSTONE_ELVEN_SPREADER.get());
                output.accept(MegaBotanyBlocks.REDSTONE_GAIA_SPREADER.get());

                output.accept(MegaBotanyItems.WALL_JUMP_AMULET.get());
                output.accept(MegaBotanyItems.WALL_CLIMB_AMULET.get());
                output.accept(MegaBotanyItems.FROST_RING.get());
                output.accept(MegaBotanyItems.MANA_DRIVE_RING.get());
                output.accept(MegaBotanyItems.JINGWEI_FEATHER.get());
                output.accept(MegaBotanyItems.PURE_DAISY_PENDANT.get());
                output.accept(MegaBotanyItems.SUPER_CROWN.get());
                output.accept(MegaBotanyItems.BOTTLED_FLAME.get());

                output.accept(MegaBotanyItems.SMELTING_LENS.get());
                output.accept(MegaBotanyItems.MANA_LENS.get());

                output.accept(MegaBotanyItems.MANAWEAVEDSTEEL_HELMET.get());
                output.accept(MegaBotanyItems.MANAWEAVEDSTEEL_CHESTPLATE.get());
                output.accept(MegaBotanyItems.MANAWEAVEDSTEEL_LEGGINGS.get());
                output.accept(MegaBotanyItems.MANAWEAVEDSTEEL_BOOTS.get());

                output.accept(MegaBotanyItems.SHADOWIUM_HELMET.get());
                output.accept(MegaBotanyItems.SHADOWIUM_CHESTPLATE.get());
                output.accept(MegaBotanyItems.SHADOWIUM_LEGGINGS.get());
                output.accept(MegaBotanyItems.SHADOWIUM_BOOTS.get());
                output.accept(MegaBotanyItems.SHADOWIUM_KATANA.get());

                output.accept(MegaBotanyItems.PHOTONIUM_HELMET.get());
                output.accept(MegaBotanyItems.PHOTONIUM_CHESTPLATE.get());
                output.accept(MegaBotanyItems.PHOTONIUM_LEGGINGS.get());
                output.accept(MegaBotanyItems.PHOTONIUM_BOOTS.get());
                output.accept(MegaBotanyItems.SHORT_PHOTONIUM_SWORD.get());

                output.accept(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get());
                output.accept(MegaBotanyItems.ORICHALCOS_CHESTPLATE_FEMALE.get());
                output.accept(MegaBotanyItems.ORICHALCOS_LEGGINGS_FEMALE.get());
                output.accept(MegaBotanyItems.ORICHALCOS_BOOTS_FEMALE.get());

                // don't like this implementation will make my own eventually
                ((CustomCreativeTabContents) MegaBotanyItems.MASTER_BAND_OF_MANA.get()).addToCreativeTab(MegaBotanyItems.MASTER_BAND_OF_MANA.get(), output);
                ((CustomCreativeTabContents) MegaBotanyItems.NATURE_ORB.get()).addToCreativeTab(MegaBotanyItems.NATURE_ORB.get(), output);
                ((CustomCreativeTabContents) MegaBotanyItems.GOD_CORE.get()).addToCreativeTab(MegaBotanyItems.GOD_CORE.get(), output);
                ((CustomCreativeTabContents) MegaBotanyItems.INFINITE_BREW.get()).addToCreativeTab(MegaBotanyItems.INFINITE_BREW.get(), output);
                ((CustomCreativeTabContents) MegaBotanyItems.INFINITE_SPLASH_BREW.get()).addToCreativeTab(MegaBotanyItems.INFINITE_SPLASH_BREW.get(), output);
                ((CustomCreativeTabContents) MegaBotanyItems.INFINITE_LINGERING_BREW.get()).addToCreativeTab(MegaBotanyItems.INFINITE_LINGERING_BREW.get(), output);
            })
            .withSearchBar()
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
