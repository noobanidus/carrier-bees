package noobanidus.mods.carrierbees.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.Tag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import noobanidus.mods.carrierbees.config.ConfigManager;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

@SuppressWarnings("NullableProblems")
public abstract class AppleBeeEntity extends AnimalEntity implements IFlyingAnimal {
  private static final DataParameter<Integer> anger = EntityDataManager.createKey(AppleBeeEntity.class, DataSerializers.VARINT);
  private static final DataParameter<Byte> multipleByteTracker = EntityDataManager.createKey(AppleBeeEntity.class, DataSerializers.BYTE);
  private UUID targetPlayer;
  private float currentPitch;
  private float lastPitch;
  private int timeSinceSting;
  private int underWaterTicks;
  private float attackDamage = -1;

  public AppleBeeEntity(EntityType<? extends AnimalEntity> type, World world) {
    super(type, world);
    this.lookController = new BeeLookController(this);
    this.moveController = new FlyingMovementController(this, 20, true);
    this.setPathPriority(PathNodeType.WATER, -1.0F);
    this.setPathPriority(PathNodeType.COCOA, -1.0F);
    this.setPathPriority(PathNodeType.FENCE, -1.0F);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new AppleBeeEntity.StingGoal(this, 1.4D, true));
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(8, new AppleBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
    this.targetSelector.addGoal(1, (new AppleBeeEntity.AngerGoal(this)).setCallsForHelp());
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (pos) -> Math.abs(pos.posY - this.posY) <= 4.0D));
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(multipleByteTracker, (byte) 0);
    this.dataManager.register(anger, 0);
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getBlockPathWeight(BlockPos pos, IWorldReader world) {
    return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  public void tick() {
    super.tick();
    this.updateBodyPitch();
  }

  @OnlyIn(Dist.CLIENT)
  public float getBodyPitch(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastPitch, this.currentPitch);
  }

  private void updateBodyPitch() {
    this.lastPitch = this.currentPitch;
    if (this.isNearTarget()) {
      this.currentPitch = Math.min(1.0F, this.currentPitch + 0.2F);
    } else {
      this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
    }

  }

  @Override
  public void setRevengeTarget(@Nullable LivingEntity target) {
    super.setRevengeTarget(target);
    if (target != null) {
      this.targetPlayer = target.getUniqueID();
    }
  }

  @Override
  protected void updateAITasks() {
    boolean flag = this.hasStung();
    if (this.isInWaterOrBubbleColumn()) {
      ++this.underWaterTicks;
    } else {
      this.underWaterTicks = 0;
    }

    if (this.underWaterTicks > 20) {
      this.attackEntityFrom(DamageSource.DROWN, 1.0F);
    }

    if (flag) {
      ++this.timeSinceSting;
      if (ConfigManager.getStingKills() && this.timeSinceSting % 5 == 0 && this.rand.nextInt(MathHelper.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
        this.attackEntityFrom(DamageSource.GENERIC, this.getHealth());
      }
    }

    if (this.isAngry()) {
      int i = this.getAnger();
      this.setAnger(i - 1);
      LivingEntity livingentity = this.getAttackTarget();
      if (ConfigManager.getAlwaysAngry() || (i == 0 && livingentity != null)) {
        this.setBeeAttacker(livingentity);
      }
    }
  }

  @Override
  public void livingTick() {
    super.livingTick();
    if (!this.world.isRemote) {
      boolean nearby = this.isAngry() && !this.hasStung() && this.getAttackTarget() != null && this.getAttackTarget().getDistanceSq(this) < 4.0D;
      this.setNearTarget(nearby);
    }
  }

  public boolean hasStung() {
    return this.getBeeFlag(4);
  }

  private void setHasStung(boolean p_226449_1_) {
    this.setBeeFlag(4, p_226449_1_);
  }

  private boolean isNearTarget() {
    return this.getBeeFlag(2);
  }

  private void setNearTarget(boolean nearby) {
    this.setBeeFlag(2, nearby);
  }

  private void setBeeFlag(int value, boolean flag) {
    if (flag) {
      this.dataManager.set(multipleByteTracker, (byte) (this.dataManager.get(multipleByteTracker) | value));
    } else {
      this.dataManager.set(multipleByteTracker, (byte) (this.dataManager.get(multipleByteTracker) & ~value));
    }
  }

  private boolean getBeeFlag(int value) {
    return (this.dataManager.get(multipleByteTracker) & value) != 0;
  }

  @Override
  protected void registerAttributes() {
    super.registerAttributes();
    this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
    this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.12D);
    this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.12D);
    this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
  }

  @Override
  protected PathNavigator createNavigator(World world) {
    FlyingPathNavigator navigator = new FlyingPathNavigator(this, world) {
      @Override
      @SuppressWarnings("deprecation")
      public boolean canEntityStandOnPos(BlockPos pos) {
        return !this.world.getBlockState(pos.down()).isAir();
      }
    };
    navigator.setCanOpenDoors(true);
    navigator.setCanSwim(false);
    navigator.setCanEnterDoors(true);
    return navigator;
  }

  @Override
  public boolean isBreedingItem(ItemStack stack) {
    return false;
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
  protected float getStandingEyeHeight(Pose pose, EntitySize size) {
    return this.isChild() ? size.height * 0.5F : size.height * 0.5F;
  }

  @Override
  public boolean onLivingFall(float p_225503_1_, float p_225503_2_) {
    return false;
  }

  @Override
  protected void updateFallState(double distance, boolean flag, BlockState pos1, BlockPos pos2) {
  }

  @Override
  protected boolean makeFlySound() {
    return true;
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
    this.setHasStung(tag.getBoolean("HasStung"));
    String hurtBy = tag.getString("HurtBy");
    if (!hurtBy.isEmpty()) {
      this.targetPlayer = UUID.fromString(hurtBy);
      PlayerEntity player = this.world.getPlayerByUuid(this.targetPlayer);
      this.setRevengeTarget(player);
      if (player != null) {
        this.attackingPlayer = player;
        this.recentlyHit = this.getRevengeTimer();
      }
    }
    if (tag.contains("attackDamage", Constants.NBT.TAG_INT)) {
      attackDamage = (float) tag.getInt("attackDamage");
    } else if (tag.contains("attackDamage", Constants.NBT.TAG_FLOAT)) {
      attackDamage = tag.getFloat("attackDamage");
    }
    this.setAnger(tag.getInt("Anger"));
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    tag.putBoolean("HasStung", this.hasStung());
    if (this.targetPlayer != null) {
      tag.putString("HurtBy", this.targetPlayer.toString());
    } else {
      tag.putString("HurtBy", "");
    }
    tag.putFloat("attackDamage", attackDamage);
    tag.putInt("Anger", this.getAnger());
  }

  private boolean setBeeAttacker(Entity attacker) {
    this.setAnger(400 + this.rand.nextInt(400));
    if (attacker instanceof LivingEntity) {
      this.setRevengeTarget((LivingEntity) attacker);
    }

    return true;
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (this.isInvulnerableTo(source)) {
      return false;
    } else {
      Entity attacker = source.getTrueSource();
      if (attacker instanceof AppleBeeEntity) {
        return false;
      }
      if (!this.world.isRemote && attacker instanceof PlayerEntity && !((PlayerEntity) attacker).isCreative() && this.canEntityBeSeen(attacker) && !this.isAIDisabled()) {
        this.setBeeAttacker(attacker);
      }

      return super.attackEntityFrom(source, amount);
    }
  }

  @Override
  public CreatureAttribute getCreatureAttribute() {
    return CreatureAttribute.ARTHROPOD;
  }

  @Override
  protected void handleFluidJump(Tag<Fluid> fluid) {
    this.setMotion(this.getMotion().add(0.0D, 0.01D, 0.0D));
  }

  public boolean isAngry() {
    return ConfigManager.getAlwaysAngry() || this.getAnger() > 0;
  }

  private int getAnger() {
    if (ConfigManager.getAlwaysAngry()) {
      return 9999;
    }
    return this.dataManager.get(anger);
  }

  private void setAnger(int angerTime) {
    if (!ConfigManager.getAlwaysAngry()) {
      this.dataManager.set(anger, angerTime);
    }
  }

  @Nullable
  @Override
  public AgeableEntity createChild(AgeableEntity ageableEntity) {
    return null;
  }

  class StingGoal extends MeleeAttackGoal {
    StingGoal(CreatureEntity entity, double speed, boolean longMemory) {
      super(entity, speed, longMemory);
    }
  }

  class BeeLookController extends LookController {
    BeeLookController(MobEntity entity) {
      super(entity);
    }

    @Override
    protected boolean func_220680_b() {
      return true;
    }
  }

  class WanderGoal extends Goal {
    WanderGoal() {
      this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
      return AppleBeeEntity.this.navigator.noPath() && AppleBeeEntity.this.rand.nextInt(10) == 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
      return AppleBeeEntity.this.navigator.func_226337_n_();
    }

    @Override
    public void startExecuting() {
      Vec3d pos = this.getRandomLocation();
      if (pos != null) {
        AppleBeeEntity.this.navigator.setPath(AppleBeeEntity.this.navigator.getPathToPos(new BlockPos(pos), 1), 1.0D);
      }

    }

    @Nullable
    private Vec3d getRandomLocation() {
      Vec3d lookVec = AppleBeeEntity.this.getLook(0.0F);

      Vec3d target = RandomPositionGenerator.findAirTarget(AppleBeeEntity.this, 8, 7, lookVec, (float) (Math.PI / 2), 2, 1);
      return target != null ? target : RandomPositionGenerator.findGroundTarget(AppleBeeEntity.this, 8, 4, -2, lookVec, Math.PI / 2);
    }
  }

  class AngerGoal extends HurtByTargetGoal {
    AngerGoal(AppleBeeEntity bee) {
      super(bee);
    }

    @Override
    protected void setAttackTarget(MobEntity bee, LivingEntity target) {
      if (bee instanceof AppleBeeEntity && this.goalOwner.canEntityBeSeen(target) && ((AppleBeeEntity) bee).setBeeAttacker(target)) {
        bee.setAttackTarget(target);
      }
    }
  }
}
