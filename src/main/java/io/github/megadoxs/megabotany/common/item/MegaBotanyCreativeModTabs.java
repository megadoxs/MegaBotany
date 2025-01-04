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
                output.accept(MegaBotanyFlowerBlocks.stonesia);
                output.accept(MegaBotanyFlowerBlocks.stonesiaFloating);
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
                output.accept(MegaBotanyItems.MANASTEEL_HAMMER.get());
                output.accept(MegaBotanyItems.ELEMENTIUM_HAMMER.get());
                output.accept(MegaBotanyItems.TERRASTEEL_HAMMER.get());

                output.accept(MegaBotanyItems.PHOTONIUM_INGOT.get());
                output.accept(MegaBotanyBlocks.PHOTONIUM_BLOCK.get());
                output.accept(MegaBotanyItems.SHADOWIUM_INGOT.get());
                output.accept(MegaBotanyBlocks.SHADOWIUM_BLOCK.get());
                output.accept(MegaBotanyItems.ORICHALCOS_INGOT.get());
                output.accept(MegaBotanyBlocks.ORICHALCOS_BLOCK.get());
                output.accept(MegaBotanyItems.TICKET.get());

                output.accept(MegaBotanyItems.NIGHTMARE_FUEL.get());
                output.accept(MegaBotanyItems.SPIRIT_FUEL.get());
                output.accept(MegaBotanyItems.SPIRIT_FRAGMENT.get());
                output.accept(MegaBotanyBlocks.MORTAR.get());
                output.accept(MegaBotanyItems.GILDED_POTATO.get());
                output.accept(MegaBotanyItems.GILDED_MASHED_POTATO.get());
                output.accept(MegaBotanyItems.FRIED_CHICKEN.get());

                output.accept(MegaBotanyItems.MASTER_BAND_OF_MANA.get());
                output.accept(MegaBotanyItems.FROST_RING.get());
                output.accept(MegaBotanyItems.CURSE_RING.get());
                output.accept(MegaBotanyItems.WALL_JUMP_AMULET.get());
                output.accept(MegaBotanyItems.WALL_CLIMB_AMULET.get());
                output.accept(MegaBotanyItems.PARKOUR_AMULET.get());
                output.accept(MegaBotanyItems.PARKOUR_AMULET2.get());
                output.accept(MegaBotanyItems.MANA_DRIVE_RING.get());
                output.accept(MegaBotanyItems.ELVEN_KING_RING.get());
                output.accept(MegaBotanyItems.JINGWEI_FEATHER.get());
                output.accept(MegaBotanyItems.PURE_DAISY_PENDANT.get());
                output.accept(MegaBotanyItems.SUPER_CROWN.get());
                output.accept(MegaBotanyItems.BOTTLED_FLAME.get());

                output.accept(MegaBotanyItems.ORICHALCOS_HELMET_FEMALE.get());
                output.accept(MegaBotanyItems.ORICHALCOS_CHESTPLATE_FEMALE.get());
                output.accept(MegaBotanyItems.ORICHALCOS_LEGGINGS_FEMALE.get());
                output.accept(MegaBotanyItems.ORICHALCOS_BOOTS_FEMALE.get());


                ((CustomCreativeTabContents) MegaBotanyItems.NATURE_ORB.get()).addToCreativeTab(MegaBotanyItems.NATURE_ORB.get(), output); // don't like this implementation will make my own eventually
                ((CustomCreativeTabContents) MegaBotanyItems.GOD_CORE.get()).addToCreativeTab(MegaBotanyItems.GOD_CORE.get(), output);
            })
            .withSearchBar()
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
