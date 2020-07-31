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
import noobanidus.mods.carrierbees.entities.projectiles.BombEntity;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.world.BeeExplosion;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class BombleBeeEntity extends AnimalEntity implements IFlyingAnimal, IAppleBee {
  private static final DataParameter<Boolean> nearTarget = EntityDataManager.createKey(BombleBeeEntity.class, DataSerializers.BOOLEAN);
  private static final DataParameter<Integer> anger = EntityDataManager.createKey(BombleBeeEntity.class, DataSerializers.VARINT);
  private UUID targetPlayer;
  private float currentPitch;
  private float lastPitch;
  private int ticksInsideWater;
  public float explosionDamage;
  public float explosionSize;

  public BombleBeeEntity(EntityType<? extends BombleBeeEntity> type, World world) {
    super(type, world);
    this.moveController = new FlyingMovementController(this, 20, true);
    this.lookController = new BombleBeeEntity.BeeLookController(this);
    this.setPathPriority(PathNodeType.WATER, -1.0F);
    this.setPathPriority(PathNodeType.COCOA, -1.0F);
    this.setPathPriority(PathNodeType.FENCE, -1.0F);
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(nearTarget, false);
    this.dataManager.register(anger, 0);
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getBlockPathWeight(BlockPos pos, IWorldReader world) {
    return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0f, 6.5D, 1.8D));
    this.goalSelector.addGoal(1, new BombProjectileAttackGoal(this));
    this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 12.0F));
    this.goalSelector.addGoal(8, new BombleBeeEntity.WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
    this.targetSelector.addGoal(1, (new BombleBeeEntity.AngerGoal(this)).setCallsForHelp());
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 12, true, false, (pos) -> Math.abs(pos.posY - this.posY) <= 12.0D));
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    if (this.targetPlayer != null) {
      tag.putString("HurtBy", this.targetPlayer.toString());
    } else {
      tag.putString("HurtBy", "");
    }
    tag.putFloat("explosionSize", explosionSize);
    tag.putFloat("explosionDamage", explosionDamage);
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
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
    if (tag.contains("explosionSize", Constants.NBT.TAG_INT)) {
      explosionSize = (float) tag.getInt("explosionSize");
    } else if (tag.contains("explosionSize", Constants.NBT.TAG_FLOAT)) {
      explosionSize = tag.getFloat("explosionSize");
    } else {
      explosionSize = 1.5f;
    }
    if (tag.contains("explosionDamage", Constants.NBT.TAG_INT)) {
      explosionDamage = (float) tag.getInt("explosionDamage");
    } else if (tag.contains("explosionDamage", Constants.NBT.TAG_FLOAT)) {
      explosionDamage = tag.getFloat("explosionDamage");
    } else {
      explosionDamage = 3.5f;
    }
  }

  @Override
  public boolean attackEntityAsMob(Entity target) {
    return false;
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
    if (this.isInWaterOrBubbleColumn()) {
      ++this.ticksInsideWater;
    } else {
      this.ticksInsideWater = 0;
    }

    if (this.ticksInsideWater > 20) {
      this.attackEntityFrom(DamageSource.DROWN, 1.0F);
    }

    LivingEntity target = this.getAttackTarget();
    if (target != null) {
      this.setBeeAttacker(target);
    }
  }

  @Override
  public void livingTick() {
    super.livingTick();
    if (!this.world.isRemote) {
      boolean nearby = this.getAttackTarget() != null && this.getAttackTarget().getDistanceSq(this) < 4.0D;
      this.setNearTarget(nearby);
    }
  }

  private void setNearTarget(boolean nearby) {
    this.dataManager.set(nearTarget, nearby);
  }

  private boolean isNearTarget() {
    return this.dataManager.get(nearTarget);
  }

  @Override
  public void onDeath(DamageSource death) {
    super.onDeath(death);
    BeeExplosion.createExplosion(this.world, this, this.posX, this.getPosYHeight(0.0625D), this.posZ);
  }

  @Override
  protected void registerAttributes() {
    super.registerAttributes();
    this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(2.52D);
    this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(2.53D);
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
  protected void playStepSound(BlockPos pos, BlockState state) {
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
  public BombleBeeEntity createChild(AgeableEntity entity) {
    return ModEntities.BOMBLE_BEE.get().create(entity.world);
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
      if (attacker instanceof IAppleBee) {
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
      return BombleBeeEntity.this.navigator.noPath() && BombleBeeEntity.this.rand.nextInt(40) == 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
      return BombleBeeEntity.this.navigator.func_226337_n_();
    }

    @Override
    public void startExecuting() {
      Vec3d pos = this.getRandomLocation();
      if (pos != null) {
        BombleBeeEntity.this.navigator.setPath(BombleBeeEntity.this.navigator.getPathToPos(new BlockPos(pos), 1), 1.0D);
      }

    }

    @Nullable
    private Vec3d getRandomLocation() {
      Vec3d lookVec = BombleBeeEntity.this.getLook(0.0F);

      Vec3d target = RandomPositionGenerator.findAirTarget(BombleBeeEntity.this, 8, 7, lookVec, 1.5707964F, 2, 1);
      return target != null ? target : RandomPositionGenerator.findGroundTarget(BombleBeeEntity.this, 8, 4, -2, lookVec, Math.PI / 2);
    }
  }

  class AngerGoal extends HurtByTargetGoal {
    AngerGoal(BombleBeeEntity bee) {
      super(bee);
    }

    @Override
    protected void setAttackTarget(MobEntity bee, LivingEntity target) {
      if (bee instanceof BombleBeeEntity && this.goalOwner.canEntityBeSeen(target) && ((BombleBeeEntity) bee).setBeeAttacker(target)) {
        bee.setAttackTarget(target);
      }
    }
  }

  static class BombProjectileAttackGoal extends Goal {
    private final BombleBeeEntity parentEntity;
    public int attackTimer;

    public BombProjectileAttackGoal(BombleBeeEntity bee) {
      this.parentEntity = bee;
    }

    @Override
    public boolean shouldExecute() {
      return this.parentEntity.getAttackTarget() != null;
    }

    @Override
    public void startExecuting() {
      this.attackTimer = 0;
    }

    @Override
    public void resetTask() {
    }

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
          double d2 = livingentity.posX - this.parentEntity.posX;
          double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + this.parentEntity.getPosYHeight(0.5D));
          double d4 = livingentity.posZ - this.parentEntity.posZ;
          BombEntity bomb = new BombEntity(this.parentEntity, d2, d3, d4, world);
          bomb.setPosition(this.parentEntity.posX, this.parentEntity.getPosYHeight(0.5D) + 0.5D, bomb.posZ);
          world.addEntity(bomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }
}
