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
    this.x = bee.getX();
    this.y = bee.getY();
    this.z = bee.getZ();
    this.looping = true;
    this.delay = 0;
    this.volume = 0.0f;
  }

  @Override
  public void tick() {
    Minecraft mc = Minecraft.getInstance();
    if (this.shouldSwitchSound() && !isStopped()) {
      mc.getSoundManager().queueTickingSound(this.getNextSound());
      this.hasSwitchedSound = true;
    }

    if (!this.beeInstance.removed && !this.hasSwitchedSound) {
      this.x = this.beeInstance.getX();
      this.y = this.beeInstance.getY();
      this.z = this.beeInstance.getZ();
      float dist = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(this.beeInstance.getDeltaMovement()));
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
      this.stop();
    }
  }

  private float getMinPitch() {
    if (this.beeInstance.isBeehemoth()) {
      return this.beeInstance.isBaby() ? 0.4F : 0.2F;
    } else {
      return this.beeInstance.isBaby() ? 1.1F : 0.7F;
    }
  }

  private float getMaxPitch() {
    if (this.beeInstance.isBeehemoth()) {
      return this.beeInstance.isBaby() ? 0.6F : 0.4F;
    } else {
      return this.beeInstance.isBaby() ? 1.5F : 1.1F;
    }
  }

  public boolean canStartSilent() {
    return true;
  }

  public boolean canPlaySound() {
    return !this.beeInstance.isSilent();
  }

  protected abstract TickableSound getNextSound();

  protected abstract boolean shouldSwitchSound();
}
