package noobanidus.mods.carrierbees.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import java.util.Objects;

public class EffectBuilder {
  private Effect effect;
  private int duration;
  private int amplifier;

  public EffectBuilder(Effect effect, int duration, int amplifier) {
    this.effect = effect;
    this.duration = duration;
    this.amplifier = amplifier;
  }

  public EffectInstance build() {
    return new EffectInstance(effect, duration, amplifier);
  }

  public CompoundNBT asTag() {
    CompoundNBT tag = new CompoundNBT();
    tag.putInt("d", duration);
    tag.putInt("a", amplifier);
    tag.putString("n", Objects.requireNonNull(effect.getRegistryName()).toString());
    return tag;
  }
}
