package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import io.github.megadoxs.extrabotany_reborn.common.item.ModItems;
import io.github.megadoxs.extrabotany_reborn.common.network.C2SPacket.MagicAuraPacket;
import io.github.megadoxs.extrabotany_reborn.common.network.ModNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.common.block.flower.PureDaisyBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class PureDaisyPendant extends BaubleItem {

    public static final String TAG_USE = "usecount";
    private static final String TAG_COOLDOWN = "cooldown";

    public PureDaisyPendant(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && inCooldown(player))
            ItemNBTHelper.setInt(stack, TAG_COOLDOWN, ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 3000) - 1);
    }

    @SubscribeEvent
    public void RightClickBlock(PlayerInteractEvent.RightClickBlock evt) {
        if (getStack(evt.getEntity()) != ItemStack.EMPTY && !evt.getEntity().isCrouching() && !inCooldown(evt.getEntity()) && ManaItemHandler.instance().requestManaExact(new ItemStack(Items.APPLE), evt.getEntity(), 50, false)) {
            Level world = evt.getLevel();
            world.getProfiler().push("findRecipe");
            PureDaisyRecipe recipe = this.findRecipe(evt.getPos(), evt.getLevel());
            world.getProfiler().pop();
            if (recipe != null) {
                recipe.set(world, evt.getPos(), new PureDaisyBlockEntity(evt.getPos(), null));
                world.blockEvent(evt.getPos(), recipe.getOutputState().getBlock(), 1, 1);
                world.levelEvent(2001, evt.getPos(), Block.getId(recipe.getOutputState()));
                ManaItemHandler.instance().requestManaExact(new ItemStack(Items.APPLE), evt.getEntity(), 50, false);
                setCooldown(evt.getEntity());
            }
        }
    }

    private @Nullable PureDaisyRecipe findRecipe(BlockPos coords, Level level) {
        BlockState state = level.getBlockState(coords);

        for (PureDaisyRecipe pureDaisyRecipe : BotaniaRecipeTypes.getRecipes(level, BotaniaRecipeTypes.PURE_DAISY_TYPE).values()) {
            if ((Recipe<?>) pureDaisyRecipe instanceof PureDaisyRecipe daisyRecipe) {
                if (daisyRecipe.matches(level, coords, new PureDaisyBlockEntity(coords, null), state)) {
                    return daisyRecipe;
                }
            }
        }

        return null;
    }

    private ItemStack getStack(Player player) {
        if (CuriosApi.getCuriosInventory(player).resolve().isPresent())
            if (CuriosApi.getCuriosInventory(player).resolve().get().findFirstCurio(ModItems.PURE_DAISY_PENDANT.get()).isPresent())
                return CuriosApi.getCuriosInventory(player).resolve().get().findFirstCurio(ModItems.PURE_DAISY_PENDANT.get()).get().stack();
        return ItemStack.EMPTY;
    }

    private boolean inCooldown(Player player) {
        return ItemNBTHelper.getInt(getStack(player), TAG_COOLDOWN, 0) > 0;
    }

    private void setCooldown(Player player) {
        if (ItemNBTHelper.getInt(getStack(player), TAG_USE, 0) < 63) // works for a stack before going on cd
            ItemNBTHelper.setInt(getStack(player), TAG_USE, ItemNBTHelper.getInt(getStack(player), TAG_USE, 0) + 1);
        else {
            ItemNBTHelper.setInt(getStack(player), TAG_COOLDOWN, 3000);
            ItemNBTHelper.setInt(getStack(player), TAG_USE, 0);
        }
    }
}
