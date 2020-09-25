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
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    tag.putFloat("explosionSize", explosionSize);
    tag.putFloat("explosionDamage", explosionDamage);
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
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
  public void onDeath(DamageSource death) {
    super.onDeath(death);
    BeeExplosion.createExplosion(this.world, this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosY());
  }

  static class BombProjectileAttackGoal extends Goal {
    private final BombleBeeEntity parentEntity;
    public int attackTimer;

    public BombProjectileAttackGoal(BombleBeeEntity bee) {
      this.parentEntity = bee;
    }

    @Override
    public boolean shouldExecute() {
      return this.parentEntity.getAttackTarget() != null && this.parentEntity.isAngry();
    }

    @Override
    public boolean shouldContinueExecuting() {
      return this.parentEntity.isAngry();
    }

    @Override
    public void startExecuting() {
      this.attackTimer = 0;
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
          BombEntity bomb = new BombEntity(this.parentEntity, d2, d3, d4, world);
          bomb.setPosition(this.parentEntity.getPosX(), this.parentEntity.getPosYHeight(0.5D) + 0.5D, bomb.getPosZ());
          world.addEntity(bomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }
}
