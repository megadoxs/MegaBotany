package io.github.megadoxs.megabotany.common.item;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.item.equipment.armor.manaweavedSteel.ManaweavedSteelArmorItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.manaweavedSteel.ManaweavedSteelHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.orichalcos.OrichalcosArmorItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.orichalcos.OrichalcosHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.photonium.PhotoniumArmorItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.photonium.PhotoniumHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.shadowium.ShadowiumArmorItem;
import io.github.megadoxs.megabotany.common.item.equipment.armor.shadowium.ShadowiumHelmetItem;
import io.github.megadoxs.megabotany.common.item.equipment.bauble.*;
import io.github.megadoxs.megabotany.common.item.equipment.shield.ElementiumShield;
import io.github.megadoxs.megabotany.common.item.equipment.shield.ManasteelShield;
import io.github.megadoxs.megabotany.common.item.equipment.shield.TerrasteelShield;
import io.github.megadoxs.megabotany.common.item.equipment.tool.PhotoniumSword;
import io.github.megadoxs.megabotany.common.item.equipment.tool.ShadowiumKatana;
import io.github.megadoxs.megabotany.common.item.equipment.tool.WalkingCane;
import io.github.megadoxs.megabotany.common.item.food.MegaBotanyFoods;
import io.github.megadoxs.megabotany.common.item.fuel.ManaFuel;
import io.github.megadoxs.megabotany.common.item.lens.ManaLens;
import io.github.megadoxs.megabotany.common.item.lens.SmeltLens;
import io.github.megadoxs.megabotany.common.item.relic.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.item.lens.LensItem;

public class MegaBotanyItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MegaBotany.MOD_ID);

    public static final RegistryObject<Item> MANASTEEL_SHIELD = ITEMS.register("manasteel_shield", () -> new ManasteelShield(new Item.Properties().defaultDurability(600)));
    public static final RegistryObject<Item> ELEMENTIUM_SHIELD = ITEMS.register("elementium_shield", () -> new ElementiumShield(new Item.Properties().defaultDurability(1440)));
    public static final RegistryObject<Item> TERRASTEEL_SHIELD = ITEMS.register("terrasteel_shield", () -> new TerrasteelShield(new Item.Properties().defaultDurability(4600)));
    public static final RegistryObject<Item> WALKING_CANE = ITEMS.register("walking_cane", () -> new WalkingCane(new Item.Properties().defaultDurability(64)));

    //relics
    public static final RegistryObject<Item> FAILNAUGHT = ITEMS.register("failnaught", () -> new Failnaught(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> EXCALIBER = ITEMS.register("excaliber", () -> new Excaliber(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> ACHILLED_SHIELD = ITEMS.register("achilled_shield", () -> new AchilledShield(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> ALL_FOR_ONE = ITEMS.register("all_for_one", () -> new AFORing(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> INFINITE_DRINK = ITEMS.register("holy_grail", () -> new InfiniteDrink(new Item.Properties()));
    public static final RegistryObject<Item> INFINITE_BREW = ITEMS.register("holy_grail_brew", () -> new InfiniteBrew(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> INFINITE_SPLASH_BREW = ITEMS.register("holy_hand_grenade", () -> new InfiniteSplashBrew(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> INFINITE_LINGERING_BREW = ITEMS.register("infinite_lingering_brew", () -> new InfiniteLingeringBrew(new Item.Properties().stacksTo(1))); //placeholder name
    public static final RegistryObject<Item> ABSOLUTION_PENDANT = ITEMS.register("absolution_pendant", () -> new DamageNullification(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1))); //placeholder name
    public static final RegistryObject<Item> PANDORA_BOX = ITEMS.register("pandora_box", () -> new PandoraBox(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> EARTH_ESSENCE = ITEMS.register("earth_essence", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> MEDAL_OF_HEROISM = ITEMS.register("medal_of_heroism", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> NATURE_ORB = ITEMS.register("nature_orb", () -> new NatureOrb(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> MASTER_BAND_OF_MANA = ITEMS.register("master_band_of_mana", () -> new MasterBandOfMana(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FROST_RING = ITEMS.register("frost_ring", () -> new RingOfFrost(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WALL_JUMP_AMULET = ITEMS.register("wall_jump_amulet", () -> new WallJumpAmulet(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WALL_CLIMB_AMULET = ITEMS.register("wall_climb_amulet", () -> new WallClimbAmulet(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> JUMP_AMULET = ITEMS.register("jump_amulet", () -> new JumpAmulet(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MANA_DRIVE_RING = ITEMS.register("mana_drive_ring", () -> new ManaDriveRing(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GOD_CORE = ITEMS.register("god_core", () -> new CoreGod(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> JINGWEI_FEATHER = ITEMS.register("jingwei_feather", () -> new JingweiFeather(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PURE_DAISY_PENDANT = ITEMS.register("pure_daisy_pendant", () -> new PureDaisyPendant(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUPER_CROWN = ITEMS.register("super_crown", () -> new SuperCrown(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BOTTLED_FLAME = ITEMS.register("bottled_flame", () -> new BottledFlame(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MANAWEAVEDSTEEL_HELMET = ITEMS.register("manaweavedsteel_helmet", () -> new ManaweavedSteelHelmetItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> MANAWEAVEDSTEEL_CHESTPLATE = ITEMS.register("manaweavedsteel_chestplate", () -> new ManaweavedSteelArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MANAWEAVEDSTEEL_LEGGINGS = ITEMS.register("manaweavedsteel_leggings", () -> new ManaweavedSteelArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MANAWEAVEDSTEEL_BOOTS = ITEMS.register("manaweavedsteel_boots", () -> new ManaweavedSteelArmorItem(ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> PHOTONIUM_HELMET = ITEMS.register("photonium_helmet", () -> new PhotoniumHelmetItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> PHOTONIUM_CHESTPLATE = ITEMS.register("photonium_chestplate", () -> new PhotoniumArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> PHOTONIUM_LEGGINGS = ITEMS.register("photonium_leggings", () -> new PhotoniumArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> PHOTONIUM_BOOTS = ITEMS.register("photonium_boots", () -> new PhotoniumArmorItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> SHORT_PHOTONIUM_SWORD = ITEMS.register("photonium_sword", () -> new PhotoniumSword(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SHADOWIUM_HELMET = ITEMS.register("shadowium_helmet", () -> new ShadowiumHelmetItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SHADOWIUM_CHESTPLATE = ITEMS.register("shadowium_chestplate", () -> new ShadowiumArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SHADOWIUM_LEGGINGS = ITEMS.register("shadowium_leggings", () -> new ShadowiumArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SHADOWIUM_BOOTS = ITEMS.register("shadowium_boots", () -> new ShadowiumArmorItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> SHADOWIUM_KATANA = ITEMS.register("shadowium_katana", () -> new ShadowiumKatana(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ORICHALCOS_HELMET_FEMALE = ITEMS.register("orichalcos_helmet_female", () -> new OrichalcosHelmetItem(ArmorItem.Type.HELMET, OrichalcosArmorItem.Version.FEMALE, new Item.Properties()));
    public static final RegistryObject<Item> ORICHALCOS_CHESTPLATE_FEMALE = ITEMS.register("orichalcos_chestplate_female", () -> new OrichalcosArmorItem(ArmorItem.Type.CHESTPLATE, OrichalcosArmorItem.Version.FEMALE, new Item.Properties()));
    public static final RegistryObject<Item> ORICHALCOS_LEGGINGS_FEMALE = ITEMS.register("orichalcos_leggings_female", () -> new OrichalcosArmorItem(ArmorItem.Type.LEGGINGS, OrichalcosArmorItem.Version.FEMALE, new Item.Properties()));
    public static final RegistryObject<Item> ORICHALCOS_BOOTS_FEMALE = ITEMS.register("orichalcos_boots_female", () -> new OrichalcosArmorItem(ArmorItem.Type.BOOTS, OrichalcosArmorItem.Version.FEMALE, new Item.Properties()));

    public static final RegistryObject<Item> SPIRIT_FUEL = ITEMS.register("spirit_fuel", () -> new ManaFuel(new Item.Properties().food(MegaBotanyFoods.SPIRIT_FUEL)));
    public static final RegistryObject<Item> NIGHTMARE_FUEL = ITEMS.register("nightmare_fuel", () -> new ManaFuel(new Item.Properties().food(MegaBotanyFoods.NIGHTMARE_FUEL)));
    public static final RegistryObject<Item> FRIED_CHICKEN = ITEMS.register("fried_chicken", () -> new Item(new Item.Properties().food(MegaBotanyFoods.FRIED_CHICKEN)));

    public static final RegistryObject<Item> ORICHALCOS_INGOT = ITEMS.register("orichalcos_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHADOWIUM_INGOT = ITEMS.register("shadowium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PHOTONIUM_INGOT = ITEMS.register("photonium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TICKET = ITEMS.register("ticket", () -> new Ticket(new Item.Properties()));

    public static final RegistryObject<Item> SMELTING_LENS = ITEMS.register("smelting_lens", () -> new LensItem(new Item.Properties().stacksTo(16), new SmeltLens(), LensItem.PROP_DAMAGE | LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
    public static final RegistryObject<Item> MANA_LENS = ITEMS.register("mana_lens", () -> new LensItem(new Item.Properties().stacksTo(16), new ManaLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
