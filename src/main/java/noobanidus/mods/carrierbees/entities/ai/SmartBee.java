package noobanidus.mods.carrierbees.entities.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;

public class SmartBee {
  public static CachedPathHolder smartBee(MobEntity beeEntity, CachedPathHolder cachedPathHolder) {
    if (cachedPathHolder == null || cachedPathHolder.pathTimer > 50 || cachedPathHolder.cachedPath == null ||
        (beeEntity.getDeltaMovement().length() <= 0.05d && cachedPathHolder.pathTimer > 5) ||
        beeEntity.blockPosition().distManhattan(cachedPathHolder.cachedPath.getTarget()) <= 4) {
      BlockPos.Mutable mutable = new BlockPos.Mutable();
      World world = beeEntity.level;

      boolean targeting = beeEntity.getTarget() != null;
      boolean noSnipe = beeEntity instanceof AppleBeeEntity && ((AppleBeeEntity) beeEntity).noSnipe();
      int maxDistance = noSnipe ? 3 : 5;
      int rand1 = noSnipe ? 11 : 21;
      int rand2 = noSnipe ? 5 : 10;

      double targetDistance = targeting ? beeEntity.distanceToSqr(beeEntity.getTarget()) + maxDistance * maxDistance : 0;

      mutable.set(beeEntity.blockPosition());

      if (!noSnipe || world.random.nextInt(15) == 0) {
        for (int attempt = 0; attempt < 11 || beeEntity.blockPosition().distManhattan(mutable) <= maxDistance || (targeting && noSnipe && beeEntity.distanceToSqr(beeEntity.getTarget()) <= targetDistance); attempt++) {
          // pick a random place to fly to
          mutable.set(beeEntity.blockPosition()).move(
              world.random.nextInt(rand1) - rand2,
              world.random.nextInt(rand1) - rand2,
              world.random.nextInt(rand1) - rand2
          );

          int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable.getX(), mutable.getZ());
          if (mutable.getY() <= 2 || height == 0) {
            mutable.setY(Math.max((int) beeEntity.getX(), 2));
            continue;
          }
          if (mutable.getY() >= height + ConfigManager.getAIHeight()) {
            mutable.setY(height + ConfigManager.getAIHeight());
            continue;
          }

          if (world.getBlockState(mutable).isAir()) {
            break; // Valid spot to go towards.
          }
        }
      }

      Path newPath = beeEntity.getNavigation().createPath(mutable, 1);
      beeEntity.getNavigation().moveTo(newPath, 1);

      if (cachedPathHolder == null) {
        cachedPathHolder = new CachedPathHolder();
      }
      cachedPathHolder.cachedPath = newPath;
      cachedPathHolder.pathTimer = 0;
    } else {
      beeEntity.getNavigation().moveTo(cachedPathHolder.cachedPath, 1);
      cachedPathHolder.pathTimer += 1;
    }

    return cachedPathHolder;
  }
}
