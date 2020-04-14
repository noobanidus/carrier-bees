package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.world.BeeExplosion;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class BombEntity extends DamagingProjectileEntity implements IEntityAdditionalSpawnData, IRendersAsItem {
  public static ItemStack BOMB = new ItemStack(Items.TNT);
  private float size = 1.5f;
  private float damage = 6f;

  public BombEntity(EntityType<? extends BombEntity> type, World world) {
    super(type, world);
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    tag.putFloat("damage", damage);
    tag.putFloat("size", size);
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
    damage = tag.getFloat("damage");
    size = tag.getFloat("size");
  }

  public BombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, float size, float damage, World world) {
    super(ModEntities.BOMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
    this.size = size;
    this.damage = damage;
  }

  @Override
  protected IParticleData getParticle() {
    return new ItemParticleData(ParticleTypes.ITEM, getItem());
  }

  @Override
  public ItemStack getItem() {
    return BOMB;
  }

  @Override
  protected boolean isFireballFiery() {
    return false;
  }

  @Override
  protected void onImpact(RayTraceResult ray) {
    super.onImpact(ray);
    if (!world.isRemote) {
      if (ray instanceof EntityRayTraceResult) {
        EntityRayTraceResult eray = (EntityRayTraceResult) ray;
        Entity entity = eray.getEntity();
        if (entity != this && entity != this.shootingEntity) {
          entity.attackEntityFrom(DamageSource.causeExplosionDamage(this.shootingEntity), damage);
        }
      }
    }
    BeeExplosion.createExplosion(this.world, this, this.getX(), this.getBodyY(0.0625D), this.getZ(), size, damage);
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
    buffer.writeFloat(damage);
    buffer.writeFloat(size);
    buffer.writeDouble(accelerationX);
    buffer.writeDouble(accelerationY);
    buffer.writeDouble(accelerationZ);
  }

  @Override
  public void readSpawnData(PacketBuffer additionalData) {
    damage = additionalData.readFloat();
    size = additionalData.readFloat();
    accelerationX = additionalData.readDouble();
    accelerationY = additionalData.readDouble();
    accelerationZ = additionalData.readDouble();
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void handleStatusUpdate(byte id) {
    if (id == 3) {
      for (int i = 0; i < 8; ++i) {
        this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, getItem()), false, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
      }
    } else {
      super.handleStatusUpdate(id);
    }
  }
}
