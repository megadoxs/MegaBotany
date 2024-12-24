package io.github.megadoxs.extrabotany_reborn.common.block.flower.generating;

import io.github.megadoxs.extrabotany_reborn.common.block.MegaBotanyFlowerBlocks;
import io.github.megadoxs.extrabotany_reborn.common.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.Objects;

public class BloodyEnchantressBlockEntity extends GeneratingFlowerBlockEntity {
    private static final String TAG_BURN_TIME = "burnTime";
    private static final String TAG_MANA_GENERATION = "manaGeneration";
    private static final int RANGE = 0;

    private int burnTime = 0;
    private int manaGeneration = 0;

    public BloodyEnchantressBlockEntity(BlockPos pos, BlockState state) {
        super(MegaBotanyFlowerBlocks.BLOODY_ENCHANTRESS, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (burnTime > 0) {
            burnTime--;
        }

        if (getLevel().isClientSide) {
            if (burnTime > 0 && getLevel().random.nextInt(10) == 0) {
                emitParticle(ParticleTypes.FLAME, 0.4 + Math.random() * 0.2, 0.7, 0.4 + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);
            }
            return;
        } else {
            if (burnTime > 0 && ticksExisted % 2 == 0) {
                addMana(manaGeneration);
            }
        }

        if (burnTime == 0) {
            if (getMana() < getMaxMana()) {

                for (LivingEntity living : getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                    if (living instanceof Player player && player.isCreative())
                        continue;

                    if (burnTime != 0) {
                        break;
                    }

                    int ea = (living.hasEffect(ModEffects.BLOOD_DEFICIENCY.get()) ? living.getEffect(ModEffects.BLOOD_DEFICIENCY.get()).getAmplifier() : -1) + 2;
                    manaGeneration = 10 / ea;

                    if (living.getHealth() >= 3)
                        burnTime = 60;
                    else
                        burnTime = (int) (living.getHealth() * 20);

                    living.hurt(getLevel().damageSources().magic(), ea * 2);
                    living.addEffect(new MobEffectInstance(ModEffects.BLOOD_DEFICIENCY.get(), (int) (200 * Math.pow(3, ea - 1)), ea - 1));
                    getLevel().playSound(null, getEffectivePos(), BotaniaSounds.endoflame, SoundSource.BLOCKS, 1F, 1F);
                    sync();
                }
            }
        }
    }

    @Override
    public int getMaxMana() {
        return 300;
    }

    @Override
    public int getMana() {
        return super.getMana();
    }

    @Override
    public int getColor() {
        return 0x8B0000;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);

        cmp.putInt(TAG_BURN_TIME, burnTime);
        cmp.putInt(TAG_MANA_GENERATION, manaGeneration);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInt(TAG_BURN_TIME);
        manaGeneration = cmp.getInt(TAG_MANA_GENERATION);
    }
}