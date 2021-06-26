package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.BoogerCombEntity;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModSounds;

import javax.annotation.Nullable;

public class BoogerBeeEntity extends AppleBeeEntity {
  public BoogerBeeEntity(EntityType<? extends BoogerBeeEntity> type, World world) {
    super(type, world);
    shouldSting = false;
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    if (ConfigManager.getHoneycombDamage(this) > 0) {
      this.goalSelector.addGoal(1, new BoogerBeeEntity.HoneycombProjectileAttackGoal(this));
    }
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 4.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.4).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
  }

  static class HoneycombProjectileAttackGoal extends Goal {
    private final BoogerBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(BoogerBeeEntity bee) {
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
          BoogerCombEntity honeycomb = new BoogerCombEntity(this.parentEntity, d2, d3, d4, world);
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
    return ModSounds.BOOGER_BEE_DIES.get();
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return ModSounds.BOOGER_BEE_HURT.get();
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return ModSounds.BOOGER_BEE_AMBIENT.get();
  }

  @Override
  public int getTalkInterval() {
    return 40;
  }
}
