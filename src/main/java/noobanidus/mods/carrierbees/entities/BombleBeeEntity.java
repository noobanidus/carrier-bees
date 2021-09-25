package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import noobanidus.mods.carrierbees.entities.projectiles.BombEntity;
import noobanidus.mods.carrierbees.world.BeeExplosion;

public class BombleBeeEntity extends AppleBeeEntity {
  private float explosionDamage;
  private float explosionSize;

  public BombleBeeEntity(EntityType<? extends BombleBeeEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(1, new BombProjectileAttackGoal(this));
  }

  @Override
  public void addAdditionalSaveData(CompoundNBT tag) {
    super.addAdditionalSaveData(tag);
    tag.putFloat("explosionSize", explosionSize);
    tag.putFloat("explosionDamage", explosionDamage);
  }

  @Override
  public void readAdditionalSaveData(CompoundNBT tag) {
    super.readAdditionalSaveData(tag);
    if (tag.contains("explosionSize", Constants.NBT.TAG_INT)) {
      explosionSize = (float) tag.getInt("explosionSize");
    } else if (tag.contains("explosionSize", Constants.NBT.TAG_FLOAT)) {
      explosionSize = tag.getFloat("explosionSize");
    } else {
      explosionSize = 1.5f;
    }
    if (tag.contains("explosionDamage", Constants.NBT.TAG_INT)) {
      explosionDamage = (float) tag.getInt("explosionDamage");
    } else if (tag.contains("explosionDamage", Constants.NBT.TAG_FLOAT)) {
      explosionDamage = tag.getFloat("explosionDamage");
    } else {
      explosionDamage = 3.5f;
    }
  }

  @Override
  public void die(DamageSource death) {
    super.die(death);
    BeeExplosion.createExplosion(this.level, this, this.getX(), this.getY(0.0625D), this.getY());
  }

  static class BombProjectileAttackGoal extends Goal {
    private final BombleBeeEntity parentEntity;
    public int attackTimer;

    public BombProjectileAttackGoal(BombleBeeEntity bee) {
      this.parentEntity = bee;
    }

    @Override
    public boolean canUse() {
      return this.parentEntity.getTarget() != null && this.parentEntity.isAngry();
    }

    @Override
    public boolean canContinueToUse() {
      return this.parentEntity.isAngry();
    }

    @Override
    public void start() {
      this.attackTimer = 0;
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
          BombEntity bomb = new BombEntity(this.parentEntity, d2, d3, d4, world);
          bomb.setPos(this.parentEntity.getX(), this.parentEntity.getY(0.5D) + 0.5D, bomb.getZ());
          world.addFreshEntity(bomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }
}
