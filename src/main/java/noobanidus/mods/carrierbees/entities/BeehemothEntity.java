package noobanidus.mods.carrierbees.entities;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BeehemothEntity extends AnimalEntity implements IFlyingAnimal, ISyncMount, IFlyingMount {
  private static final int FLIGHT_CHANCE_PER_TICK = 1200;
  private static final DataParameter<Boolean> HOVERING = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.BYTE);

  public float sitProgress;
  public float hoverProgress;
  public float flyProgress;
  public int spacebarTicks;
  public int airBorneCounter;
  public BlockPos homePos;
  public boolean hasHomePosition = false;
  public int feedings = 0;
  private boolean isLandNavigator;
  private boolean isSitting;
  private boolean isHovering;
  private boolean isFlying;
  private int animationTick;
  private int flyTicks;
  private int hoverTicks;
  private boolean hasChestVarChanged = false;
  private int navigatorType = -1;
  private boolean isOverAir;

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

  protected void switchNavigator() {
    if (this.isBeingRidden() && this.isOverAir()) {
      if (navigatorType != 1) {
        this.moveController = new FlightManager.PlayerFlightMoveHelper<>(this);
        this.navigator = new PathNavigateFlyingCreature(this, world);
        navigatorType = 1;
      }
    }
    if (!this.isBeingRidden() || !this.isOverAir()) {
      if (navigatorType != 0) {
        this.moveController = new MovementController(this);
        this.navigator = new GroundPathNavigator(this, world);
        navigatorType = 0;
      }
    }
  }

  protected void switchNavigator(boolean onLand) {
    if (onLand) {
      this.moveController = new MovementController(this);
      this.navigator = createNavigator(world, AdvancedPathNavigate.MovementType.CLIMBING);
      this.isLandNavigator = true;
    } else {
      this.moveController = new EntityHippogryph.FlyMoveHelper(this);
      this.navigator = createNavigator(world, AdvancedPathNavigate.MovementType.FLYING);
      this.isLandNavigator = false;
    }
  }

  protected boolean isOverAir() {
    return isOverAir;
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(HOVERING, false);
    this.dataManager.register(FLYING, false);
    this.dataManager.register(CONTROL_STATE, (byte) 0);
  }

  @Override
  public double getYSpeedMod() {
    return 4;
  }

  private boolean isOverAirLogic() {
    return world.isAirBlock(new BlockPos(this.getPosX(), this.getBoundingBox().minY - 1, this.getPosZ()));
  }

  @Override
  public boolean canPassengerSteer() {
    return false; // ??
  }

  @Override
  public boolean canBeSteered() {
    return true;
  }

  public void updatePassenger(Entity passenger) {
    super.updatePassenger(passenger);
    if (this.isPassenger(passenger)) {
      renderYawOffset = rotationYaw;
      this.rotationYaw = passenger.rotationYaw;
    }
    passenger.setPosition(this.getPosX(), this.getPosY() + 1.05F, this.getPosZ());
  }

  @Nullable
  @Override
  public Entity getControllingPassenger() {
    for (Entity p : this.getPassengers()) {
      return p;
    }
    return null;
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
        player.startRiding(this, true);
      }

      return ActionResultType.func_233537_a_(this.world.isRemote);
    } else {
      return super.func_230254_b_(player, p_230254_2_);
    }
  }

  public boolean isGoingUp() {
    return (dataManager.get(CONTROL_STATE) & 1) == 1;
  }

  public boolean isGoingDown() {
    return (dataManager.get(CONTROL_STATE) >> 1 & 1) == 1;
  }

  public boolean dismountIAF() {
    return (dataManager.get(CONTROL_STATE).byteValue() >> 3 & 1) == 1;
  }

  public void up(boolean up) {
    setStateField(0, up);
  }

  public void down(boolean down) {
    setStateField(1, down);
  }

  public void dismount(boolean dismount) {
    setStateField(3, dismount);
  }

  private void setStateField(int i, boolean newState) {
    byte prevState = dataManager.get(CONTROL_STATE);
    if (newState) {
      dataManager.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
    } else {
      dataManager.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
    }
  }

  public byte getControlState() {
    return dataManager.get(CONTROL_STATE);
  }

  public void setControlState(byte state) {
    dataManager.set(CONTROL_STATE, state);

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

  @Override
  protected void updateFallState(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
  }

  @Override
  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putBoolean("hovering", this.isHovering());
    compound.putBoolean("flying", this.isFlying());
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.setHovering(compound.getBoolean("hover"));
    this.setFlying(compound.getBoolean("flying"));
  }

  public boolean isHovering() {
    if (world.isRemote) {
      return this.isHovering = Boolean.valueOf(this.dataManager.get(HOVERING).booleanValue());
    }
    return isHovering;
  }

  public void setHovering(boolean hovering) {
    this.dataManager.set(HOVERING, Boolean.valueOf(hovering));
    if (!world.isRemote) {
      this.isHovering = Boolean.valueOf(hovering);
    }
  }

  public boolean isRidingPlayer(PlayerEntity player) {
    return getRidingPlayer() != null && player != null && getRidingPlayer().getUniqueID().equals(player.getUniqueID());
  }

  @Nullable
  public PlayerEntity getRidingPlayer() {
    if (this.getControllingPassenger() instanceof PlayerEntity) {
      return (PlayerEntity) this.getControllingPassenger();
    }
    return null;

  }

  @Override
  public double getFlightSpeedModifier() {
    return 1;
  }

  public boolean isFlying() {
    if (world.isRemote) {
      return this.isFlying = this.dataManager.get(FLYING);
    }
    return isFlying;
  }

  public void setFlying(boolean flying) {
    this.dataManager.set(FLYING, flying);
    if (!world.isRemote) {
      this.isFlying = flying;
    }
  }

  @OnlyIn(Dist.CLIENT)
  protected void updateClientControls() {
    Minecraft mc = Minecraft.getInstance();
    if (this.isRidingPlayer(mc.player)) {
      byte previousState = getControlState();
      up(mc.gameSettings.keyBindJump.isKeyDown());
      down(IafKeybindRegistry.dragon_down.isKeyDown());
      attack(IafKeybindRegistry.dragon_strike.isKeyDown());
      dismount(mc.gameSettings.keyBindSneak.isKeyDown());
      byte controlState = getControlState();
      if (controlState != previousState) {
        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, getPosX(), getPosY(), getPosZ()));
      }
    }
    if (this.getRidingEntity() != null && this.getRidingEntity() == mc.player) {
      byte previousState = getControlState();
      dismount(mc.gameSettings.keyBindSneak.isKeyDown());
      byte controlState = getControlState();
      if (controlState != previousState) {
        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, getPosX(), getPosY(), getPosZ()));
      }
    }
  }

  public boolean canMove() {
    return this.getControllingPassenger() == null && sitProgress == 0;
  }

  @Override
  public void travel(Vector3d move) {
    if (!this.canMove() && !this.isBeingRidden()) {
      super.travel(Vector3d.ZERO);
      return;
    }
    super.travel(move);
  }

  @Override
  public void livingTick() {
    super.livingTick();
    if (!world.isRemote && !this.isOverAir() && this.getNavigator().noPath() && this.getAttackTarget() != null && this.getAttackTarget().getPosY() - 3 > this.getPosY() && this.getRNG().nextInt(15) == 0 && this.canMove() && !this.isHovering() && !this.isFlying()) {
      this.setHovering(true);
      this.hoverTicks = 0;
      this.flyTicks = 0;
    }
    if (this.isOverAir()) {
      airBorneCounter++;
    } else {
      airBorneCounter = 0;
    }
    if (this.isFlying() && this.ticksExisted % 40 == 0) {
      this.setFlying(true);
    }
    if (!this.canMove()) {
      this.getNavigator().clearPath();
    }
    boolean hovering = isHovering();
    if (hovering && hoverProgress < 20.0F) {
      hoverProgress += 0.5F;
    } else if (!hovering && hoverProgress > 0.0F) {
      hoverProgress -= 0.5F;
    }
    boolean flying = this.isFlying() || this.isHovering() && airBorneCounter > 10;
    if (flying && flyProgress < 20.0F) {
      flyProgress += 0.5F;
    } else if (!flying && flyProgress > 0.0F) {
      flyProgress -= 0.5F;
    }
    if (flying && this.isLandNavigator) {
      switchNavigator(false);
    }
    if (!flying && !this.isLandNavigator) {
      switchNavigator(true);
    }
    if ((flying || hovering) && !doesWantToLand()) {
      double up = isInWater() ? 0.16D : 0.08D;
      this.setMotion(this.getMotion().add(0, up, 0));
    }
    if (this.isOnGround() && this.doesWantToLand() && (this.isFlying() || this.isHovering())) {
      this.setFlying(false);
      this.setHovering(false);
    }
    if (this.isHovering()) {
      if (this.isSitting()) {
        this.setHovering(false);
      }
      this.hoverTicks++;
      if (this.doesWantToLand()) {
        this.setMotion(this.getMotion().add(0, -0.25D, 0));
      } else {
        if (this.getControllingPassenger() == null) {
          this.setMotion(this.getMotion().add(0, 0.08D, 0));
        }
        if (this.hoverTicks > 40) {
          if (!this.isChild()) {
            this.setFlying(true);
          }
          this.setHovering(false);
          this.hoverTicks = 0;
          this.flyTicks = 0;
        }
      }
    }
    if (this.isSitting()) {
      this.getNavigator().clearPath();
    }
    if (this.isOnGround() && flyTicks != 0) {
      flyTicks = 0;
    }
    if (this.isFlying() && this.doesWantToLand() && this.getControllingPassenger() == null) {
      this.setHovering(false);
      if (this.isOnGround()) {
        flyTicks = 0;
      }
      this.setFlying(false);
    }
    if (this.isFlying()) {
      this.flyTicks++;
    }
    if ((this.isHovering() || this.isFlying()) && this.isSitting()) {
      this.setFlying(false);
      this.setHovering(false);
    }
    if (this.isBeingRidden() && this.isGoingDown() && this.isOnGround()) {
      this.setHovering(false);
      this.setFlying(false);
    }
    if ((!world.isRemote && this.getRNG().nextInt(FLIGHT_CHANCE_PER_TICK) == 0 && !this.isSitting() && !this.isFlying() && this.getPassengers().isEmpty() && !this.isChild() && !this.isHovering() && !this.isSitting() && this.canMove() && !this.isOverAir() || this.getPosY() < -1)) {
      this.setHovering(true);
      this.hoverTicks = 0;
      this.flyTicks = 0;
    }
    if (getAttackTarget() != null && !this.getPassengers().isEmpty()) {
      this.setAttackTarget(null);
    }
  }

  @Override
  public void tick() {
    super.tick();
    isOverAir = this.isOverAirLogic();
    if (world.isRemote) {
      this.updateClientControls();
    }
    if (this.isGoingUp()) {
      if (this.airBorneCounter == 0) {
        this.setMotion(this.getMotion().add(0, 0.4F, 0));
      }
      if (!this.isFlying() && !this.isHovering()) {
        this.spacebarTicks += 2;
      }
    } else if (this.dismountIAF()) {
      if (this.isFlying() || this.isHovering()) {
        this.setFlying(false);
        this.setHovering(false);
      }
    }
    if (this.getControllingPassenger() != null && this.getControllingPassenger().isSneaking()) {
      this.getControllingPassenger().stopRiding();
    }

    double motion = this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z;//Use squared norm2

    if (this.isFlying() && !this.isHovering() && this.getControllingPassenger() != null && this.isOverAir() && motion < 0.01F) {
      this.setHovering(true);
      this.setFlying(false);
    }
    if (this.isHovering() && !this.isFlying() && this.getControllingPassenger() != null && this.isOverAir() && motion > 0.01F) {
      this.setFlying(true);
      this.setHovering(false);
    }
    if (this.spacebarTicks > 0) {
      this.spacebarTicks--;
    }
    if (this.spacebarTicks > 10 && this.getControllingPassenger() != null && !this.isFlying() && !this.isHovering()) {
      this.setHovering(true);
    }
  }

  public float getDistanceSquared(Vector3d Vector3d) {
    float f = (float) (this.getPosX() - Vector3d.x);
    float f1 = (float) (this.getPosY() - Vector3d.y);
    float f2 = (float) (this.getPosZ() - Vector3d.z);
    return f * f + f1 * f1 + f2 * f2;
  }

  public boolean isSitting() {
    return false;
  }

  public boolean doesWantToLand() {
    return (this.flyTicks > 200 || flyTicks > 40 && this.flyProgress == 0) && !this.isBeingRidden();
  }

  protected PathNavigator createNavigator(World worldIn) {
    return createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
  }

  protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type) {
    return createNavigator(worldIn, type, 2, 2);
  }

  protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
    AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, world, type, width, height);
    this.navigator = newNavigator;
    newNavigator.setCanSwim(true);
    newNavigator.getNodeProcessor().setCanOpenDoors(true);
    return newNavigator;
  }

  class FlyMoveHelper extends MovementController {
        public FlyMoveHelper(EntityHippogryph pixie) {
            super(pixie);
            this.speed = 1.75F;
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                if (EntityHippogryph.this.collidedHorizontally) {
                    EntityHippogryph.this.rotationYaw += 180.0F;
                    BlockPos target = DragonUtils.getBlockInViewHippogryph(EntityHippogryph.this, 180);
                    this.speed = 0.1F;
                    if (target != null) {
                        this.posX = target.getX() + 0.5F;
                        this.posY = target.getY() + 0.5F;
                        this.posZ = target.getZ() + 0.5F;
                    }
                }
                double d0 = this.posX - EntityHippogryph.this.getPosX();
                double d1 = this.posY - EntityHippogryph.this.getPosY();
                double d2 = this.posZ - EntityHippogryph.this.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);

                if (d3 < EntityHippogryph.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    EntityHippogryph.this.setMotion(EntityHippogryph.this.getMotion().mul(0.5D, 0.5D, 0.5D));
                } else {
                    EntityHippogryph.this.setMotion(EntityHippogryph.this.getMotion().add(d0 / d3 * 0.1D * this.speed, d1 / d3 * 0.1D * this.speed, d2 / d3 * 0.1D * this.speed));

                    if (EntityHippogryph.this.getAttackTarget() == null) {
                        EntityHippogryph.this.rotationYaw = -((float) MathHelper.atan2(EntityHippogryph.this.getMotion().x, EntityHippogryph.this.getMotion().z)) * (180F / (float) Math.PI);
                        EntityHippogryph.this.renderYawOffset = EntityHippogryph.this.rotationYaw;
                    } else {
                        double d4 = EntityHippogryph.this.getAttackTarget().getPosX() - EntityHippogryph.this.getPosX();
                        double d5 = EntityHippogryph.this.getAttackTarget().getPosZ() - EntityHippogryph.this.getPosZ();
                        EntityHippogryph.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityHippogryph.this.renderYawOffset = EntityHippogryph.this.rotationYaw;
                    }
                }
            }
        }
    }
}
