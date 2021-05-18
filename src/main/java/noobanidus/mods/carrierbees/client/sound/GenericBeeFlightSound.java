package noobanidus.mods.carrierbees.client.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.IAppleBee;

@OnlyIn(Dist.CLIENT)
public class GenericBeeFlightSound<T extends AnimalEntity & IAppleBee> extends CarrierBeeSound<T> {
  public GenericBeeFlightSound(T p_i226059_1_) {
    super(p_i226059_1_, SoundEvents.ENTITY_BEE_LOOP, SoundCategory.NEUTRAL);
  }

  protected TickableSound getNextSound() {
    return new CarrierBeeAngrySound<>(this.beeInstance);
  }

  protected boolean shouldSwitchSound() {
    return this.beeInstance.safeIsAngry();
  }
}