package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModParticles;

import javax.annotation.Nullable;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class BucketEntity extends DamagingProjectileEntity implements IEntityAdditionalSpawnData, IRendersAsItem {
  private static ItemStack BUCKET = new ItemStack(Items.WATER_BUCKET);

  public BucketEntity(EntityType<? extends DamagingProjectileEntity> type, LivingEntity parent, double aX, double aY, double aZ, World world) {
    super(type, parent, aX, aY, aZ, world);
  }

  public BucketEntity(EntityType<? extends BucketEntity> type, World world) {
    super(type, world);
  }

  @Override
  public void tick() {
    if (!world.isRemote && world.getBlockState(getPosition()).getBlock() == Blocks.FIRE) {
      removeFire(world, getPosition());
      this.remove();
    }

    super.tick();

    for (int i = 0; i < 8; i++) {
      world.addParticle(ParticleTypes.SPLASH, getPosX() + world.rand.nextDouble() - 0.5, getPosY() + world.rand.nextFloat() - 0.5, getPosZ() + world.rand.nextDouble() - 0.5, accelerationX, accelerationY, accelerationZ);
    }

    if (!world.isRemote && this.ticksExisted > 30 * 20) {
      this.remove();
    }
  }

  public BucketEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    this(ModEntities.BUCKET_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    return BUCKET;
  }

  @Override
  protected IParticleData getParticle() {
    return ParticleTypes.SPLASH;
  }

  @Override
  protected boolean isFireballFiery() {
    return false;
  }

  @Nullable
  private BlockPos firePosition(World world, BlockPos pos) {
    for (BlockPos possible : BlockPos.getAllInBoxMutable(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2)) {
      if (world.getBlockState(possible).getBlock() == Blocks.FIRE) {
        if (!world.getBlockState(possible.down()).isFireSource(world, possible.down(), Direction.UP)) {
          return possible.toImmutable();
        }
      }
    }
    return null;
  }

  private void removeFire(World world, BlockPos firePos) {
    world.removeBlock(firePos, false);
    world.playSound(null, firePos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 1, 1);
    for (int i = 0; i < 5; i++) {
      world.addParticle(ParticleTypes.SPLASH, firePos.getX() + 0.5 + world.rand.nextFloat() - 0.5, firePos.getY() + 0.5 + world.rand.nextFloat() - 0.5, firePos.getZ() + 0.5 + world.rand.nextFloat() - 0.5, 0, 0, 0);
    }
  }

  @Override
  protected void onImpact(RayTraceResult ray) {
    super.onImpact(ray);
    if (!world.isRemote()) {
      if (ray.getType() == RayTraceResult.Type.BLOCK) {
        BlockRayTraceResult blockTrace = (BlockRayTraceResult) ray;
        BlockPos firePos = this.firePosition(world, blockTrace.getPos());
        if (firePos != null) {
          removeFire(world, firePos);
        }
      }
    }
    if (!world.isRemote) {
      this.remove();
    }
  }

  @Override
  public void setFire(int seconds) {
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public void writeSpawnData(PacketBuffer buffer) {
    buffer.writeDouble(accelerationX);
    buffer.writeDouble(accelerationY);
    buffer.writeDouble(accelerationZ);
  }

  @Override
  public void readSpawnData(PacketBuffer additionalData) {
    accelerationX = additionalData.readDouble();
    accelerationY = additionalData.readDouble();
    accelerationZ = additionalData.readDouble();
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void handleStatusUpdate(byte id) {
    if (id == 3) {
      for (int i = 0; i < 8; ++i) {
        this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, getItem()), false, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
      }
    } else {
      super.handleStatusUpdate(id);
    }
  }
}
