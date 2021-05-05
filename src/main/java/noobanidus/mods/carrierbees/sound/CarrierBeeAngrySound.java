package noobanidus.mods.carrierbees.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;

@OnlyIn(Dist.CLIENT)
public class CarrierBeeAngrySound extends CarrierBeeSound {
  public CarrierBeeAngrySound(AppleBeeEntity p_i226058_1_) {
    super(p_i226058_1_, SoundEvents.ENTITY_BEE_LOOP_AGGRESSIVE, SoundCategory.NEUTRAL);
    this.repeatDelay = 0;
  }

  protected TickableSound getNextSound() {
    return new CarrierBeeFlightSound(this.beeInstance);
  }

  protected boolean shouldSwitchSound() {
    return !this.beeInstance.isAngry();
  }
}
