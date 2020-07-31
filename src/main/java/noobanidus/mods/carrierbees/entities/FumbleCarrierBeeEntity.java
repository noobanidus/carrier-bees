package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.FumbleCombEntity;
import noobanidus.mods.carrierbees.init.ModEntities;

public class FumbleCarrierBeeEntity extends CarrierBeeEntity {
  public FumbleCarrierBeeEntity(EntityType<? extends FumbleCarrierBeeEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FumbleCarrierBeeEntity.StingGoal(this, 1.4D, true));
    if (ConfigManager.getHoneycombDamage() > 0) {
      this.goalSelector.addGoal(1, new FumbleCarrierBeeEntity.HoneycombProjectileAttackGoal(this));
    }
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(8, new FumbleCarrierBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
    this.targetSelector.addGoal(1, (new FumbleCarrierBeeEntity.AngerGoal(this)).setCallsForHelp());
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (pos) -> Math.abs(pos.posY - this.posY) <= 4.0D));
  }

  @Override
  public FumbleCarrierBeeEntity createChild(AgeableEntity entity) {
    return ModEntities.FUMBLE_BEE.get().create(entity.world);
  }

  static class HoneycombProjectileAttackGoal extends Goal {
    private final FumbleCarrierBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(FumbleCarrierBeeEntity bee) {
      this.parentEntity = bee;
    }

    @Override
    public boolean shouldExecute() {
      return this.parentEntity.getAttackTarget() != null;
    }

    @Override
    public void startExecuting() {
      this.attackTimer = 0;
    }

    @Override
    public void resetTask() {
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
          double d2 = livingentity.posX - this.parentEntity.posX;
          double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + this.parentEntity.getPosYHeight(0.5D));
          double d4 = livingentity.posZ - this.parentEntity.posZ;
          FumbleCombEntity honeycomb = new FumbleCombEntity(this.parentEntity, d2, d3, d4, world);
          honeycomb.setPosition(this.parentEntity.posX, this.parentEntity.getPosYHeight(0.5D) + 0.2D, honeycomb.posZ);
          world.addEntity(honeycomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }
}
