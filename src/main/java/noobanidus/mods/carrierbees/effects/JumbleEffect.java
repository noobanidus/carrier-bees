package noobanidus.mods.carrierbees.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class JumbleEffect extends Effect implements IBeeEffect {
  public JumbleEffect() {
    super(EffectType.HARMFUL, 0xff00de);
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }
}
