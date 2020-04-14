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
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.HoneyCombEntity;
import noobanidus.mods.carrierbees.init.ModEntities;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class CarrierBeeEntity extends AnimalEntity implements IFlyingAnimal {
  private static final DataParameter<Byte> multipleByteTracker = EntityDataManager.createKey(CarrierBeeEntity.class, DataSerializers.BYTE);
  private UUID targetPlayer;
  private float currentPitch;
  private float lastPitch;
  private int ticksSinceSting;
  private int ticksInsideWater;
  private List<EffectBuilder> effects = new ArrayList<>();
  public float attackDamage = -1;

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
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getBlockPathWeight(BlockPos pos, IWorldReader world) {
    return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new CarrierBeeEntity.StingGoal(this, 1.4D, true));
    if (ConfigManager.getHoneycombDamage() > 0) {
      this.goalSelector.addGoal(1, new CarrierBeeEntity.HoneycombProjectileAttackGoal(this));
    }
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.addGoal(8, new CarrierBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
    this.targetSelector.addGoal(1, (new CarrierBeeEntity.AngerGoal(this)).setCallsForHelp());
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (pos) -> Math.abs(pos.getY() - this.getY()) <= 4.0D));
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
    if (!effects.isEmpty()) {
      ListNBT list = new ListNBT();
      list.addAll(effects.stream().map(EffectBuilder::asTag).collect(Collectors.toList()));
      tag.put("effects", list);
    }
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
    effects.clear();
    if (tag.contains("effects", Constants.NBT.TAG_LIST)) {
      ListNBT listEffects = tag.getList("effects", Constants.NBT.TAG_COMPOUND);
      for (int i = 0; i < listEffects.size(); i++) {
        CompoundNBT thisTag = listEffects.getCompound(i);
        if (thisTag.contains("d", Constants.NBT.TAG_INT) && thisTag.contains("n", Constants.NBT.TAG_STRING) && thisTag.contains("a", Constants.NBT.TAG_INT)) {
          ResourceLocation rl = new ResourceLocation(thisTag.getString("n"));
          Effect e = ForgeRegistries.POTIONS.getValue(rl);
          if (e == null) {
            CarrierBees.LOG.error("Invalid effect name: '" + rl.toString() + "', full tag: " + thisTag.toString());
          } else {
            int dur = thisTag.getInt("d");
            int amp = thisTag.getInt("a");
            effects.add(new EffectBuilder(e, dur, amp));
          }
        } else {
          CarrierBees.LOG.error("Invalid effect tag: " + thisTag.toString());
        }
      }
    }
  }

  @Override
  public boolean attackEntityAsMob(Entity target) {
    float damage = ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
    if (!hasStung()) {
      boolean attacked = target.attackEntityFrom(DamageSource.sting(this), damage);
      if (attacked) {
        this.applyEnchantments(this, target);
        if (target instanceof LivingEntity) {
          ((LivingEntity) target).setStingerCount(((LivingEntity) target).getStingerCount() + 1);
          if (effects.isEmpty()) {
            Effect e = Effects.POISON;
            int dur = 0;
            if (this.world.getDifficulty() == Difficulty.NORMAL) {
              dur = 10 * 20;
            } else if (this.world.getDifficulty() == Difficulty.HARD) {
              dur = 18 * 20;
            }

            if (dur > 0) {
              ((LivingEntity) target).addPotionEffect(new EffectInstance(e, dur, 0));
            }
          } else {
            ((LivingEntity) target).addPotionEffect(effects.get(rand.nextInt(effects.size())).build());
          }
        }

        this.setHasStung(true);
        this.setAttackTarget(null);
        this.playSound(SoundEvents.field_226128_ac_, 1.0F, 1.0F);
      }

      return attacked;
    }

    if (attackDamage != -1.0f) {
      if (attackDamage == 0) {
        return false;
      } else {
        return target.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);
      }
    }

    return target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
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

    LivingEntity target = this.getAttackTarget();
    if (target != null) {
      this.setBeeAttacker(target);
    }
  }

  @Override
  protected void func_213387_K() {
    super.func_213387_K();
  }

  @Override
  public void livingTick() {
    super.livingTick();
    if (!this.world.isRemote) {
      boolean nearby = !this.hasStung() && this.getAttackTarget() != null && this.getAttackTarget().getDistanceSq(this) < 4.0D;
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

  static class HoneycombProjectileAttackGoal extends Goal {
    private final CarrierBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(CarrierBeeEntity bee) {
      this.parentEntity = bee;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    @Override
    public boolean shouldExecute() {
      return this.parentEntity.getAttackTarget() != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
      this.attackTimer = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
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
          double d2 = livingentity.getX() - this.parentEntity.getX();
          double d3 = livingentity.getBodyY(0.5D) - (0.5D + this.parentEntity.getBodyY(0.5D));
          double d4 = livingentity.getZ() - this.parentEntity.getZ();
          HoneyCombEntity honeycomb = new HoneyCombEntity(this.parentEntity, d2, d3, d4, world);
          honeycomb.setPosition(this.parentEntity.getX(), this.parentEntity.getBodyY(0.5D) + 0.2D, honeycomb.getZ());
          world.addEntity(honeycomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }

  public static class EffectBuilder {
    private Effect effect;
    private int duration;
    private int amplifier;

    public EffectBuilder(Effect effect, int duration, int amplifier) {
      this.effect = effect;
      this.duration = duration;
      this.amplifier = amplifier;
    }

    public EffectInstance build() {
      return new EffectInstance(effect, duration, amplifier);
    }

    public CompoundNBT asTag() {
      CompoundNBT tag = new CompoundNBT();
      tag.putInt("d", duration);
      tag.putInt("a", amplifier);
      tag.putString("n", Objects.requireNonNull(effect.getRegistryName()).toString());
      return tag;
    }
  }
}
