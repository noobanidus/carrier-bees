package noobanidus.mods.carrierbees.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class TumbleEffect extends Effect implements IBeeEffect {
  public TumbleEffect() {
    super(EffectType.HARMFUL, 0x532eca);
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }
}
