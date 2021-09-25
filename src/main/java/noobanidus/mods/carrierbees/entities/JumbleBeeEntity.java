package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.JumbleCombEntity;

public class JumbleBeeEntity extends AppleBeeEntity {
  public JumbleBeeEntity(EntityType<? extends JumbleBeeEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    if (ConfigManager.getHoneycombDamage(this) > 0) {
      this.goalSelector.addGoal(1, new JumbleBeeEntity.HoneycombProjectileAttackGoal(this));
    }
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.FLYING_SPEED, 0.4).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 128.0D);
  }

  static class HoneycombProjectileAttackGoal extends Goal {
    private final JumbleBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(JumbleBeeEntity bee) {
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
          JumbleCombEntity honeycomb = new JumbleCombEntity(this.parentEntity, d2, d3, d4, world);
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
