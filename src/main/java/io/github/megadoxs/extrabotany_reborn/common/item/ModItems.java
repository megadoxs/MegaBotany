package io.github.megadoxs.extrabotany_reborn.common.item;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble.*;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.shield.ElementiumShield;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.shield.ManasteelShield;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.shield.TerrasteelShield;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.tool.hammer.ElementiumHammer;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.tool.hammer.ManasteelHammer;
import io.github.megadoxs.extrabotany_reborn.common.item.equipment.tool.hammer.TerrasteelHammer;
import io.github.megadoxs.extrabotany_reborn.common.item.food.ModFoods;
import io.github.megadoxs.extrabotany_reborn.common.item.food.NightmareFuel;
import io.github.megadoxs.extrabotany_reborn.item.fuel.Fuel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExtraBotany_Reborn.MOD_ID);

    public static final RegistryObject<Item> MANASTEEL_SHIELD = ITEMS.register("manasteel_shield", () -> new ManasteelShield(new Item.Properties().defaultDurability(600)));
    public static final RegistryObject<Item> ELEMENTIUM_SHIELD = ITEMS.register("elementium_shield", () -> new ElementiumShield(new Item.Properties().defaultDurability(1440)));
    public static final RegistryObject<Item> TERRASTEEL_SHIELD = ITEMS.register("terrasteel_shield", () -> new TerrasteelShield(new Item.Properties().defaultDurability(4600)));
    public static final RegistryObject<Item> MANASTEEL_HAMMER = ITEMS.register("manasteel_hammer", () -> new ManasteelHammer(new Item.Properties().defaultDurability(400)));
    public static final RegistryObject<Item> ELEMENTIUM_HAMMER = ITEMS.register("elementium_hammer", () -> new ElementiumHammer(new Item.Properties().defaultDurability(900)));
    public static final RegistryObject<Item> TERRASTEEL_HAMMER = ITEMS.register("terrasteel_hammer", () -> new TerrasteelHammer(new Item.Properties().defaultDurability(3000)));


    public static final RegistryObject<Item> NATURE_ORB = ITEMS.register("nature_orb", () -> new NatureOrb(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> MASTER_BAND_OF_MANA = ITEMS.register("master_band_of_mana", () -> new MasterBandOfMana(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FROST_RING = ITEMS.register("frost_ring", () -> new RingOfFrost(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CURSE_RING = ITEMS.register("curse_ring", () -> new RingOfCurse(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WALL_JUMP_AMULET = ITEMS.register("wall_jump_amulet", () -> new WallJumpAmulet(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WALL_CLIMB_AMULET = ITEMS.register("wall_climb_amulet", () -> new WallClimbAmulet(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PARKOUR_AMULET = ITEMS.register("parkour_amulet", () -> new ParkourAmulet(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PARKOUR_AMULET2 = ITEMS.register("parkour_amulet2", () -> new ParkourAmulet2(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MANA_DRIVE_RING = ITEMS.register("mana_drive_ring", () -> new ManaDriveRing(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ELVEN_KING_RING = ITEMS.register("elven_king_ring", () -> new ElvenKingRing(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GOD_CORE = ITEMS.register("god_core", () -> new CoreGod(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> JINGWEI_FEATHER = ITEMS.register("jingwei_feather", () -> new JingweiFeather(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> SPIRIT_FRAGMENT = ITEMS.register("spirit_fragment", () -> new Fuel(new Item.Properties(), Fuel.BurnTime.SPIRIT_FUEL/8));
    public static final RegistryObject<Item> SPIRIT_FUEL = ITEMS.register("spirit_fuel", () -> new Fuel(new Item.Properties().food(ModFoods.SPIRIT_FUEL), Fuel.BurnTime.SPIRIT_FUEL));
    public static final RegistryObject<Item> NIGHTMARE_FUEL = ITEMS.register("nightmare_fuel", () -> new NightmareFuel(new Item.Properties().food(ModFoods.NIGHTMARE_FUEL), Fuel.BurnTime.NIGHTMARE_FUEL));
    public static final RegistryObject<Item> GILDED_POTATO = ITEMS.register("gilded_potato", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GILDED_MASHED_POTATO = ITEMS.register("gilded_mashed_potato", () -> new Item(new Item.Properties().food(ModFoods.GILDED_MASHED_POTATO)));

    public static final RegistryObject<Item> ORICHALCOS_INGOT = ITEMS.register("orichalcos_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHADOWIUM_INGOT = ITEMS.register("shadowium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PHOTONIUM_INGOT = ITEMS.register("photonium_ingot", () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
