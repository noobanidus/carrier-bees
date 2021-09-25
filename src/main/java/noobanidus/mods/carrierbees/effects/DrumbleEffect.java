package noobanidus.mods.carrierbees.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

import java.util.Random;

public class DrumbleEffect extends Effect implements IBeeEffect {
  private static final Random rand = new Random();

  public DrumbleEffect() {
    super(EffectType.HARMFUL, 0x9c0000);
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (rand.nextInt(8) == 0) {
      entity.level.addParticle(ParticleTypes.END_ROD, entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0), 0, 0, 0);
    }
    if (!entity.level.isClientSide && rand.nextInt(30) == 0) {
      if (entity.getEffect(Effects.MOVEMENT_SLOWDOWN) == null) {
        entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 15, 10, false, false, true));
      }
    }
  }
}
