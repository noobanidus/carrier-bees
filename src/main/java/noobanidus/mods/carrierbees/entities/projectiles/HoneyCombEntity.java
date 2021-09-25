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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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
import noobanidus.mods.carrierbees.entities.IAppleBee;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModSounds;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class HoneyCombEntity extends DamagingProjectileEntity implements IEntityAdditionalSpawnData, IRendersAsItem {
  private static ItemStack HONEY_COMB = new ItemStack(Items.HONEYCOMB);

  @Nullable
  public EffectInstance getInstance() {
    return new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 2, ConfigManager.getHoneycombSlow());
  }

  @Override
  public void tick() {
    super.tick();

    if (!level.isClientSide && this.tickCount > 30 * 20) {
      this.remove();
    }
  }

  public HoneyCombEntity(EntityType<? extends DamagingProjectileEntity> type, LivingEntity parent, double aX, double aY, double aZ, World world) {
    super(type, parent, aX, aY, aZ, world);
  }

  public HoneyCombEntity(EntityType<? extends HoneyCombEntity> type, World world) {
    super(type, world);
  }

  public HoneyCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    this(ModEntities.HONEY_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    return HONEY_COMB;
  }

  @Override
  protected IParticleData getTrailParticle() {
    return ParticleTypes.FALLING_HONEY;
  }

  @Override
  protected boolean shouldBurn() {
    return false;
  }

  @Override
  protected void onHit(RayTraceResult ray) {
    super.onHit(ray);
    if (!level.isClientSide()) {
      if (ray.getType() == RayTraceResult.Type.ENTITY) {
        EntityRayTraceResult ray2 = (EntityRayTraceResult) ray;
        Entity entity = ray2.getEntity();
        Entity shootingEntity = this.getOwner();
        if ((entity != this || entity != shootingEntity) && entity instanceof LivingEntity && !(entity instanceof IAppleBee) && !(entity instanceof BeeEntity)) {
          LivingEntity living = (LivingEntity) ray2.getEntity();
          EffectInstance instance = getInstance();
          if (instance != null) {
            living.addEffect(instance);
          }
          DamageSource source;
          if (shootingEntity instanceof LivingEntity) {
            source = DamageSource.mobAttack((LivingEntity) shootingEntity).setMagic();
          } else {
            source = DamageSource.MAGIC;
          }
          float damage = ConfigManager.getHoneycombDamage(shootingEntity);
          if (damage > 0) {
            living.hurt(source, damage);
          }
          double val = ConfigManager.getHoneycombSize();
          List<LivingEntity> list = this.level.getEntities(living, this.getBoundingBox().inflate(val, val, val)).stream().filter(o -> o instanceof LivingEntity).map(o -> (LivingEntity) o).collect(Collectors.toList());
          level.addParticle(ParticleTypes.FALLING_HONEY, living.getX(), living.getY(), living.getZ(), 0, 0, 0);
          level.playSound(null, this.blockPosition(), ModSounds.SPLOOSH.get(), SoundCategory.HOSTILE, 1f, 0.5f);
          for (LivingEntity l : list) {
            if (l == shootingEntity || l instanceof IAppleBee || l instanceof BeeEntity) {
              continue;
            }
            if (instance != null) {
              l.addEffect(instance);
            }
            if (damage > 0) {
              l.hurt(source, damage);
            }
            level.addParticle(ParticleTypes.FALLING_HONEY, l.getX(), l.getY(), l.getZ(), 0, 0, 0);
          }
        }
      }
    }
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
