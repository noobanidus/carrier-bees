package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
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
  protected IParticleData getTrailParticle() {
    return new ItemParticleData(ParticleTypes.ITEM, getItem());
  }

  @Override
  public void tick() {
    super.tick();

    if (!level.isClientSide && this.tickCount > 30 * 20) {
      this.remove();
    }
  }

  @Override
  public ItemStack getItem() {
    return BOMB;
  }

  @Override
  protected boolean shouldBurn() {
    return false;
  }

  @Override
  protected void onHit(RayTraceResult ray) {
    super.onHit(ray);
    if (!level.isClientSide) {
      if (ray instanceof EntityRayTraceResult) {
        EntityRayTraceResult eray = (EntityRayTraceResult) ray;
        Entity entity = eray.getEntity();
        if (entity != this && entity != this.getOwner() && !(entity instanceof IAppleBee) && !(entity instanceof BeeEntity) && !(entity instanceof ItemEntity)) {
          entity.hurt(DamageSource.GENERIC, ConfigManager.getExplosionDamage());
        }
      }
    }
    BeeExplosion.createExplosion(this.level, this, this.getX(), this.getY(0.0625D), this.getZ());
    if (!level.isClientSide) {
      this.remove();
    }
  }

  @Override
  public IPacket<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public void writeSpawnData(PacketBuffer buffer) {
    buffer.writeDouble(xPower);
    buffer.writeDouble(yPower);
    buffer.writeDouble(zPower);
  }

  @Override
  public void readSpawnData(PacketBuffer additionalData) {
    xPower = additionalData.readDouble();
    yPower = additionalData.readDouble();
    zPower = additionalData.readDouble();
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void handleEntityEvent(byte id) {
    if (id == 3) {
      for (int i = 0; i < 8; ++i) {
        this.level.addParticle(new ItemParticleData(ParticleTypes.ITEM, getItem()), false, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
      }
    } else {
      super.handleEntityEvent(id);
    }
  }
}
