package noobanidus.mods.carrierbees.client.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.IAppleBee;
import noobanidus.mods.carrierbees.init.ModSounds;

@OnlyIn(Dist.CLIENT)
public class GenericBeeAngrySound<T extends AnimalEntity & IAppleBee> extends CarrierBeeSound<T> {
  public GenericBeeAngrySound(T p_i226058_1_) {
    super(p_i226058_1_, ModSounds.GENERIC_BEE_LOOP_AGGRESSIVE.get(), SoundCategory.NEUTRAL);
    this.repeatDelay = 0;
  }

  protected TickableSound getNextSound() {
    return new CarrierBeeFlightSound<>(this.beeInstance);
  }

  protected boolean shouldSwitchSound() {
    return !this.beeInstance.safeIsAngry();
  }
}
