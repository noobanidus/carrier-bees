package noobanidus.mods.carrierbees.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;

public abstract class BeehemothBeeSound extends TickableSound {
  protected final BeehemothEntity beeInstance;
  private boolean hasSwitchedSound = false;

  public BeehemothBeeSound(BeehemothEntity bee, SoundEvent p_i46532_1_, SoundCategory p_i46532_2_) {
    super(p_i46532_1_, p_i46532_2_);
    this.beeInstance = bee;
    this.x = bee.getPosX();
    this.y = bee.getPosY();
    this.z = bee.getPosZ();
    this.repeat = true;
    this.repeatDelay = 0;
    this.volume = 0.0f;
  }

  @Override
  public void tick() {
    Minecraft mc = Minecraft.getInstance();
    if (this.shouldSwitchSound() && !isDonePlaying()) {
      mc.getSoundHandler().playOnNextTick(this.getNextSound());
      this.hasSwitchedSound = true;
    }

    if (this.beeInstance.isAlive() && !this.hasSwitchedSound) {
      this.x = (double) ((float) this.beeInstance.getPosX());
      this.y = (double) ((float) this.beeInstance.getPosY());
      this.z = (double) ((float) this.beeInstance.getPosZ());
      float lvt_2_1_ = MathHelper.sqrt(Entity.horizontalMag(this.beeInstance.getMotion()));
      if ((double) lvt_2_1_ >= 0.01D) {
        this.pitch = MathHelper.lerp(MathHelper.clamp(lvt_2_1_, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
        this.volume = MathHelper.lerp(MathHelper.clamp(lvt_2_1_, 0.0F, 0.5F), 0.0F, 1.2F);
      } else {
        this.pitch = 0.0F;
        this.volume = 0.0F;
      }

    } else {
      this.finishPlaying();
    }
  }

  private float getMinPitch() {
    return this.beeInstance.isChild() ? 0.4F : 0.2F;
  }

  private float getMaxPitch() {
    return this.beeInstance.isChild() ? 0.6F : 0.4F;
  }

  public boolean canBeSilent() {
    return true;
  }

  public boolean shouldPlaySound() {
    return !this.beeInstance.isSilent();
  }

  protected abstract TickableSound getNextSound();

  protected abstract boolean shouldSwitchSound();
}
