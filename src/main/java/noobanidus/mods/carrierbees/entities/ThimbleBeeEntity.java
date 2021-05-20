package noobanidus.mods.carrierbees.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.ThimbleCombEntity;
import noobanidus.mods.carrierbees.init.ModEntities;

import javax.annotation.Nullable;

public class ThimbleBeeEntity extends AppleBeeEntity {
  public ThimbleBeeEntity(EntityType<? extends ThimbleBeeEntity> type, World world) {
    super(type, world);
    shouldSting = false;
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    if (ConfigManager.getHoneycombDamage(this) > 0) {
      this.goalSelector.addGoal(1, new ThimbleBeeEntity.HoneycombProjectileAttackGoal(this));
    }
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 4.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.4).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
  }

  @Override
  public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
    if (ConfigManager.getAdditionalThimblebees()) {
      if (nbt == null || !nbt.contains("summoned")) {
        if (nbt == null) {
          nbt = new CompoundNBT();
        }
        nbt.putBoolean("summoned", true);
        if (world instanceof ServerWorld) {
          int count = rand.nextInt(1) + 1;
          for (int i = 0; i < count; i++) {
            ThimbleBeeEntity entity = ModEntities.THIMBLE_BEE.get().create((ServerWorld) world, nbt, null, null, getPosition(), reason, true, true);
            if (entity != null) {
              world.addEntity(entity);
            }
          }
        }
      }
    }
    return super.onInitialSpawn(world, difficulty, reason, data, nbt);
  }

  static class HoneycombProjectileAttackGoal extends Goal {
    private final ThimbleBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(ThimbleBeeEntity bee) {
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
          ThimbleCombEntity honeycomb = new ThimbleCombEntity(this.parentEntity, d2, d3, d4, world);
          honeycomb.setPosition(this.parentEntity.getPosX(), this.parentEntity.getPosYHeight(0.5D) + 0.2D, honeycomb.getPosZ());
          world.addEntity(honeycomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }
}
