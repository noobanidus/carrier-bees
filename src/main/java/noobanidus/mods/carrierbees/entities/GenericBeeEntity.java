package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.GenericCombEntity;
import noobanidus.mods.carrierbees.init.ModSounds;

public class GenericBeeEntity extends AppleBeeEntity {
  public GenericBeeEntity(EntityType<? extends GenericBeeEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    if (ConfigManager.getHoneycombDamage(this) > 0) {
      this.goalSelector.addGoal(1, new GenericBeeEntity.HoneycombProjectileAttackGoal(this));
    }
  }

  static class HoneycombProjectileAttackGoal extends Goal {
    private final GenericBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(GenericBeeEntity bee) {
      this.parentEntity = bee;
    }

    @Override
    public boolean canUse() {
      return this.parentEntity.getTarget() != null && this.parentEntity.isAngry();
    }

    @Override
    public void start() {
      this.attackTimer = -(this.parentEntity.random.nextInt(60)+30);
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
          GenericCombEntity honeycomb = new GenericCombEntity(this.parentEntity, d2, d3, d4, world);
          honeycomb.setPos(this.parentEntity.getX(), this.parentEntity.getY(0.5D) + 0.2D, honeycomb.getZ());
          world.addFreshEntity(honeycomb);
          this.attackTimer = -220;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }

  @Override
  protected SoundEvent getDeathSound() {
    return ModSounds.GENERIC_BEE_DIES.get();
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource pDamageSource) {
    return ModSounds.GENERIC_BEE_HURT.get();
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return ModSounds.GENERIC_BEE_AMBIENT.get();
  }

  @Override
  protected float getSoundVolume() {
    return 0.7f;
  }

  @Override
  public int getAmbientSoundInterval() {
    return 220;
  }
}
