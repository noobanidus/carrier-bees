package noobanidus.mods.carrierbees.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class GenericEffect extends Effect {
  public GenericEffect() {
    super(EffectType.HARMFUL, 0x160a82);
  }

  @Override
  public boolean isReady(int p_76397_1_, int p_76397_2_) {
    return true;
  }
}
