package io.github.megadoxs.extrabotany_reborn.common.datagen;

import io.github.megadoxs.extrabotany_reborn.common.ExtraBotany_Reborn;
import io.github.megadoxs.extrabotany_reborn.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import io.github.megadoxs.extrabotany_reborn.common.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, ExtraBotany_Reborn.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS).add(
                MegaBotanyFlowerBlocks.bloodyEnchantress.asItem(),
                MegaBotanyFlowerBlocks.SunshineLily.asItem()
        );

        //only works because all floating flowers are specials
        this.tag(BotaniaTags.Items.SPECIAL_FLOATING_FLOWERS).add(
                ForgeRegistries.BLOCKS.getEntries().stream().filter( entry -> ExtraBotany_Reborn.MOD_ID.equals(entry.getKey().location().getNamespace()) && entry.getValue() instanceof FloatingFlowerBlock).map(entry -> entry.getValue().asItem()).toArray(Item[]::new)
        );

        this.tag(ModTags.Items.HAMMERS).add(
                ModItems.MANASTEEL_HAMMER.get(),
                ModItems.ELEMENTIUM_HAMMER.get(),
                ModItems.TERRASTEEL_HAMMER.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "ring"))).add(
                ModItems.MASTER_BAND_OF_MANA.get(),
                ModItems.FROST_RING.get(),
                ModItems.CURSE_RING.get(),
                ModItems.MANA_DRIVE_RING.get(),
                ModItems.ELVEN_KING_RING.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "necklace"))).add(
                ModItems.WALL_JUMP_AMULET.get(),
                ModItems.WALL_CLIMB_AMULET.get(),
                ModItems.PARKOUR_AMULET.get(),
                ModItems.PARKOUR_AMULET2.get(),
                ModItems.PURE_DAISY_PENDANT.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "charm"))).add(
                ModItems.NATURE_ORB.get(),
                ModItems.JINGWEI_FEATHER.get(),
                ModItems.BOTTLED_FLAME.get()
        );

        this.tag(ItemTags.create(new ResourceLocation("curios", "head"))).add(
                ModItems.GOD_CORE.get(),
                ModItems.SUPER_CROWN.get()
        );
    }
}
