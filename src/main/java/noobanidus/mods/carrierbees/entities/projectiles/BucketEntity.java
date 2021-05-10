package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.entity.Entity;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModParticles;
import noobanidus.mods.carrierbees.init.ModSounds;

import java.util.List;
import java.util.stream.Collectors;

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
    super.tick();

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
    return ModParticles.AIR_BUBBLE.get();
  }

  @Override
  protected boolean isFireballFiery() {
    return false;
  }

  @Override
  protected void onImpact(RayTraceResult ray) {
    super.onImpact(ray);
    if (!world.isRemote()) {
      if (ray.getType() == RayTraceResult.Type.ENTITY) {
      }
    }
    if (!world.isRemote) {
      this.remove();
    }
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
