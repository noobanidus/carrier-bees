package noobanidus.mods.carrierbees.mixins;

import net.minecraft.entity.passive.BeeEntity;
import noobanidus.mods.carrierbees.compat.BumbleZone;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.ai.CachedPathHolder;
import noobanidus.mods.carrierbees.entities.ai.SmartBee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.WanderGoal.class)
public class MixinBeePathFinding {
  @Unique
  private BeeEntity beeEntity;

  @Unique
  private CachedPathHolder cachedPathHolder;

  @Inject(method = "<init>(Lnet/minecraft/entity/passive/BeeEntity;)V", at = @At(value = "RETURN"), require = 1)
  private void init(BeeEntity beeEntity, CallbackInfo ci) {
    this.beeEntity = beeEntity;
  }

  /**
   * @author TelepathicGrunt
   * @reason Make bees not get stuck on ceiling anymore and lag people as a result. (Only applies in Bumblezone dimension)
   */
  @Inject(method = "Lnet/minecraft/entity/passive/BeeEntity$WanderGoal;startExecuting()V",
      at = @At(value = "HEAD"),
      cancellable = true,
      require = 1)
  private void newWander(CallbackInfo ci) {
    // Applies Bumblezone AI outside of the Bumblezone dimension
    if (ConfigManager.getImprovedAI() && !beeEntity.world.getDimensionKey().equals(BumbleZone.BZ_WORLD_KEY)) {
      cachedPathHolder = SmartBee.smartBee(beeEntity, cachedPathHolder);
      ci.cancel();
    }
  }
}
