package noobanidus.mods.carrierbees.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.entities.ai.BeehemothAIRide;
import noobanidus.mods.carrierbees.init.ModItems;
import noobanidus.mods.carrierbees.init.ModSounds;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BeehemothEntity extends TameableEntity implements IFlyingAnimal, IAppleBee {
  private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Boolean> QUEEN = EntityDataManager.createKey(BeehemothEntity.class, DataSerializers.BOOLEAN);

  private boolean stopWandering = false;
  private boolean hasItemTarget = false;

  public float offset1, offset2, offset3, offset4, offset5, offset6;

  public BeehemothEntity(EntityType<? extends BeehemothEntity> type, World world) {
    super(type, world);
    this.moveController = new MoveHelperController(this);
    this.offset1 = (this.rand.nextFloat() - 0.5f);
    this.offset2 = (this.rand.nextFloat() - 0.5f);
    this.offset3 = (this.rand.nextFloat() - 0.5f);
    this.offset4 = (this.rand.nextFloat() - 0.5f);
    this.offset5 = (this.rand.nextFloat() - 0.5f);
    this.offset6 = (this.rand.nextFloat() - 0.5f);
  }

  private static TranslationTextComponent QUEEN_NAME = new TranslationTextComponent("entity.carrierbees.beehemoth_queen");

  @Override
  protected ITextComponent getProfessionName() {
    if (isQueen()) {
      return QUEEN_NAME;
    }
    return super.getProfessionName();
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(SADDLED, false);
    this.dataManager.register(QUEEN, false);
  }

  public boolean isQueen() {
    return this.dataManager.get(QUEEN);
  }

  public boolean isSaddled() {
    return this.dataManager.get(SADDLED);
  }

  public void setSaddled(boolean saddled) {
    this.dataManager.set(SADDLED, saddled);
  }

  public void setQueen(boolean queen) {
    this.dataManager.set(QUEEN, queen);
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    tag.putBoolean("saddled", isSaddled());
    tag.putBoolean("queen", isQueen());
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
    setSaddled(tag.getBoolean("saddled"));
    setQueen(tag.contains("queen") && tag.getBoolean("queen"));
  }

  @Override
  protected boolean canTriggerWalking() {
    return false;
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (source == DamageSource.OUT_OF_WORLD || source.getTrueSource() instanceof PlayerEntity) {
      return super.attackEntityFrom(source, amount);
    }

    return false;
  }

  public static AttributeModifierMap.MutableAttribute createAttributes() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 42.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new BeehemothAIRide(this, 3.2D));
    this.goalSelector.addGoal(3, new TemptGoal(this, 3.2d, Ingredient.fromItems(Items.SUGAR), false));
    this.goalSelector.addGoal(4, new RandomFlyGoal(this));
    this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 10));
    this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    this.goalSelector.addGoal(9, new SwimGoal(this));
  }

  @Override
  protected PathNavigator createNavigator(World p_175447_1_) {
    return new DirectPathNavigator(this, p_175447_1_);
  }

  @Nullable
  @Override
  public Entity getControllingPassenger() {
    for (Entity p : this.getPassengers()) {
      return p;
    }
    return null;
  }

  public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    Item item = stack.getItem();
    if (this.world.isRemote) {
      if (this.isTamed() && this.isOwner(player)) {
        return ActionResultType.SUCCESS;
      } else {
        return !(this.getHealth() < this.getMaxHealth()) && this.isTamed() ? ActionResultType.PASS : ActionResultType.SUCCESS;
      }
    } else {
      if (this.isTamed()) {
        if (this.isOwner(player)) {
          if (item == ModItems.ROYAL_JELLY.get() && !isQueen()) {
            this.consumeItemFromStack(player, stack);
            setQueen(true);
            CarrierBees.QUEEN_PREDICATE.trigger((ServerPlayerEntity) player, null);
            return ActionResultType.CONSUME;
          }

          if (item == Items.SUGAR && this.getHealth() < this.getMaxHealth()) {
            this.consumeItemFromStack(player, stack);
            this.heal(10);
            return ActionResultType.CONSUME;
          }

          if (item == Items.SADDLE && !isSaddled()) {
            this.consumeItemFromStack(player, stack);
            this.setSaddled(true);
            CarrierBees.STEED_PREDICATE.trigger((ServerPlayerEntity) player, null);
            return ActionResultType.CONSUME;
          }

          if (stack.isEmpty() && isSaddled() && player.isSneaking()) {
            setSaddled(false);
            ItemStack saddle = new ItemStack(Items.SADDLE);
            if (player.addItemStackToInventory(saddle)) {
              ItemEntity entity = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), saddle);
              player.world.addEntity(entity);
            }
          }

          if (stack.isEmpty() && !this.isBeingRidden() && !player.isSecondaryUseActive()) {
            if (!this.world.isRemote) {
              player.startRiding(this);
            }

            return ActionResultType.func_233537_a_(this.world.isRemote);
          }
        }
      } else if (item == Items.SUGAR) {
        this.consumeItemFromStack(player, stack);
        if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
          this.setTamedBy(player);
          this.func_233687_w_(true);
          this.world.setEntityState(this, (byte) 7);
        } else {
          this.world.setEntityState(this, (byte) 6);
        }

        this.enablePersistence();
        return ActionResultType.CONSUME;
      }

      ActionResultType actionresulttype1 = super.func_230254_b_(player, hand);
      if (actionresulttype1.isSuccessOrConsume()) {
        this.enablePersistence();
      }

      return actionresulttype1;
    }
  }

  public void updatePassenger(Entity passenger) {
    if (this.isPassenger(passenger)) {
      float radius = -0.25F;
      float angle = (0.01745329251F * this.renderYawOffset);
      double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
      double extraZ = radius * MathHelper.cos(angle);
      passenger.setPosition(this.getPosX() + extraX, this.getPosY() + this.getMountedYOffset() + passenger.getYOffset(), this.getPosZ() + extraZ);
    }
  }

  public double getMountedYOffset() {
    float f = Math.min(0.25F, this.limbSwingAmount);
    float f1 = this.limbSwing;
    return (double) this.getHeight() - 0.2D + (double) (0.12F * MathHelper.cos(f1 * 0.7F) * 0.7F * f);
  }

  public boolean hasNoGravity() {
    return true;
  }

  @Override
  protected void updateFallState(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
  }

  @Override
  public boolean onLivingFall(float p_225503_1_, float p_225503_2_) {
    return false;
  }

  @Override
  protected void playFallSound() {
  }

  @Override
  protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return null;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return ModSounds.BEEHEMOTH_HURT.get();
  }

  @Override
  protected SoundEvent getDeathSound() {
    return ModSounds.BEEHEMOTH_DEATH.get();
  }

  public void tick() {
    super.tick();
    stopWandering = getLeashed();
  }

  private BlockPos getGroundPosition(BlockPos radialPos) {
    while (radialPos.getY() > 1 && world.isAirBlock(radialPos)) {
      radialPos = radialPos.down();
    }
    if (radialPos.getY() <= 1) {
      return new BlockPos(radialPos.getX(), world.getSeaLevel(), radialPos.getZ());
    }
    return radialPos;
  }

  @Nullable
  @Override
  public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
    return null;
  }

  @Override
  public boolean safeIsAngry() {
    return false;
  }

  @Override
  public boolean isBeehemoth() {
    return true;
  }

  @Override
  public void setLeashHolder(Entity p_110162_1_, boolean p_110162_2_) {
    super.setLeashHolder(p_110162_1_, p_110162_2_);
    stopWandering = true;
  }

  static class MoveHelperController extends MovementController {
    private final BeehemothEntity parentEntity;

    public MoveHelperController(BeehemothEntity sunbird) {
      super(sunbird);
      this.parentEntity = sunbird;
    }

    public void tick() {
      if (this.action == Action.STRAFE) {
        Vector3d vector3d = new Vector3d(this.posX - parentEntity.getPosX(), this.posY - parentEntity.getPosY(), this.posZ - parentEntity.getPosZ());
        double d0 = vector3d.length();
        parentEntity.setMotion(parentEntity.getMotion().add(0, vector3d.scale(this.speed * 0.05D / d0).getY(), 0));
        float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float f1 = (float) this.speed * f;
        this.moveForward = 1.0F;
        this.moveStrafe = 0.0F;

        this.mob.setAIMoveSpeed(f1);
        this.mob.setMoveForward(this.moveForward);
        this.mob.setMoveStrafing(this.moveStrafe);
        this.action = MovementController.Action.WAIT;
      } else if (this.action == MovementController.Action.MOVE_TO) {
        Vector3d vector3d = new Vector3d(this.posX - parentEntity.getPosX(), this.posY - parentEntity.getPosY(), this.posZ - parentEntity.getPosZ());
        double d0 = vector3d.length();
        if (d0 < parentEntity.getBoundingBox().getAverageEdgeLength()) {
          this.action = MovementController.Action.WAIT;
          parentEntity.setMotion(parentEntity.getMotion().scale(0.5D));
        } else {
          double localSpeed = this.speed;
          if (parentEntity.isBeingRidden()) {
            localSpeed *= 1.5D;
          }
          parentEntity.setMotion(parentEntity.getMotion().add(vector3d.scale(localSpeed * 0.005D / d0)));
          if (parentEntity.getAttackTarget() == null) {
            Vector3d vector3d1 = parentEntity.getMotion();
            parentEntity.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
            parentEntity.renderYawOffset = parentEntity.rotationYaw;
          } else {
            double d2 = parentEntity.getAttackTarget().getPosX() - parentEntity.getPosX();
            double d1 = parentEntity.getAttackTarget().getPosZ() - parentEntity.getPosZ();
            parentEntity.rotationYaw = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
            parentEntity.renderYawOffset = parentEntity.rotationYaw;
          }
        }

      }
    }
  }

  public boolean isTargetBlocked(Vector3d target) {
    Vector3d Vector3d = new Vector3d(this.getPosX(), this.getPosYEye(), this.getPosZ());
    return this.world.rayTraceBlocks(new RayTraceContext(Vector3d, target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() != RayTraceResult.Type.MISS;
  }

  public class DirectPathNavigator extends GroundPathNavigator {

    private MobEntity mob;

    public DirectPathNavigator(MobEntity mob, World world) {
      super(mob, world);
      this.mob = mob;
    }

    public void tick() {
      ++this.totalTicks;
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
      mob.getMoveHelper().setMoveTo(x, y, z, speedIn);
      return true;
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
      mob.getMoveHelper().setMoveTo(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ(), speedIn);
      return true;
    }
  }


  static class RandomFlyGoal extends Goal {
    private final BeehemothEntity parentEntity;
    private BlockPos target = null;

    public RandomFlyGoal(BeehemothEntity mosquito) {
      this.parentEntity = mosquito;
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
      MovementController movementcontroller = this.parentEntity.getMoveHelper();
      if (parentEntity.stopWandering || parentEntity.hasItemTarget) {
        return false;
      }
      if (!movementcontroller.isUpdating() || target == null) {
        target = getBlockInViewBeehemoth();
        if (target != null) {
          this.parentEntity.getMoveHelper().setMoveTo(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 1.0D);
        }
        return true;
      }
      return false;
    }

    public boolean shouldContinueExecuting() {
      return target != null && !parentEntity.stopWandering && !parentEntity.hasItemTarget && parentEntity.getDistanceSq(Vector3d.copyCentered(target)) > 2.4D && parentEntity.getMoveHelper().isUpdating() && !parentEntity.collidedHorizontally;
    }

    public void resetTask() {
      target = null;
    }

    public void tick() {
      if (target == null) {
        target = getBlockInViewBeehemoth();
      }
      if (target != null) {
        this.parentEntity.getMoveHelper().setMoveTo(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 1.0D);
        if (parentEntity.getDistanceSq(Vector3d.copyCentered(target)) < 2.5F) {
          target = null;
        }
      }
    }

    public BlockPos getBlockInViewBeehemoth() {
      float radius = 1 + parentEntity.getRNG().nextInt(5);
      float neg = parentEntity.getRNG().nextBoolean() ? 1 : -1;
      float renderYawOffset = parentEntity.renderYawOffset;
      float angle = (0.01745329251F * renderYawOffset) + 3.15F + (parentEntity.getRNG().nextFloat() * neg);
      double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
      double extraZ = radius * MathHelper.cos(angle);
      BlockPos radialPos = new BlockPos(parentEntity.getPosX() + extraX, parentEntity.getPosY() + 2, parentEntity.getPosZ() + extraZ);
      BlockPos ground = parentEntity.getGroundPosition(radialPos);
      BlockPos newPos = ground.up(1 + parentEntity.getRNG().nextInt(6));
      if (!parentEntity.isTargetBlocked(Vector3d.copyCentered(newPos)) && parentEntity.getDistanceSq(Vector3d.copyCentered(newPos)) > 6) {
        return newPos;
      }
      return null;
    }
  }
}
