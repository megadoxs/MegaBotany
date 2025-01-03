package io.github.megadoxs.megabotany.common.item;

import io.github.megadoxs.megabotany.common.entity.GaiaGuardianIII;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class Ticket extends Item {
    public Ticket(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        return GaiaGuardianIII.spawn(context.getPlayer(), context.getItemInHand(), context.getLevel(), context.getClickedPos()) ? InteractionResult.sidedSuccess(context.getLevel().isClientSide()) : InteractionResult.FAIL;
    }
}
