package io.github.megadoxs.extrabotany_reborn.common.item.equipment.bauble;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class ManaDriveRing extends BaubleItem {
    public ManaDriveRing(Properties props) {
        super(props);
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            int range = 7; // lol will make it a static final variable
            for (int x = -range; x < range; x++)
                for (int y = -range; y < range; y++)
                    for (int z = -range; z < range; z++) {
                        Vec3 playerPosition = player.position().add(x, y, z);
                        BlockEntity blockEntity = player.level().getBlockEntity(new BlockPos((int) playerPosition.x, (int) playerPosition.y, (int) playerPosition.z));
                        if (blockEntity instanceof FunctionalFlowerBlockEntity flowerEntity) {
                            int manaToUse = flowerEntity.getMaxMana() - flowerEntity.getMana();
                            if (ManaItemHandler.instance().requestManaExact(stack, player, manaToUse, true))
                                flowerEntity.addMana(manaToUse);
                        }
                    }
        }
    }
}
