package noobanidus.mods.carrierbees.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import noobanidus.mods.carrierbees.client.sound.CarrierBeeAngrySound;
import noobanidus.mods.carrierbees.client.sound.CarrierBeeFlightSound;
import noobanidus.mods.carrierbees.client.sound.CarrierBeeSound;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;
import noobanidus.mods.carrierbees.entities.DrabbleBeeEntity;
import noobanidus.mods.carrierbees.entities.IAppleBee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {
  @Inject(method = "Lnet/minecraft/client/world/ClientWorld;addEntity(ILnet/minecraft/entity/Entity;)V", at = @At(value = "RETURN"))
  private void beeHandleSpawnMob(int entityIdIn, Entity entityToSpawn, CallbackInfo info) {
    if (entityToSpawn instanceof IAppleBee) {
      CarrierBeeSound<?> beeSound = null;
      if (entityToSpawn instanceof BeehemothEntity) {
        if (((IAppleBee) entityToSpawn).safeIsAngry()) {
          beeSound = new CarrierBeeAngrySound<>((BeehemothEntity) entityToSpawn);
        } else {
          beeSound = new CarrierBeeFlightSound<>((BeehemothEntity) entityToSpawn);
        }
      } else if (entityToSpawn instanceof AppleBeeEntity) {
        if (((IAppleBee) entityToSpawn).safeIsAngry()) {
          beeSound = new CarrierBeeAngrySound<>((AppleBeeEntity) entityToSpawn);
        } else {
          beeSound = new CarrierBeeFlightSound<>((AppleBeeEntity) entityToSpawn);
        }
      } else if (entityToSpawn instanceof DrabbleBeeEntity) {
        beeSound = new CarrierBeeFlightSound<>((DrabbleBeeEntity) entityToSpawn);
      }
      if (beeSound != null) {
        Minecraft.getInstance().getSoundHandler().playOnNextTick(beeSound);
      }
    }
  }
}
