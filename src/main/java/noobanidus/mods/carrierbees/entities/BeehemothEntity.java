package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;

public class BeehemothEntity extends AnimalEntity implements IFlyingAnimal {
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
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 42.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(9, new SwimGoal(this));
  }

  public ActionResultType func_230254_b_(PlayerEntity player, Hand p_230254_2_) {
    if (!this.isBeingRidden() && !player.isSecondaryUseActive()) {
      if (!this.world.isRemote) {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        player.startRiding(this);
      }

      return ActionResultType.func_233537_a_(this.world.isRemote);
    } else {
      return super.func_230254_b_(player, p_230254_2_);
    }
  }

  @Nullable
  @Override
  public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
    return null;
  }

  @Override
  public boolean onLivingFall(float p_225503_1_, float p_225503_2_) {
    return false;
  }

  public void travel(Vector3d travelVector) {
    if (!this.isBeingRidden()) {
      super.travel(travelVector);
    }
    if (this.isAlive()) {
      if (this.canBeSteered()) {
        setNoGravity(true);
        LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
        this.rotationYaw = livingentity.rotationYaw;
        this.prevRotationYaw = this.rotationYaw;
        this.rotationPitch = livingentity.rotationPitch * 0.5F;
        this.setRotation(this.rotationYaw, this.rotationPitch);
        this.renderYawOffset = this.rotationYaw;
        this.rotationYawHead = this.renderYawOffset;
        float f = livingentity.moveStrafing * 0.5F;
        float f1 = livingentity.moveForward;
        if (f1 <= 0.0F) {
          f1 *= 0.25F;
        }

        if (this.isJumping) {
          Vector3d vector3d = this.getMotion();
          this.setMotion(vector3d.x, jumpMovementFactor, vector3d.z);
          this.isAirBorne = true;
          net.minecraftforge.common.ForgeHooks.onLivingJump(this);
          if (f1 > 0.0F) {
            float f2 = MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F));
            float f3 = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F));
            this.setMotion(this.getMotion().add((double) (-0.4F * f2 * this.jumpMovementFactor), 0.0D, (double) (0.4F * f3 * this.jumpMovementFactor)));
          }
        }

        this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
        if (this.canPassengerSteer()) {
          this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
          super.travel(new Vector3d((double) f, travelVector.y, (double) f1));
        } else if (livingentity instanceof PlayerEntity) {
          this.setMotion(Vector3d.ZERO);
        }

        this.func_233629_a_(this, true);
      } else {
        this.jumpMovementFactor = 0.02F;
        super.travel(travelVector);
      }
    }
  }

  protected double getFlyingYModifier(final double motionY) {
    if (motionY == 0) {
      return 0;
    }
    double yModifier = 0.07;
    if (this.onGround) {
      yModifier += 0.4;
    } else {
      yModifier += 0.07;
    }
    return yModifier;
  }

  protected float getRelevantMoveFactor(final float p_213335_1_) {
    return this.onGround ? (this.getAIMoveSpeed() * (0.21600002f / (p_213335_1_ * p_213335_1_ * p_213335_1_))) : this.jumpMovementFactor;
  }

  private void travelSpecial(final Vector3d travelInputVec) {
    if (isBeingRidden() && (this.isServerWorld() || this.canPassengerSteer())) {
      double gravForce;
      final ModifiableAttributeInstance gravity = this.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
      final boolean flag = this.getMotion().y <= 0.0;
      if (flag && this.isPotionActive(Effects.SLOW_FALLING)) {
        this.fallDistance = 0.0F;
      }
      gravForce = gravity.getValue();
      if (!this.isInWater()) {
        final BlockPos blockpos = this.getPositionUnderneath();
        final float slipFactor = this.world.getBlockState(blockpos).getSlipperiness(this.world, blockpos, this);
        final float motionSlowFactor = this.onGround ? (slipFactor * 0.91f) : 0.91f;
        this.moveRelative(this.getRelevantMoveFactor(slipFactor), travelInputVec);
        this.setMotion(this.getMotion());
        this.move(MoverType.SELF, this.getMotion());
        Vector3d vec3d7 = this.getMotion();
        if ((this.collidedHorizontally || this.isJumping) && this.isOnLadder()) {
          vec3d7 = new Vector3d(vec3d7.x, 0.2, vec3d7.z);
        }
        double yMotion = vec3d7.y;
        if (this.isPotionActive(Effects.LEVITATION)) {
          yMotion += (0.05 * (this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - vec3d7.y) * 0.2;
          this.fallDistance = 0.0f;
        } else if (this.world.isRemote && !this.world.isBlockLoaded(blockpos)) {
          if (this.getPosY() > 0.0) {
            yMotion = -0.1;
          } else {
            yMotion = 0.0;
          }
        } else if (!this.hasNoGravity()) {
          yMotion -= gravForce;
        }
        yMotion += this.getFlyingYModifier(yMotion);
        this.setMotion(vec3d7.x * motionSlowFactor, yMotion * 0.9800000190734863, vec3d7.z * motionSlowFactor);
      }
    } else {
      super.travel(travelInputVec);
    }
  }

  public boolean canBeSteered() {
    return this.getControllingPassenger() instanceof LivingEntity;
  }

  public void updatePassenger(Entity passenger) {
    super.updatePassenger(passenger);
    if (passenger instanceof MobEntity) {
      MobEntity mobentity = (MobEntity) passenger;
      this.renderYawOffset = mobentity.renderYawOffset;
    }
  }

  @Nullable
  public Entity getControllingPassenger() {
    return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
  }

  @Nullable
  private Vector3d func_234236_a_(Vector3d p_234236_1_, LivingEntity p_234236_2_) {
    double d0 = this.getPosX() + p_234236_1_.x;
    double d1 = this.getBoundingBox().minY;
    double d2 = this.getPosZ() + p_234236_1_.z;
    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

    for (Pose pose : p_234236_2_.getAvailablePoses()) {
      blockpos$mutable.setPos(d0, d1, d2);
      double d3 = this.getBoundingBox().maxY + 0.75D;

      while (true) {
        double d4 = this.world.func_242403_h(blockpos$mutable);
        if ((double) blockpos$mutable.getY() + d4 > d3) {
          break;
        }

        if (TransportationHelper.func_234630_a_(d4)) {
          AxisAlignedBB axisalignedbb = p_234236_2_.getPoseAABB(pose);
          Vector3d vector3d = new Vector3d(d0, (double) blockpos$mutable.getY() + d4, d2);
          if (TransportationHelper.func_234631_a_(this.world, p_234236_2_, axisalignedbb.offset(vector3d))) {
            p_234236_2_.setPose(pose);
            return vector3d;
          }
        }

        blockpos$mutable.move(Direction.UP);
        if (!((double) blockpos$mutable.getY() < d3)) {
          break;
        }
      }
    }

    return null;
  }

  public Vector3d func_230268_c_(LivingEntity livingEntity) {
    Vector3d vector3d = func_233559_a_((double) this.getWidth(), (double) livingEntity.getWidth(), this.rotationYaw + (livingEntity.getPrimaryHand() == HandSide.RIGHT ? 90.0F : -90.0F));
    Vector3d vector3d1 = this.func_234236_a_(vector3d, livingEntity);
    if (vector3d1 != null) {
      return vector3d1;
    } else {
      Vector3d vector3d2 = func_233559_a_((double) this.getWidth(), (double) livingEntity.getWidth(), this.rotationYaw + (livingEntity.getPrimaryHand() == HandSide.LEFT ? 90.0F : -90.0F));
      Vector3d vector3d3 = this.func_234236_a_(vector3d2, livingEntity);
      return vector3d3 != null ? vector3d3 : this.getPositionVec();
    }
  }
}
