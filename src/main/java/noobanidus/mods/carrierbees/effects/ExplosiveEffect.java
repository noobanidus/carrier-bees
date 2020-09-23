package noobanidus.mods.carrierbees.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;

public class ExplosiveEffect extends InstantEffect {
  public ExplosiveEffect() {
    super(EffectType.HARMFUL, 0xeb4e10);
  }

  @Override
  public void performEffect(LivingEntity entity, int amplifier) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      player.world.createExplosion(player, DamageSource.causeExplosionDamage(player), player.posX, player.posY, player.posZ, 2.0F, false, Explosion.Mode.BREAK);
    }
  }
}
