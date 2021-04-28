package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BeehemothEntity extends AppleBeeEntity {
  public BeehemothEntity(EntityType<? extends BeehemothEntity> type, World world) {
    super(type, world);
    this.moveController = new FlyerMoveController(this);
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (source == DamageSource.OUT_OF_WORLD || source.getTrueSource() instanceof PlayerEntity) {
      return super.attackEntityFrom(source, amount);
    }

    return false;
  }

  @Override
  public void tick() {
    super.tick();
    updateBodyPitch();
    Entity riding = getRidingEntity();
    if (riding instanceof PlayerEntity && !world.isRemote) {
      PlayerEntity player = (PlayerEntity) riding;
      player.abilities.allowFlying = true;
      player.abilities.isFlying = true;
      prevPosX = getPosX();
      prevPosY = getPosY();
      prevPosZ = getPosZ();
      setLocationAndAngles(player.getPosX(), player.getPosY() - 2d, player.getPosZ(), player.rotationYaw, player.rotationPitch);
    }
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 42.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
  }

  @Override
  protected void registerGoals() {
/*    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));*/
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
/*    this.goalSelector.addGoal(8, new AppleBeeEntity.WanderGoal());*/
/*    this.goalSelector.addGoal(9, new SwimGoal(this));*/
  }

  public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
    if (!p_230254_1_.isBeingRidden() && !p_230254_1_.isSecondaryUseActive()) {
      if (!this.world.isRemote) {
        this.startRiding(p_230254_1_, true);
      }

      return ActionResultType.func_233537_a_(this.world.isRemote);
    } else {
      return super.func_230254_b_(p_230254_1_, p_230254_2_);
    }
  }

  private float currentPitch;
  private float lastPitch;

  @Override
  @OnlyIn(Dist.CLIENT)
  public float getBodyPitch(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastPitch, this.currentPitch);
  }

  private void updateBodyPitch() {
    this.lastPitch = this.currentPitch;
    this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
  }
}
