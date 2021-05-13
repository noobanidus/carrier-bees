package noobanidus.mods.carrierbees.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.ai.CachedPathHolder;
import noobanidus.mods.carrierbees.entities.ai.SmartBee;
import noobanidus.mods.carrierbees.entities.projectiles.BucketEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrabbleBeeEntity extends AnimalEntity implements IFlyingAnimal, IAppleBee {
  private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.createKey(DrabbleBeeEntity.class, DataSerializers.BYTE);
  private float rollAmount;
  private float rollAmountO;
  private BlockPos target = null;
  private int ttl = -1;

  public static AxisAlignedBB FIRE_SEARCH = new AxisAlignedBB(-10, -10, -10, 11, 11, 11);

  public DrabbleBeeEntity(EntityType<? extends DrabbleBeeEntity> type, World worldIn) {
    super(type, worldIn);
    this.moveController = new FlyingMovementController(this, 20, true);
    this.lookController = new DrabbleBeeEntity.BeeLookController(this);
    this.setPathPriority(PathNodeType.WATER, -1.0F);
    this.setPathPriority(PathNodeType.WATER_BORDER, 16.0F);
    this.setPathPriority(PathNodeType.COCOA, -1.0F);
    this.setPathPriority(PathNodeType.FENCE, -1.0F);
  }

  public void setTTL (int ttl) {
    this.ttl = ttl;
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(DATA_FLAGS_ID, (byte) 0);
  }

  @Override
  public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
    return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new DrabbleBeeEntity.FireFightingGoal(this));
    this.goalSelector.addGoal(8, new DrabbleBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
    this.targetSelector.addGoal(0, new DrabbleBeeEntity.TargetFireGoal(this));
  }

  @Override
  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putInt("ttl", this.ttl);
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    if (compound.contains("ttl")) {
      setTTL(compound.getInt("ttl"));
    }
  }

  @Override
  public boolean attackEntityAsMob(Entity entityIn) {
    return false;
  }

  @Override
  public void tick() {
    super.tick();
    this.updateBodyPitch();
  }

/*  private void startMovingTo(BlockPos pos) {
    Vector3d vector3d = Vector3d.copyCenteredHorizontally(pos);
    int i = 0;
    BlockPos blockpos = this.getPosition();
    int j = (int) vector3d.y - blockpos.getY();
    if (j > 2) {
      i = 4;
    } else if (j < -2) {
      i = -4;
    }

    int k = 6;
    int l = 8;
    int i1 = blockpos.manhattanDistance(pos);
    if (i1 < 15) {
      k = i1 / 2;
      l = i1 / 2;
    }

    Vector3d vector3d1 = RandomPositionGenerator.func_226344_b_(this, k, l, i, vector3d, (double) ((float) Math.PI / 10F));
    if (vector3d1 != null) {
      this.navigator.setRangeMultiplier(0.5F);
      this.navigator.tryMoveToXYZ(vector3d1.x, vector3d1.y, vector3d1.z, 1.0D);
    }
  }*/

  @OnlyIn(Dist.CLIENT)
  public float getBodyPitch(float p_226455_1_) {
    return MathHelper.lerp(p_226455_1_, this.rollAmountO, this.rollAmount);
  }

  private void updateBodyPitch() {
    this.rollAmountO = this.rollAmount;
    if (this.isNearTarget()) {
      this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
    } else {
      this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
    }

  }

  @Override
  protected void updateAITasks() {
  }

  @Override
  public void livingTick() {
    super.livingTick();
    if (!this.world.isRemote && ttl != -1) {
      if (ttl-- <= 0) {
        this.remove();
      }
    }
    if (!this.world.isRemote) {
      boolean flag = this.getAttackTarget() != null && this.getAttackTarget().getDistanceSq(this) < 4.0D;
      this.setNearTarget(flag);
    }
  }

  private boolean isNearTarget() {
    return this.getBeeFlag(2);
  }

  private void setNearTarget(boolean p_226452_1_) {
    this.setBeeFlag(2, p_226452_1_);
  }

/*  private boolean isTooFar(BlockPos pos) {
    return !this.isWithinDistance(pos, 32);
  }*/

  private void setBeeFlag(int flagId, boolean p_226404_2_) {
    if (p_226404_2_) {
      this.dataManager.set(DATA_FLAGS_ID, (byte) (this.dataManager.get(DATA_FLAGS_ID) | flagId));
    } else {
      this.dataManager.set(DATA_FLAGS_ID, (byte) (this.dataManager.get(DATA_FLAGS_ID) & ~flagId));
    }

  }

  private boolean getBeeFlag(int flagId) {
    return (this.dataManager.get(DATA_FLAGS_ID) & flagId) != 0;
  }

  public static AttributeModifierMap.MutableAttribute attr() {
    return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 10.0D).createMutableAttribute(Attributes.FLYING_SPEED, (double) 0.6F).createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 0.3F).createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 48.0D);
  }

  @Override
  protected PathNavigator createNavigator(World worldIn) {
    FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn) {
      @Override
      public boolean canEntityStandOnPos(BlockPos pos) {
        return !this.world.getBlockState(pos.down()).isAir();
      }
    };
    flyingpathnavigator.setCanOpenDoors(false);
    flyingpathnavigator.setCanSwim(false);
    flyingpathnavigator.setCanEnterDoors(true);
    return flyingpathnavigator;
  }

  @Override
  public boolean isBreedingItem(ItemStack stack) {
    return stack.getItem().isIn(ItemTags.FLOWERS);
  }

/*  private boolean isFlowers(BlockPos pos) {
    return this.world.isBlockPresent(pos) && this.world.getBlockState(pos).getBlock().isIn(BlockTags.FLOWERS);
  }*/

  @Override
  protected void playStepSound(BlockPos pos, BlockState blockIn) {
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return null;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
    return SoundEvents.ENTITY_BEE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_BEE_DEATH;
  }

  @Override
  protected float getSoundVolume() {
    return 0.4F;
  }

  @Override
  public DrabbleBeeEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
    return null;
  }

  @Override
  protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
    return this.isChild() ? sizeIn.height * 0.5F : sizeIn.height * 0.5F;
  }

  @Override
  public boolean onLivingFall(float distance, float damageMultiplier) {
    return false;
  }

  @Override
  protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
  }

  @Override
  protected boolean makeFlySound() {
    return true;
  }

  /**
   * Called when the entity is attacked.
   */
  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (this.isInvulnerableTo(source)) {
      return false;
    } else {
      if (source.isFireDamage()) {
        return false;
      }

      return super.attackEntityFrom(source, amount);
    }
  }

  @Override
  public CreatureAttribute getCreatureAttribute() {
    return CreatureAttribute.ARTHROPOD;
  }

  @Override
  protected void handleFluidJump(ITag<Fluid> fluidTag) {
    this.setMotion(this.getMotion().add(0.0D, 0.01D, 0.0D));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public Vector3d func_241205_ce_() {
    return new Vector3d(0.0D, (double) (0.5F * this.getEyeHeight()), (double) (this.getWidth() * 0.2F));
  }

  @Override
  public boolean safeIsAngry() {
    return false;
  }

  @Override
  public boolean isBeehemoth() {
    return false;
  }

/*  private boolean isWithinDistance(BlockPos pos, int distance) {
    return pos.withinDistance(this.getPosition(), (double) distance);
  }*/

  class BeeLookController extends LookController {
    BeeLookController(MobEntity beeIn) {
      super(beeIn);
    }

    @Override
    protected boolean shouldResetPitch() {
      return true;
    }

    @Override
    public void setLookPositionWithEntity(Entity p_75651_1_, float p_75651_2_, float p_75651_3_) {
      if (p_75651_1_ != null) {
        super.setLookPositionWithEntity(p_75651_1_, p_75651_2_, p_75651_3_);
      }
    }
  }

  abstract class PassiveGoal extends Goal {
    private PassiveGoal() {
    }

    public abstract boolean canBeeStart();

    public abstract boolean canBeeContinue();

    /**
     * Returns whether execution should begin. You can also read and cache any fire necessary for execution in this
     * method as well.
     */
    @Override
    public boolean shouldExecute() {
      return this.canBeeStart();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
      return this.canBeeContinue();
    }
  }

  class WanderGoal extends Goal {
    private CachedPathHolder cachedPathHolder;

    WanderGoal() {
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any fire necessary for execution in this
     * method as well.
     */
    @Override
    public boolean shouldExecute() {
      return DrabbleBeeEntity.this.navigator.noPath() && DrabbleBeeEntity.this.rand.nextInt(10) == 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
      return DrabbleBeeEntity.this.navigator.hasPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
      if (ConfigManager.getImprovedAI()) {
        cachedPathHolder = SmartBee.smartBee(DrabbleBeeEntity.this, cachedPathHolder);
      } else {
        Vector3d pos = this.getRandomLocation();
        if (pos != null) {
          DrabbleBeeEntity.this.navigator.setPath(DrabbleBeeEntity.this.navigator.getPathToPos(new BlockPos(pos), 1), 1.0D);
        }
      }
    }

    @Nullable
    private Vector3d getRandomLocation() {
      Vector3d vector3d = DrabbleBeeEntity.this.getLook(0.0F);

      Vector3d vector3d2 = RandomPositionGenerator.findAirTarget(DrabbleBeeEntity.this, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
      return vector3d2 != null ? vector3d2 : RandomPositionGenerator.findGroundTarget(DrabbleBeeEntity.this, 8, 4, -2, vector3d, (double) ((float) Math.PI / 2F));
    }
  }

  public static class TargetFireGoal extends Goal {
    private final DrabbleBeeEntity entity;
    private int tickCache;

    public TargetFireGoal(DrabbleBeeEntity entity) {
      this.entity = entity;
      tickCache = 40;
    }

    @Override
    public void tick() {
      if (this.tickCache-- <= 0) {
        tickCache = 20;
        if (this.entity.target == null) {
          AtomicBoolean foundFire = new AtomicBoolean(false);
          AxisAlignedBB box = DrabbleBeeEntity.FIRE_SEARCH.offset(this.entity.getPosition());
          BlockPos.getAllInBoxMutable((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ).forEach(spot -> {
            if (foundFire.get()) {
              return;
            }
            BlockState state = this.entity.world.getBlockState(spot);
            if (state.getBlock() == Blocks.FIRE && !this.entity.world.getBlockState(spot.down()).isFireSource(this.entity.world, spot.down(), Direction.UP)) {
              this.entity.target = spot.toImmutable();
              foundFire.set(true);
            }
          });
        }
      }
    }

    @Override
    public boolean shouldExecute() {
      return this.entity.target == null;
    }

    @Override
    public void resetTask() {
      this.entity.target = null;
    }
  }

  public static class FireFightingGoal extends Goal {
    private DrabbleBeeEntity entity;

    public FireFightingGoal(DrabbleBeeEntity entity) {
      this.entity = entity;
    }

    @Override
    public boolean shouldExecute() {
      return this.entity.target != null;
    }

    @Override
    public void tick() {
      if (this.entity.target != null) {
        BlockState state = this.entity.world.getBlockState(this.entity.target);
        if (state.getBlock() == Blocks.FIRE && !this.entity.world.getBlockState(this.entity.target.down()).isFireSource(this.entity.world, this.entity.target.down(), Direction.UP)) {
          BlockPos pos = this.entity.target;
          double d2 = pos.getX() + 0.5 - this.entity.getPosX();
          double d3 = pos.getY() - (0.5D + this.entity.getPosYHeight(0.5d));
          double d4 = pos.getZ() + 0.5 - this.entity.getPosZ();
          BucketEntity bucket = new BucketEntity(this.entity, d2, d3, d4, this.entity.world);
          bucket.setPosition(this.entity.getPosX(), this.entity.getPosYHeight(0.5D) + 0.5D, bucket.getPosZ());
          this.entity.world.addEntity(bucket);
        }
      }
    }

    @Override
    public void resetTask() {
      this.entity.target = null;
    }
  }
}
