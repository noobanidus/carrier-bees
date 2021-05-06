package noobanidus.mods.carrierbees.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import noobanidus.mods.carrierbees.entities.IAppleBee;

public abstract class CarrierBeeSound<T extends AnimalEntity & IAppleBee> extends TickableSound {
  protected final T beeInstance;
  private boolean hasSwitchedSound = false;

  public CarrierBeeSound(T bee, SoundEvent p_i46532_1_, SoundCategory p_i46532_2_) {
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
      this.x = this.beeInstance.getPosX();
      this.y = this.beeInstance.getPosY();
      this.z = this.beeInstance.getPosZ();
      float dist = MathHelper.sqrt(Entity.horizontalMag(this.beeInstance.getMotion()));
      if (this.beeInstance.isBeehemoth()) {
        dist *= 100;
      }
      if (dist >= 0.005f) {
        this.pitch = MathHelper.lerp(MathHelper.clamp(dist, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
        this.volume = MathHelper.lerp(MathHelper.clamp(dist, 0.0F, 0.5F), 0.0F, 1.2F);
      } else {
        this.pitch = 0.0F;
        this.volume = 0.0F;
      }
    } else {
      this.finishPlaying();
    }
  }

  private float getMinPitch() {
    if (this.beeInstance.isBeehemoth()) {
      return this.beeInstance.isChild() ? 0.4F : 0.2F;
    } else {
      return this.beeInstance.isChild() ? 1.1F : 0.7F;
    }
  }

  private float getMaxPitch() {
    if (this.beeInstance.isBeehemoth()) {
      return this.beeInstance.isChild() ? 0.6F : 0.4F;
    } else {
      return this.beeInstance.isChild() ? 1.5F : 1.1F;
    }
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
