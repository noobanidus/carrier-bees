package noobanidus.mods.carrierbees.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class JumbleEffect extends Effect {
  public JumbleEffect() {
    super(EffectType.HARMFUL, 0xff00de);
  }

  @Override
  public boolean isReady(int p_76397_1_, int p_76397_2_) {
    return true;
  }
}
