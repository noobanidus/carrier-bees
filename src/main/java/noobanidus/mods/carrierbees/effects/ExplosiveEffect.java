package noobanidus.mods.carrierbees.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;

public class ExplosiveEffect extends InstantEffect implements IBeeEffect {
  public ExplosiveEffect() {
    super(EffectType.HARMFUL, 0xeb4e10);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      player.level.explode(player, DamageSource.explosion(player), null, player.getX(), player.getY(), player.getZ(), 2.0F, false, Explosion.Mode.BREAK);
    }
  }
}
