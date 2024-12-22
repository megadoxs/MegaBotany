package io.github.megadoxs.extrabotany_reborn.common.block.entity_block;

import io.github.megadoxs.extrabotany_reborn.common.block.ModBlockEntities;
import io.github.megadoxs.extrabotany_reborn.common.craft.recipe.CrushingRecipe;
import io.github.megadoxs.extrabotany_reborn.common.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MortarBlockEntity extends BlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int progress = 0;

    public MortarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MORTAR.get(), pPos, pBlockState);
    }

    // subject to change before release
    public void onUse(Level level, BlockPos pos, Player player) {

        ItemStack selectedItem = player.getInventory().getSelected().copy();
        selectedItem.setCount(1);

        ItemStack storedItem = itemHandler.getStackInSlot(0);

        if (!storedItem.isEmpty()) {
            if (selectedItem.is(ModTags.Items.HAMMERS) && getCurrentRecipe().isPresent()) {
                if (this.progress == getCurrentRecipe().get().getStrikes() - 1) {
                    Containers.dropContents(this.level, this.worldPosition, NonNullList.of(ItemStack.EMPTY, getCurrentRecipe().get().getResultItem(null)));
                    itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    this.progress = 0;
                }
                else this.progress++;
            }
            else {
                if (selectedItem.isEmpty()) {
                    int slot = player.getInventory().selected;
                    player.getInventory().add(slot, storedItem);
                }
                else player.getInventory().add(storedItem);

                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                this.progress = 0;
            }
        }
        else if (!selectedItem.isEmpty()) {
            itemHandler.setStackInSlot(0, selectedItem);
            player.getInventory().removeFromSelected(false);
        }
        else return;
        setChanged();
    }

    private Optional<CrushingRecipe> getCurrentRecipe(){
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, this.itemHandler.getStackInSlot(0));

        return this.level.getRecipeManager().getRecipeFor(CrushingRecipe.Type.INSTANCE, container, level);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    
    public ItemStack getRenderStack(){
        return itemHandler.getStackInSlot(0);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("mortar.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("mortar.progress");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
