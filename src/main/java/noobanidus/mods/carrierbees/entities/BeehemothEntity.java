package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@SuppressWarnings("ALL")
public class BeehemothEntity extends AppleBeeEntity implements IRideable {
  public static final DataParameter<Boolean> isSaddled = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Integer> BOOST_TIME = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.VARINT);
  private final BoostHelper field_234214_bx_ = new BoostHelper(this.dataManager, BOOST_TIME, isSaddled);

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
  public boolean canBeSteered() {
    return getControllingPassenger() instanceof PlayerEntity;
  }

  @Override
  public Vector3d func_230268_c_(LivingEntity livingEntity) {
    Direction direction = this.getAdjustedHorizontalFacing();
    if (direction.getAxis() == Direction.Axis.Y) {
      return super.func_230268_c_(livingEntity);
    } else {
      int[][] aint = TransportationHelper.func_234632_a_(direction);
      BlockPos blockpos = this.getPosition();
      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

      for (Pose pose : livingEntity.getAvailablePoses()) {
        AxisAlignedBB axisalignedbb = livingEntity.getPoseAABB(pose);

        for (int[] aint1 : aint) {
          blockpos$mutable.setPos(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
          double d0 = this.world.func_242403_h(blockpos$mutable);
          if (TransportationHelper.func_234630_a_(d0)) {
            Vector3d vector3d = Vector3d.copyCenteredWithVerticalOffset(blockpos$mutable, d0);
            if (TransportationHelper.func_234631_a_(this.world, livingEntity, axisalignedbb.offset(vector3d))) {
              livingEntity.setPose(pose);
              return vector3d;
            }
          }
        }
      }

      return super.func_230268_c_(livingEntity);
    }
  }

  @Override
  protected void registerData() {
    super.registerData();
    getDataManager().register(isSaddled, true);
    this.dataManager.register(BOOST_TIME, 0);
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 42.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
  }

  public float getMountedSpeed() {
    return 0.4f; // (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225F;
  }

  public void travelTowards(Vector3d travelVec) {
    super.travel(travelVec);
  }

  public boolean boost() {
    return this.field_234214_bx_.boost(this.getRNG());
  }

  public void travel(Vector3d travelVector) {
    this.ride(this, this.field_234214_bx_, travelVector);
  }

  @Override
  public void tick() {
    super.tick();
    this.updateBodyPitch();
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(8, new AppleBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
  }

  @Override
  protected void setRotation(float yaw, float pitch) {
    this.rotationYaw = yaw % 360f;
    this.rotationPitch = pitch % 360f;
    // super.setRotation(yaw, pitch); ???
  }

  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    this.field_234214_bx_.setSaddledToNBT(compound);
  }

  /**
   * (abstract) Protected helper method to read subclass entity data from NBT.
   */
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.field_234214_bx_.setSaddledFromNBT(compound);
  }

  @Override
  protected boolean canBeRidden(Entity entityIn) {
    return !this.isBeingRidden();
  }

  public void clearAI() {
    isJumping = false;
    navigator.clearPath();
    setAttackTarget(null);
    setRevengeTarget(null);

    setMoveForward(0);
    setMoveVertical(0);
  }

  @Override
  public void updateRidden() {
    super.updateRidden();

/*    Entity entity = getRidingEntity();

    if (entity == null || !entity.isAlive()) {
      stopRiding();
      return;
    }

    setMotion(Vector3d.ZERO);
    clearAI();

    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      if ((player.isSneaking() && !player.abilities.isFlying)) {
        stopRiding();
        return;
      }

      prevRotationPitch = rotationPitch = player.rotationPitch / 2;
      rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
      setRotation(player.rotationYawHead, rotationPitch);

      Vector3d vec3d = new Vector3d(0, getMountedYOffset(), 0);

      if (player.isElytraFlying()) {
        setFlown(true);
        vec3d = vec3d.scale(1.5);
      }

      Vector3d pos = new Vector3d(vec3d.x, 0, vec3d.z).rotateYaw((float) (-player.renderYawOffset * Math.PI / 180f)).add(player.getPosX(), player.getPosY() + vec3d.y, player.getPosZ());
      setPosition(pos.x, pos.y, pos.z);
    }*/
  }

  @Override
  public void updatePassenger(Entity player) {
    super.updatePassenger(player);
/*    Vector3d vec3d = new Vector3d(0, getMountedYOffset(), 0);

    Vector3d pos = new Vector3d(vec3d.x, 0, vec3d.z).rotateYaw((float) (-renderYawOffset * Math.PI / 180f)).add(player.getPosX(), player.getPosY() + vec3d.y, player.getPosZ());
    setPosition(pos.x, pos.y, pos.z);*/
  }

  public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
    if (/*this.field_234214_bx_.getSaddled() && */!this.isBeingRidden() && !p_230254_1_.isSecondaryUseActive()) {
      if (!this.world.isRemote) {
        p_230254_1_.startRiding(this);
      }

      return ActionResultType.func_233537_a_(this.world.isRemote);
    } else {
      return super.func_230254_b_(p_230254_1_, p_230254_2_);
    }
  }

  @Override
  public boolean canPassengerSteer() {
    return this.getControllingPassenger() != null;
  }

  @Nullable
  @Override
  public Entity getControllingPassenger() {
    return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
  }

/*  public void travel(Vector3d travelVector) {
    float speed = 0.5f; //getTravelSpeed();
    if (this.isAlive()) {
      if (canPassengerSteer()) {
        LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
        double y = travelVector.y;
        double x = travelVector.x;
        double z = livingentity.moveForward;

        rotationYawHead = livingentity.rotationYawHead;
        rotationPitch = livingentity.rotationPitch * 0.5f;

        if (livingentity.moveForward != 0) {
          y = livingentity.getLookVec().y * speed * 50;
        }
        if (livingentity instanceof ServerPlayerEntity) {
          ((ServerPlayerEntity) livingentity).connection.vehicleFloating = false;
        }
        moveRelative(speed, travelVector);
        move(MoverType.SELF, getMotion());
        //setMotion(getMotion().scale(0.88f));

        Vector3d motion = getMotion();
        if (motion.length() < 0.04f) {
          setMotion(motion.add(0, Math.cos(ticksExisted * 0.1f) * 0.02f, 0));
        }

        float limbSpeed = 0.4f;
        float amount = 1f;
        if (getPosY() - prevPosY < -0.1f) {
          amount = 0;
          limbSpeed = 0.2f;
        }

        prevLimbSwingAmount = limbSwingAmount;
        limbSwingAmount += (amount - limbSwingAmount) * limbSpeed;
        limbSwing += limbSwingAmount;

        return;
      }
    }
    super.travel(travelVector);
  }*/

  @Override
  protected void addPassenger(Entity p_184200_1_) {
    super.addPassenger(p_184200_1_);
/*    if (getControllingPassenger() == p_184200_1_) {
      clearAI();
      if (getLeashed()) {
        clearLeashed(true, true);
      }
    }*/
  }

  /**
   * Returns the Y offset from the entity's position for any entity riding this one.
   */
  public double getMountedYOffset() {
    return 2.05D;
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
