package noobanidus.mods.carrierbees.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;
import noobanidus.mods.carrierbees.init.ModSounds;

@OnlyIn(Dist.CLIENT)
public class BeehemothBeeFlightSound extends BeehemothBeeSound {
  public BeehemothBeeFlightSound(BeehemothEntity p_i226058_1_) {
    super(p_i226058_1_, ModSounds.BEEHEMOTH_LOOP.get(), SoundCategory.NEUTRAL);
    this.repeatDelay = 0;
  }

  protected TickableSound getNextSound() {
    return new BeehemothBeeFlightSound(this.beeInstance);
  }

  protected boolean shouldSwitchSound() {
    // TOOD
    return false; // Can't get angry yet
    /*    return !this.beeInstance.isAngry();*/
  }
}
