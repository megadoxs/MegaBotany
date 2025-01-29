package io.github.megadoxs.megabotany.common.datagen;

import io.github.megadoxs.megabotany.common.MegaBotany;
import io.github.megadoxs.megabotany.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import io.github.megadoxs.megabotany.common.util.MegaBotanyTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class MegaBotanyItemTagGenerator extends ItemTagsProvider {
    public MegaBotanyItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, MegaBotany.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantress.asItem(),
                MegaBotanyFlowerBlocks.sunshineLily.asItem(),
                MegaBotanyFlowerBlocks.moonlightLily.asItem(),
                MegaBotanyFlowerBlocks.omniviolet.asItem(),
                MegaBotanyFlowerBlocks.edelweiss.asItem(),
                MegaBotanyFlowerBlocks.tinkle.asItem(),
                MegaBotanyFlowerBlocks.bellFlower.asItem(),
                MegaBotanyFlowerBlocks.reikarLily.asItem(),
                MegaBotanyFlowerBlocks.stonesia.asItem()
        );

        this.tag(BotaniaTags.Items.FUNCTIONAL_SPECIAL_FLOWERS).add(
                MegaBotanyFlowerBlocks.geminiOrchid.asItem(),
                MegaBotanyFlowerBlocks.mirrortunia.asItem(),
                MegaBotanyFlowerBlocks.annoyingFlower.asItem(),
                MegaBotanyFlowerBlocks.necrofleur.asItem(),
                MegaBotanyFlowerBlocks.necrofleurChibi.asItem(),
                MegaBotanyFlowerBlocks.stardustLotus.asItem(),
                MegaBotanyFlowerBlocks.manalinkium.asItem()
        );

        this.tag(BotaniaTags.Items.MINI_FLOWERS).add(
                MegaBotanyFlowerBlocks.necrofleurChibi.asItem()
        );

        //only works because all floating flowers are specials
        this.tag(BotaniaTags.Items.SPECIAL_FLOATING_FLOWERS).add(
                ForgeRegistries.BLOCKS.getEntries().stream().filter(entry -> MegaBotany.MOD_ID.equals(entry.getKey().location().getNamespace()) && entry.getValue() instanceof FloatingFlowerBlock).map(entry -> entry.getValue().asItem()).toArray(Item[]::new)
        );

        this.tag(MegaBotanyTags.Items.HAMMERS).add(
                MegaBotanyItems.MANASTEEL_HAMMER.get(),
                MegaBotanyItems.ELEMENTIUM_HAMMER.get(),
                MegaBotanyItems.TERRASTEEL_HAMMER.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "ring"))).add(
                MegaBotanyItems.MASTER_BAND_OF_MANA.get(),
                MegaBotanyItems.FROST_RING.get(),
                MegaBotanyItems.CURSE_RING.get(),
                MegaBotanyItems.MANA_DRIVE_RING.get(),
                MegaBotanyItems.ELVEN_KING_RING.get(),
                MegaBotanyItems.ALL_FOR_ONE.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "necklace"))).add(
                MegaBotanyItems.WALL_JUMP_AMULET.get(),
                MegaBotanyItems.WALL_CLIMB_AMULET.get(),
                MegaBotanyItems.PARKOUR_AMULET.get(),
                MegaBotanyItems.PARKOUR_AMULET2.get(),
                MegaBotanyItems.PURE_DAISY_PENDANT.get(),
                MegaBotanyItems.ABSOLUTION_PENDANT.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "charm"))).add(
                MegaBotanyItems.NATURE_ORB.get(),
                MegaBotanyItems.JINGWEI_FEATHER.get(),
                MegaBotanyItems.BOTTLED_FLAME.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "head"))).add(
                MegaBotanyItems.GOD_CORE.get(),
                MegaBotanyItems.SUPER_CROWN.get()
        );
    }
}
