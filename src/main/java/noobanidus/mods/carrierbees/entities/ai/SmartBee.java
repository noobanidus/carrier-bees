package noobanidus.mods.carrierbees.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;

public class SmartBee {
  public static CachedPathHolder smartBee(MobEntity beeEntity, CachedPathHolder cachedPathHolder) {
    if (cachedPathHolder == null || cachedPathHolder.pathTimer > 50 || cachedPathHolder.cachedPath == null ||
        (beeEntity.getMotion().length() <= 0.05d && cachedPathHolder.pathTimer > 5) ||
        beeEntity.getPosition().manhattanDistance(cachedPathHolder.cachedPath.getTarget()) <= 4) {
      BlockPos.Mutable mutable = new BlockPos.Mutable();
      World world = beeEntity.world;

      for (int attempt = 0; attempt < 11 || beeEntity.getPosition().manhattanDistance(mutable) <= 5; attempt++) {
        // pick a random place to fly to
        mutable.setPos(beeEntity.getPosition()).move(
            world.rand.nextInt(21) - 10,
            world.rand.nextInt(21) - 10,
            world.rand.nextInt(21) - 10
        );

        if (world.getBlockState(mutable).isAir()) {
          break; // Valid spot to go towards.
        }
      }

      Path newPath = beeEntity.getNavigator().getPathToPos(mutable, 1);
      beeEntity.getNavigator().setPath(newPath, 1);

      if (cachedPathHolder == null) {
        cachedPathHolder = new CachedPathHolder();
      }
      cachedPathHolder.cachedPath = newPath;
      cachedPathHolder.pathTimer = 0;
    } else {
      beeEntity.getNavigator().setPath(cachedPathHolder.cachedPath, 1);
      cachedPathHolder.pathTimer += 1;
    }

    return cachedPathHolder;
  }


}
