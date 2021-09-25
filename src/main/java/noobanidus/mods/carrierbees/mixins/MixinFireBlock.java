package noobanidus.mods.carrierbees.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.DrabbleBeeEntity;
import noobanidus.mods.carrierbees.init.ModEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(FireBlock.class)
public class MixinFireBlock {
  @Inject(method = "Lnet/minecraft/block/FireBlock;tick(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V", at = @At(value = "HEAD"), require = 1)
  public void fireTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand, CallbackInfo info) {
    if (worldIn.isClientSide) {
      return;
    }
    if (!worldIn.dimension().equals(World.OVERWORLD)) {
      return;
    }
    if (worldIn.getBlockState(pos.below()).isFireSource(worldIn, pos.below(), Direction.UP)) {
      return;
    }
    if (ConfigManager.getDrabblebeeChance() != -1 && worldIn.random.nextDouble() < ConfigManager.getDrabblebeeChance()) {
      AxisAlignedBB box = DrabbleBeeEntity.FIRE_SEARCH.move(pos);
      List<DrabbleBeeEntity> entities = worldIn.getEntitiesOfClass(DrabbleBeeEntity.class, box);
      if (entities.size() <= 3) {
        for (BlockPos spot : BlockPos.betweenClosed((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ)) {
          if (spot.distSqr(pos) < 1.5) {
            if (worldIn.isEmptyBlock(spot)) {
              Entity entity = ModEntities.DRABBLE_BEE.get().spawn(worldIn, null, null, spot, SpawnReason.TRIGGERED, true, true);
              if (entity instanceof DrabbleBeeEntity) {
                ((DrabbleBeeEntity) entity).setTTL(60 * 20 + (rand.nextInt(30) * 20));
                break;
              }
            }
          }
        }
      }
    }
  }
}
