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
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.init.ModEntities;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class CarrierBeeEntity extends AnimalEntity implements IFlyingAnimal {
  private static final DataParameter<Byte> multipleByteTracker = EntityDataManager.createKey(CarrierBeeEntity.class, DataSerializers.BYTE);
  private static final DataParameter<Integer> anger = EntityDataManager.createKey(CarrierBeeEntity.class, DataSerializers.VARINT);
  private UUID targetPlayer;
  private float currentPitch;
  private float lastPitch;
  private int ticksSinceSting;
  private int ticksInsideWater;

  public CarrierBeeEntity(EntityType<? extends CarrierBeeEntity> type, World world) {
    super(type, world);
    this.moveController = new FlyingMovementController(this, 20, true);
    this.lookController = new CarrierBeeEntity.BeeLookController(this);
    this.setPathPriority(PathNodeType.WATER, -1.0F);
    this.setPathPriority(PathNodeType.COCOA, -1.0F);
    this.setPathPriority(PathNodeType.FENCE, -1.0F);
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
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new CarrierBeeEntity.StingGoal(this, 1.4D, true));
    this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true);
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(ItemTags.field_226159_I_), false));
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(8, new CarrierBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
    this.targetSelector.addGoal(1, (new CarrierBeeEntity.AngerGoal(this)).setCallsForHelp());
    this.targetSelector.addGoal(2, new CarrierBeeEntity.AttackPlayerGoal(this));
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    tag.putBoolean("HasStung", this.hasStung());
    tag.putInt("Anger", this.getAnger());
    if (this.targetPlayer != null) {
      tag.putString("HurtBy", this.targetPlayer.toString());
    } else {
      tag.putString("HurtBy", "");
    }
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
    this.setHasStung(tag.getBoolean("HasStung"));
    this.setAnger(tag.getInt("Anger"));
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
  }

  @Override
  public boolean attackEntityAsMob(Entity target) {
    CompoundNBT data = getPersistentData();
    float damage = ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
    if (!hasStung()) {
      boolean attacked = target.attackEntityFrom(DamageSource.sting(this), damage);
      if (attacked) {
        this.applyEnchantments(this, target);
        if (target instanceof LivingEntity) {
          ((LivingEntity) target).setStingerCount(((LivingEntity) target).getStingerCount() + 1);
          Effect e = Effects.POISON;
          int dur = -1;
          int amp = 0;
          if (data.contains("effects", Constants.NBT.TAG_LIST)) {
            ListNBT effectData = data.getList("effects", Constants.NBT.TAG_COMPOUND);
            if (!effectData.isEmpty()) {
              int index = rand.nextInt(effectData.size());
              CompoundNBT effect = effectData.getCompound(index);
              if (effect.contains("d", Constants.NBT.TAG_INT) && effect.contains("n", Constants.NBT.TAG_STRING) && effect.contains("a", Constants.NBT.TAG_INT)) {
                String name = effect.getString("n");
                Effect potionEffect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(name));
                if (potionEffect == null) {
                  CarrierBees.LOG.error("Effect Data for bee " + this + " contains invalid effect name: " + name + " is not valid. Full data: " + effect.toString());
                } else {
                  dur = effect.getInt("d");
                  amp = effect.getInt("a");
                  e = potionEffect;
                }
              } else {
                CarrierBees.LOG.error("Effect Data for bee " + this + " contains invalid effect: missing duration, name or amplifier: " + effect.toString());
              }
            }
          }
          if (dur == -1) {
            if (this.world.getDifficulty() == Difficulty.NORMAL) {
              dur = 10 * 20;
            } else if (this.world.getDifficulty() == Difficulty.HARD) {
              dur = 18 * 20;
            }
          }

          if (dur > 0) {
            ((LivingEntity) target).addPotionEffect(new EffectInstance(e, dur, amp));
          }
        }

        this.setHasStung(true);
        this.setAttackTarget(null);
        this.playSound(SoundEvents.field_226128_ac_, 1.0F, 1.0F);
      }

      return attacked;
    }

    if (data.contains("attackDamage", Constants.NBT.TAG_FLOAT)) {
      damage = data.getFloat("attackDamage");
    } else if (data.contains("attackDamage", Constants.NBT.TAG_INT)) {
      damage = (float) data.getInt("attackDamage");
    }

    if (damage <= 0.0f) {
      return false;
    }

    return target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
  }

  @Override
  public void tick() {
    super.tick();
    this.updateBodyPitch();
  }

  private void addParticle(World world, double x1, double x2, double z1, double z2, double yy, IParticleData particle) {
    world.addParticle(particle, MathHelper.lerp(world.rand.nextDouble(), x1, x2), yy, MathHelper.lerp(world.rand.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
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
    boolean stung = this.hasStung();
    if (this.isInWaterOrBubbleColumn()) {
      ++this.ticksInsideWater;
    } else {
      this.ticksInsideWater = 0;
    }

    if (this.ticksInsideWater > 20) {
      this.attackEntityFrom(DamageSource.DROWN, 1.0F);
    }

    if (stung) {
      ++this.ticksSinceSting;
      if (this.ticksSinceSting % 5 == 0 && this.rand.nextInt(MathHelper.clamp(1200 - this.ticksSinceSting, 1, 1200)) == 0) {
        this.attackEntityFrom(DamageSource.GENERIC, this.getHealth());
      }
    }

    if (this.isAngry()) {
      int angerLevel = this.getAnger();
      this.setAnger(angerLevel - 1);
      LivingEntity target = this.getAttackTarget();
      if (angerLevel == 0 && target != null) {
        this.setBeeAttacker(target);
      }
    }
  }

  public boolean isAngry() {
    return this.getAnger() > 0;
  }

  private int getAnger() {
    return this.dataManager.get(anger);
  }

  private void setAnger(int anger) {
    this.dataManager.set(CarrierBeeEntity.anger, anger);
  }

  @Override
  protected void func_213387_K() {
    super.func_213387_K();
    //DebugPacketSender.sendBeeDebugData(this);
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
      this.dataManager.set(multipleByteTracker, (byte) ((Byte) this.dataManager.get(multipleByteTracker) & ~value));
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
    this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.72D);
    this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
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
    return SoundEvents.field_226125_Z_;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.field_226124_Y_;
  }

  @Override
  protected float getSoundVolume() {
    return 0.4F;
  }

  @Override
  public CarrierBeeEntity createChild(AgeableEntity entity) {
    return ModEntities.CARRIER_BEE.get().create(entity.world);
  }

  @Override
  protected float getStandingEyeHeight(Pose pose, EntitySize size) {
    return this.isChild() ? size.height * 0.5F : size.height * 0.5F;
  }

  @Override
  public boolean handleFallDamage(float val1, float val2) {
    return false;
  }

  @Override
  protected void updateFallState(double distance, boolean flag, BlockState pos1, BlockPos pos2) {
  }

  @Override
  protected boolean makeFlySound() {
    return true;
  }

  public boolean setBeeAttacker(Entity target) {
    this.setAnger(400 + this.rand.nextInt(400));
    if (target instanceof LivingEntity) {
      this.setRevengeTarget((LivingEntity) target);
    }

    return true;
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (this.isInvulnerableTo(source)) {
      return false;
    } else {
      Entity attacker = source.getTrueSource();
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

  private boolean isWithinDistance(BlockPos pos, int distance) {
    return pos.withinDistance(new BlockPos(this), (double) distance);
  }

  class StingGoal extends MeleeAttackGoal {
    StingGoal(CreatureEntity entity, double speed, boolean longMemory) {
      super(entity, speed, longMemory);
    }

    @Override
    public boolean shouldExecute() {
      return super.shouldExecute() && CarrierBeeEntity.this.isAngry() && !CarrierBeeEntity.this.hasStung();
    }

    @Override
    public boolean shouldContinueExecuting() {
      return super.shouldContinueExecuting() && CarrierBeeEntity.this.isAngry() && !CarrierBeeEntity.this.hasStung();
    }
  }

  class BeeLookController extends LookController {
    BeeLookController(MobEntity entity) {
      super(entity);
    }

    @Override
    public void tick() {
      if (!CarrierBeeEntity.this.isAngry()) {
        super.tick();
      }
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
      return CarrierBeeEntity.this.navigator.noPath() && CarrierBeeEntity.this.rand.nextInt(10) == 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
      return CarrierBeeEntity.this.navigator.func_226337_n_();
    }

    @Override
    public void startExecuting() {
      Vec3d pos = this.getRandomLocation();
      if (pos != null) {
        CarrierBeeEntity.this.navigator.setPath(CarrierBeeEntity.this.navigator.getPathToPos(new BlockPos(pos), 1), 1.0D);
      }

    }

    @Nullable
    private Vec3d getRandomLocation() {
      Vec3d lookVec = CarrierBeeEntity.this.getLook(0.0F);

      Vec3d target = RandomPositionGenerator.findAirTarget(CarrierBeeEntity.this, 8, 7, lookVec, 1.5707964F, 2, 1);
      return target != null ? target : RandomPositionGenerator.findGroundTarget(CarrierBeeEntity.this, 8, 4, -2, lookVec, Math.PI / 2);
    }
  }

  static class AttackPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
    AttackPlayerGoal(CarrierBeeEntity bee) {
      super(bee, PlayerEntity.class, true);
    }

    @Override
    public boolean shouldExecute() {
      return this.canSting() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
      boolean canSting = this.canSting();
      if (canSting && this.goalOwner.getAttackTarget() != null) {
        return super.shouldContinueExecuting();
      } else {
        this.target = null;
        return false;
      }
    }

    private boolean canSting() {
      CarrierBeeEntity bee = (CarrierBeeEntity) this.goalOwner;
      return bee.isAngry() && !bee.hasStung();
    }
  }

  class AngerGoal extends HurtByTargetGoal {
    AngerGoal(CarrierBeeEntity bee) {
      super(bee);
    }

    @Override
    protected void setAttackTarget(MobEntity bee, LivingEntity target) {
      if (bee instanceof CarrierBeeEntity && this.goalOwner.canEntityBeSeen(target) && ((CarrierBeeEntity) bee).setBeeAttacker(target)) {
        bee.setAttackTarget(target);
      }
    }
  }
}
