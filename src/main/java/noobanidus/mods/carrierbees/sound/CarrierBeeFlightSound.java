package noobanidus.mods.carrierbees.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;

@OnlyIn(Dist.CLIENT)
public class CarrierBeeFlightSound extends CarrierBeeSound {
  public CarrierBeeFlightSound(AppleBeeEntity p_i226059_1_) {
    super(p_i226059_1_, SoundEvents.ENTITY_BEE_LOOP, SoundCategory.NEUTRAL);
  }

  protected TickableSound getNextSound() {
    return new CarrierBeeAngrySound(this.beeInstance);
  }

  protected boolean shouldSwitchSound() {
    return this.beeInstance.isAngry();
  }
}