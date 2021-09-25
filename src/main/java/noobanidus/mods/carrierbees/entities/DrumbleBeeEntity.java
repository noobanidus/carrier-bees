package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.DrumbleCombEntity;

public class DrumbleBeeEntity extends AppleBeeEntity {
  public DrumbleBeeEntity(EntityType<? extends DrumbleBeeEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    if (ConfigManager.getHoneycombDamage(this) > 0) {
      this.goalSelector.addGoal(1, new DrumbleBeeEntity.HoneycombProjectileAttackGoal(this));
    }
  }

  static class HoneycombProjectileAttackGoal extends Goal {
    private final DrumbleBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(DrumbleBeeEntity bee) {
      this.parentEntity = bee;
    }

    @Override
    public boolean canUse() {
      return this.parentEntity.getTarget() != null && this.parentEntity.isAngry();
    }

    @Override
    public void start() {
      this.attackTimer = 0;
    }

    @Override
    public boolean canContinueToUse() {
      return this.parentEntity.isAngry();
    }

    @Override
    public void stop() {
      this.parentEntity.setTarget(null);
      this.parentEntity.setAggressive(false);
      this.parentEntity.getNavigation().stop();
    }

    @Override
    public void tick() {
      LivingEntity livingentity = this.parentEntity.getTarget();
      if (livingentity == null) {
        return;
      }
      if (livingentity.distanceToSqr(this.parentEntity) < 400D && this.parentEntity.canSee(livingentity)) {
        World world = this.parentEntity.level;
        ++this.attackTimer;
        if (this.attackTimer == 20) {
          double d2 = livingentity.getX() - this.parentEntity.getX();
          double d3 = livingentity.getY(0.5D) - (0.5D + this.parentEntity.getY(0.5D));
          double d4 = livingentity.getZ() - this.parentEntity.getZ();
          DrumbleCombEntity honeycomb = new DrumbleCombEntity(this.parentEntity, d2, d3, d4, world);
          honeycomb.setPos(this.parentEntity.getX(), this.parentEntity.getY(0.5D) + 0.2D, honeycomb.getZ());
          world.addFreshEntity(honeycomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }
}
