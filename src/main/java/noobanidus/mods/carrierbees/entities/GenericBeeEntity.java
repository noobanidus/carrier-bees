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
    public boolean shouldExecute() {
      return this.parentEntity.getAttackTarget() != null && this.parentEntity.isAngry();
    }

    @Override
    public void startExecuting() {
      this.attackTimer = 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
      return this.parentEntity.isAngry();
    }

    @Override
    public void resetTask() {
      this.parentEntity.setAttackTarget(null);
      this.parentEntity.setAggroed(false);
      this.parentEntity.getNavigator().clearPath();
    }

    @Override
    public void tick() {
      LivingEntity livingentity = this.parentEntity.getAttackTarget();
      if (livingentity == null) {
        return;
      }
      if (livingentity.getDistanceSq(this.parentEntity) < 400D && this.parentEntity.canEntityBeSeen(livingentity)) {
        World world = this.parentEntity.world;
        ++this.attackTimer;
        if (this.attackTimer == 20) {
          double d2 = livingentity.getPosX() - this.parentEntity.getPosX();
          double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + this.parentEntity.getPosYHeight(0.5D));
          double d4 = livingentity.getPosZ() - this.parentEntity.getPosZ();
          GenericCombEntity honeycomb = new GenericCombEntity(this.parentEntity, d2, d3, d4, world);
          honeycomb.setPosition(this.parentEntity.getPosX(), this.parentEntity.getPosYHeight(0.5D) + 0.2D, honeycomb.getPosZ());
          world.addEntity(honeycomb);
          this.attackTimer = -40;
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
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return ModSounds.GENERIC_BEE_HURT.get();
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return ModSounds.GENERIC_BEE_AMBIENT.get();
  }

  @Override
  public int getTalkInterval() {
    return 360;
  }
}
