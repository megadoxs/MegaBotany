package io.github.megadoxs.megabotany.common.item.equipment.bauble;

import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import io.github.megadoxs.megabotany.common.network.C2SPacket.PureDaisyPendantRecipePacket;
import io.github.megadoxs.megabotany.common.network.MegaBotanyNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.flower.PureDaisyBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.xplat.BotaniaConfig;

public class PureDaisyPendant extends BaubleItem {
    private String TAG_POS = "pos";
    private String TAG_TICK_REMAINING = "tick_remaining";
    private int DELAY = 200;


    public PureDaisyPendant(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        Level level = entity.level();
        BlockPos pos = getPos(stack);
        if(level.isClientSide && findRecipe(pos, level) != null){
            if (Minecraft.getInstance().options.keyUse.isDown()){
                ItemNBTHelper.setInt(stack, TAG_TICK_REMAINING, ItemNBTHelper.getInt(stack, TAG_TICK_REMAINING, DELAY) - 1);
                SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), 1F, 1F, 1F, 5);
                level.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
                if (ItemNBTHelper.getInt(stack, TAG_TICK_REMAINING, DELAY) == 0){
                    MegaBotanyNetwork.sendToServer(new PureDaisyPendantRecipePacket(pos));
                    for (int i = 0; i < 25; i++) {
                        double x = pos.getX() + Math.random();
                        double y = pos.getY() + Math.random() + 0.5;
                        double z = pos.getZ() + Math.random();

                        WispParticleData data2 = WispParticleData.wisp((float) Math.random() / 2F, 1, 1, 1);
                        level.addParticle(data2, x, y, z, 0, 0, 0);
                    }
                }
            }
            else
                ItemNBTHelper.setInt(stack, TAG_TICK_REMAINING, DELAY);
        }
    }

    @SubscribeEvent
    public void RightClickBlock(PlayerInteractEvent.RightClickBlock evt) {
        ItemStack stack = getStack(evt.getEntity());
        BlockPos pos = getPos(stack);

        if ((pos == null || !pos.equals(evt.getPos())) && findRecipe(evt.getPos(), evt.getLevel()) != null){
            CompoundTag blockPos = new CompoundTag();
            blockPos.putInt("x", evt.getPos().getX());
            blockPos.putInt("y", evt.getPos().getY());
            blockPos.putInt("z", evt.getPos().getZ());
            ItemNBTHelper.setCompound(stack, TAG_POS, blockPos);
            ItemNBTHelper.setInt(stack, TAG_TICK_REMAINING, DELAY);
        }
    }

    public BlockPos getPos(ItemStack stack) {
        CompoundTag blockTag = ItemNBTHelper.getCompound(stack, TAG_POS, true);
        if (blockTag == null) {
            return null;
        }
        return new BlockPos(blockTag.getInt("x"), blockTag.getInt("y"), blockTag.getInt("z"));
    }

    private static @Nullable PureDaisyRecipe findRecipe(BlockPos coords, Level level) {
        if (coords == null)
            return null;

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

    private static ItemStack getStack(Player player) {
        if (CuriosApi.getCuriosInventory(player).resolve().isPresent())
            if (CuriosApi.getCuriosInventory(player).resolve().get().findFirstCurio(MegaBotanyItems.PURE_DAISY_PENDANT.get()).isPresent())
                return CuriosApi.getCuriosInventory(player).resolve().get().findFirstCurio(MegaBotanyItems.PURE_DAISY_PENDANT.get()).get().stack();
        return ItemStack.EMPTY;
    }

    public static void applyRecipe(BlockPos pos, Level level) {
        level.getProfiler().push("findRecipe");
        PureDaisyRecipe recipe = findRecipe(pos, level);
        level.getProfiler().pop();
        if (recipe != null && recipe.set(level, pos, new PureDaisyBlockEntity(pos, null))) {
            if (BotaniaConfig.common().blockBreakParticles()) {
                level.levelEvent(2001, pos, Block.getId(recipe.getOutputState()));
            }
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
//            ManaItemHandler.instance().requestManaExact(stack, player, 50, true);
        }
    }
}
