package io.github.megadoxs.extrabotany_reborn.common.item.equipment.shield;

public class TerrasteelShield extends ManasteelShield{

    private static final int manaPerDamage = 100;

    public TerrasteelShield(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getManaPerDamage() {
        return manaPerDamage;
    }

// require me to do an event to detect when a attack is blocked
//    @Override
//    public void onAttackBlocked(ItemStack stack, EntityLivingBase attacked, float damage, DamageSource source) {
//        if(source.getImmediateSource() != null) {
//            if(attacked instanceof EntityPlayer) {
//                source.getImmediateSource().attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)attacked), damage/2);
//            } else {
//                source.getImmediateSource().attackEntityFrom(DamageSource.causeMobDamage(attacked), damage/2);
//            }
//            source.getImmediateSource().setFire(5);
//        }
//        super.onAttackBlocked(stack, attacked, damage, source);
//    }
}