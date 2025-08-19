package io.github.megadoxs.megabotany.common.item.relic;

import com.google.common.base.Suppliers;
import io.github.megadoxs.megabotany.common.item.MegaBotanyItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.common.item.relic.RelicItem;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "megabotany", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PandoraBox extends RelicItem {
    public PandoraBox(Properties props) {
        super(props);
    }

    public static final Supplier<List<ItemStack>> RELIC_STACKS = Suppliers.memoize(() -> List.of(
                    new ItemStack(MegaBotanyItems.INFINITE_DRINK.get()),
                    new ItemStack(MegaBotanyItems.EXCALIBUR.get()),
                    new ItemStack(MegaBotanyItems.ACHILLES_SHIELD.get()),
                    new ItemStack(MegaBotanyItems.ALL_FOR_ONE.get()),
                    new ItemStack(MegaBotanyItems.FAILNAUGHT.get()),
                    new ItemStack(MegaBotanyItems.ABSOLUTION_PENDANT.get())
            )
    );

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        var relic = XplatAbstractions.INSTANCE.findRelic(stack);

        if (relic != null && relic.isRightPlayer(player)) {
            if (world.isClientSide) {
                return InteractionResultHolder.success(stack);
            }

            world.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.diceOfFate, SoundSource.PLAYERS, 1F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

            List<Integer> possible = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                if (!hasRelicAlready(player, i)) {
                    possible.add(i);
                }
            }

            if (!possible.isEmpty()) {
                int relicIdx = possible.get(world.random.nextInt(possible.size()));
                var toGive = RELIC_STACKS.get().get(relicIdx).copy();
                return InteractionResultHolder.consume(toGive);
            } else {
                int roll = world.random.nextInt(6) + 1;
                ResourceLocation tableId = ResourceLocationHelper.prefix("dice/roll_" + roll);
                LootTable table = world.getServer().getLootData().getLootTable(tableId);
                LootParams context = new LootParams.Builder((ServerLevel) world)
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .withLuck(player.getLuck())
                        .create(LootContextParamSets.GIFT);

                List<ItemStack> generated = table.getRandomItems(context);
                for (ItemStack drop : generated) {
                    player.getInventory().placeItemBackInInventory(drop);
                }

                stack.shrink(1);
                return InteractionResultHolder.consume(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    private boolean hasRelicAlready(Player player, int relicId) {
        if (relicId < 0 || relicId > 6 || !(player instanceof ServerPlayer mpPlayer)) {
            return true;
        }

        var stack = RELIC_STACKS.get().get(relicId);
        var relic = XplatAbstractions.INSTANCE.findRelic(stack);

        if (relic != null && relic.getAdvancement() != null) {
            return PlayerHelper.hasAdvancement(mpPlayer, relic.getAdvancement());
        }

        return false;
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null) {
            @Override
            public boolean shouldDamageWrongPlayer() {
                return false;
            }
        };
    }
}
