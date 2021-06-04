package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.IAppleBee;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.world.BeeExplosion;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class BombEntity extends DamagingProjectileEntity implements IEntityAdditionalSpawnData, IRendersAsItem {
  public static ItemStack BOMB = new ItemStack(Items.TNT);

  public BombEntity(EntityType<? extends BombEntity> type, World world) {
    super(type, world);
  }

  public BombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.BOMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  protected IParticleData getParticle() {
    return new ItemParticleData(ParticleTypes.ITEM, getItem());
  }

  @Override
  public void tick() {
    super.tick();

    if (!world.isRemote && this.ticksExisted > 30 * 20) {
      this.remove();
    }
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
        if (entity != this && entity != this.func_234616_v_() && !(entity instanceof IAppleBee) && !(entity instanceof BeeEntity)) {
          entity.attackEntityFrom(DamageSource.GENERIC, ConfigManager.getExplosionDamage());
        }
      }
    }
    BeeExplosion.createExplosion(this.world, this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ());
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
