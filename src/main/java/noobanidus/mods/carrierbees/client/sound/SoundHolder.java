package noobanidus.mods.carrierbees.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import noobanidus.mods.carrierbees.entities.IAppleBee;

public class SoundHolder<T extends AnimalEntity & IAppleBee> {
  private CarrierBeeSound<T> sound;

  public void init(T entity, World world) {
    if (world.isRemote) {
      if (sound == null) {
        if (entity.safeIsAngry()) {
          sound = new CarrierBeeAngrySound<>(entity);
        } else {
          sound = new CarrierBeeFlightSound<>(entity);
        }
        Minecraft.getInstance().getSoundHandler().playOnNextTick(sound);
      }
    }
  }
}
