package noobanidus.mods.carrierbees.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.util.MathHelpers;

import javax.annotation.Nullable;

public class BeehemothEntity extends AppleBeeEntity {
  public static final DataParameter<Boolean> isSaddled = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.BOOLEAN);

  public static final double SPEED = 0.4d;
  public static final float MAX_ANGLE = 90f;
  public static final float MIN_ANGLE = -90f;
  private static final float MAX_COS_AMPLITUDE = 0.2f;

  private LivingEntity lastMounted = null;

  private boolean setLast = false;
  private float lastRotationPitch = -1f;
  private float lastRotationYaw = -1f;

  private double newPosX;
  private double newPosY;
  private double newPosZ;
  private double newRotationYaw;
  private double newRotationPitch;

  private int hoverTickOffset;

  @OnlyIn(Dist.CLIENT)
  private double oldHoverOffset;

  @Override
  public double getMountedYOffset() {
    return 2d;
  }

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

  @Nullable
  @Override
  public Entity getControllingPassenger() {
    return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
  }

  @Override
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    if (!isBeingRidden()) {
      super.setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, teleport);
    }

    posRotationIncrements += 6;
    this.newPosX = x;
    this.newPosY = y;
    this.newPosZ = z;
    this.newRotationYaw = (double) yaw;
    this.newRotationPitch = (double) pitch;
    this.newPosRotationIncrements = posRotationIncrements;
  }

  @Override
  protected void registerData() {
    super.registerData();
    getDataManager().register(isSaddled, true);
  }

  public void startAllowFlying(LivingEntity entity) {
    if (entity instanceof PlayerEntity) {
      ((PlayerEntity) entity).abilities.allowFlying = true;
    }
  }

  public void stopAllowFlying(LivingEntity entity) {
    if (entity instanceof PlayerEntity) {
      ((PlayerEntity) entity).abilities.allowFlying = false;
    }
  }

  @Override
  protected void addPassenger(Entity passenger) {
    super.addPassenger(passenger);
    if (!world.isRemote && passenger instanceof LivingEntity) {
      startAllowFlying((LivingEntity) passenger);
    }
  }

  @Override
  public void tick() {
    super.tick();

    Entity rider = getControllingPassenger();
    if (!world.isRemote() && !isBeingRidden() && lastMounted != null) {
      onDismount();
    } else if (rider instanceof LivingEntity) {
      lastMounted = (LivingEntity) rider;

      prevPosX = getPosX();
      prevPosY = getPosY();
      prevPosZ = getPosZ();
      prevRotationPitch = rotationPitch;
      prevRotationYaw = rotationYaw;

      if (!world.isRemote() || Minecraft.getInstance().player == lastMounted) {
        updateMountedServer();
      } else {
        updateMountedClient();
      }
    }
  }

  private void onDismount() {
    if (lastMounted instanceof PlayerEntity) {
      stopAllowFlying(lastMounted);
    }

    lastMounted = null;
  }

  @Override
  protected boolean canFitPassenger(Entity passenger) {
    return this.getPassengers().size() == 0;
  }

  @Override
  public void removePassengers() {
    if (!getEntityWorld().isRemote()) {
      for (Entity p : this.getPassengers()) {
        if (p instanceof LivingEntity) {
          stopAllowFlying((LivingEntity) p);
        }
      }
    }
    super.removePassengers();
  }

  @Override
  protected void removePassenger(Entity passenger) {
    if (!getEntityWorld().isRemote() && getControllingPassenger() == passenger) {
      onDismount();
    }
    super.removePassenger(passenger);
  }

  protected void updateMountedClient() {
    if (newPosRotationIncrements > 0) {
      double x = getPosX() + (newPosX - getPosX()) / newPosRotationIncrements;
      double y = getPosY() + (newPosY - getPosY()) / newPosRotationIncrements;
      double z = getPosZ() + (newPosZ - getPosZ()) / newPosRotationIncrements;

      float yaw = MathHelpers.normalizeAngle_180((float) (newRotationYaw - rotationYaw));
      rotationYaw += yaw / newPosRotationIncrements;
      rotationPitch += (newRotationPitch - rotationPitch) / newPosRotationIncrements;

      newPosRotationIncrements--;

      setPosition(x, y, z);
      setRotation(rotationYaw, rotationPitch);
    }

    move(MoverType.SELF, new Vector3d(0, getHoverOffset(), 0));
  }

  protected void updateMountedServer() {
    if (!setLast) {
      setLast = true;
      lastRotationYaw = rotationYaw;
      lastRotationPitch = rotationPitch;
    }

    if (lastMounted instanceof PlayerEntity) {
      if (lastMounted instanceof ServerPlayerEntity) {
        ((ServerPlayerEntity) lastMounted).connection.vehicleFloatingTickCount = 0;
      }
    } else {
      lastMounted.rotationYaw = lastMounted.rotationYawHead;
      // We have to hardcode this speed because the entity may already have ticked,
      // so we can't count on it having a predictable speed
      lastMounted.moveForward = 0.5F;
    }

    // Rotate broom
    rotationPitch = MathHelpers.normalizeAngle_180(lastMounted.rotationPitch);
    rotationYaw = MathHelpers.normalizeAngle_180(lastMounted.rotationYaw);

    // Apply maneuverability modifier
    float maneuverabilityFactor = 1F - 50f; // - (getModifier(BroomModifiers.MANEUVERABILITY) / 2000F);
    rotationPitch = rotationPitch * (1F - maneuverabilityFactor) + lastRotationPitch * maneuverabilityFactor;
    // These if's are necessary to fix rotation when the yaw goes over the border of -180F;+180F
    if (lastRotationYaw - rotationYaw > 180F) {
      lastRotationYaw -= 360F;
    }
    if (lastRotationYaw - rotationYaw < -180F) {
      lastRotationYaw += 360F;
    }
    rotationYaw = rotationYaw * (1F - maneuverabilityFactor) + lastRotationYaw * maneuverabilityFactor;
    lastRotationPitch = rotationPitch;
    lastRotationYaw = rotationYaw;

    // Limit the angle under which the player can move up or down
    if (rotationPitch > MAX_ANGLE)
      rotationPitch = MAX_ANGLE;
    else if (rotationPitch < MIN_ANGLE)
      rotationPitch = MIN_ANGLE;

    setRotation(rotationYaw, rotationPitch);

    // Handle player movement
    double pitch = ((rotationPitch + 90) * Math.PI) / 180;
    double yaw = ((rotationYaw + 90) * Math.PI) / 180;

    double x = Math.sin(pitch) * Math.cos(yaw);
    double z = Math.sin(pitch) * Math.sin(yaw);
    double y = Math.cos(pitch);

    /*// Apply speed modifier
    double playerSpeed = lastMounted.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
    //playerSpeed += getModifier(BroomModifiers.SPEED) / 100;
    LivingEntity currentRidingEntity = getControllingPassenger() instanceof LivingEntity ? (LivingEntity) getControllingPassenger() : null;
    float moveForward = lastMounted.moveForward;
    playerSpeed *= moveForward;

    // Apply acceleration modifier
    float slowingFactor = 1F; // - ((getModifier(BroomModifiers.ACCELERATION) + 1F) / 2500F);
    playerSpeed = playerSpeed * (1D - slowingFactor) + lastPlayerSpeed * slowingFactor;*/

    // Apply levitation modifier
    /*float levitation = 0.2f; //getModifier(BroomModifiers.LEVITATION);
    float levitationModifier = 0.2f; //(levitation / BroomModifiers.LEVITATION.getMaxTierValue()) * 1.5F;
    levitationModifier = Math.max(0.2F, levitationModifier);
    if (y < 0) {
      levitationModifier = Math.max(1.0F, levitationModifier);
    }*/

    // Save last speed (don't take into account water modifier)
    /*lastPlayerSpeed = playerSpeed;*/

    // Apply water speed
/*    if (this.inWater) {
      // Apply a log-scale
      float waterMovementFactor = 1f; //MathHelper.clamp(          getModifier(BroomModifiers.SWIMMING) / BroomModifiers.SWIMMING.getMaxTierValue(), 0F, 1F);
      waterMovementFactor = (float) Math.log10(1 + waterMovementFactor * 9);
      playerSpeed *= waterMovementFactor;
    }*/

    setMotion(getMotion()
        .mul(0.1, 0.1, 0.1)
        .add(x * SPEED /** playerSpeed*/, y * SPEED /** playerSpeed * levitationModifier*/, z * SPEED/* * playerSpeed*/));

    // Update motion on client side to provide a hovering effect
    if (world.isRemote()) {
      setMotion(getMotion().add(0, getHoverOffset(), 0));
    }

    move(MoverType.SELF, getMotion());
  }

  @Override
  public boolean onLivingFall(float distance, float damageMultiplier) {
    // Makes sure the player doesn't get any fall damage when on the broom
    return false;
  }

  @Override
  public boolean canBeRiddenInWater(Entity rider) {
    return true;
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 42.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(8, new AppleBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
  }

/*  @Override
  protected void setRotation(float yaw, float pitch) {
    this.rotationYaw = yaw % 360f;
    this.rotationPitch = pitch % 360f;
    // super.setRotation(yaw, pitch); ???
  }*/

  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putBoolean("saddled", dataManager.get(isSaddled));
  }

  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    dataManager.set(isSaddled, compound.getBoolean("saddled"));
  }

  public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
    if (/*dataManager.get(isSaddled) && */!this.isBeingRidden() && !p_230254_1_.isSecondaryUseActive()) {
      if (!this.world.isRemote) {
        p_230254_1_.startRiding(this);
      }

      return ActionResultType.func_233537_a_(this.world.isRemote);
    } else {
      return super.func_230254_b_(p_230254_1_, p_230254_2_);
    }
  }

  protected double getHoverOffset() {
    float x = world.getGameTime();
    float t = hoverTickOffset;
    double newHoverOffset = Math.cos(x / 10 + t) * Math.cos(x / 12 + t) * Math.cos(x / 15 + t) * MAX_COS_AMPLITUDE;

    double newHoverDifference = newHoverOffset - oldHoverOffset;
    oldHoverOffset += newHoverDifference;

    return newHoverDifference;
  }

/*  private float currentPitch;
  private float lastPitch;

  @Override
  @OnlyIn(Dist.CLIENT)
  public float getBodyPitch(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastPitch, this.currentPitch);
  }

  private void updateBodyPitch() {
    this.lastPitch = this.currentPitch;
    this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
  }*/
}
