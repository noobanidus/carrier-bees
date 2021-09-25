package noobanidus.mods.carrierbees.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class GenericEffect extends Effect implements IBeeEffect {
  public GenericEffect() {
    super(EffectType.HARMFUL, 0x160a82);
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }
}
