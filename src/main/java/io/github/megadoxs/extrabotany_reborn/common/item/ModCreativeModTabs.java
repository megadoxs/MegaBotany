package io.github.megadoxs.extrabotany_reborn.common.item;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.item.CustomCreativeTabContents;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExtraBotany_Reborn.MOD_ID);

    public static final RegistryObject<CreativeModeTab> EXTRABOTANY_REBORN = CREATIVE_MODE_TABS.register("extrabotany_reborn_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.NATURE_ORB.get()))
            .title(Component.translatable("creativetab.extrabotany_reborn_tab"))
            .displayItems((itemDisplayParameters, output) -> {

                output.accept(ModBlocks.BLOODY_ENCHANTRESS.get());

                output.accept(ModItems.MANASTEEL_SHIELD.get());
                output.accept(ModItems.ELEMENTIUM_SHIELD.get());
                output.accept(ModItems.TERRASTEEL_SHIELD.get());
                output.accept(ModItems.MANASTEEL_HAMMER.get());
                output.accept(ModItems.ELEMENTIUM_HAMMER.get());
                output.accept(ModItems.TERRASTEEL_HAMMER.get());

                output.accept(ModItems.PHOTONIUM_INGOT.get());
                output.accept(ModBlocks.PHOTONIUM_BLOCK.get());
                output.accept(ModItems.SHADOWIUM_INGOT.get());
                output.accept(ModBlocks.SHADOWIUM_BLOCK.get());
                output.accept(ModItems.ORICHALCOS_INGOT.get());
                output.accept(ModBlocks.ORICHALCOS_BLOCK.get());
                output.accept(ModItems.TICKET.get());

                output.accept(ModItems.NIGHTMARE_FUEL.get());
                output.accept(ModItems.SPIRIT_FUEL.get());
                output.accept(ModItems.SPIRIT_FRAGMENT.get());
                output.accept(ModBlocks.MORTAR.get());
                output.accept(ModItems.GILDED_POTATO.get());
                output.accept(ModItems.GILDED_MASHED_POTATO.get());

                output.accept(ModItems.MASTER_BAND_OF_MANA.get());
                output.accept(ModItems.FROST_RING.get());
                output.accept(ModItems.CURSE_RING.get());
                output.accept(ModItems.WALL_JUMP_AMULET.get());
                output.accept(ModItems.WALL_CLIMB_AMULET.get());
                output.accept(ModItems.PARKOUR_AMULET.get());
                output.accept(ModItems.PARKOUR_AMULET2.get());
                output.accept(ModItems.MANA_DRIVE_RING.get());
                output.accept(ModItems.ELVEN_KING_RING.get());
                output.accept(ModItems.JINGWEI_FEATHER.get());
                output.accept(ModItems.PURE_DAISY_PENDANT.get());
                output.accept(ModItems.SUPER_CROWN.get());
                output.accept(ModItems.BOTTLED_FLAME.get());
                
                output.accept(ModItems.ORICHALCOS_HELMET_FEMALE.get());
                output.accept(ModItems.ORICHALCOS_CHESTPLATE_FEMALE.get());
                output.accept(ModItems.ORICHALCOS_LEGGINGS_FEMALE.get());
                output.accept(ModItems.ORICHALCOS_BOOTS_FEMALE.get());


                ((CustomCreativeTabContents) ModItems.NATURE_ORB.get()).addToCreativeTab(ModItems.NATURE_ORB.get(), output); // don't like this implementation will make my own eventually
                ((CustomCreativeTabContents) ModItems.GOD_CORE.get()).addToCreativeTab(ModItems.GOD_CORE.get(), output);
            })
            .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
