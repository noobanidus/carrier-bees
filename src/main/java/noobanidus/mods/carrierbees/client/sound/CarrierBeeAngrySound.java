package noobanidus.mods.carrierbees.client.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.IAppleBee;

@OnlyIn(Dist.CLIENT)
public class CarrierBeeAngrySound<T extends AnimalEntity & IAppleBee> extends CarrierBeeSound<T> {
  public CarrierBeeAngrySound(T p_i226058_1_) {
    super(p_i226058_1_, SoundEvents.BEE_LOOP_AGGRESSIVE, SoundCategory.NEUTRAL);
    this.delay = 0;
  }

  protected TickableSound getNextSound() {
    return new CarrierBeeFlightSound<>(this.beeInstance);
  }

  protected boolean shouldSwitchSound() {
    return !this.beeInstance.safeIsAngry();
  }
}
