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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;
import noobanidus.mods.carrierbees.init.ModEntities;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(
   value = Dist.CLIENT,
   _interface = IRendersAsItem.class
)
public class HoneyCombEntity extends DamagingProjectileEntity implements IEntityAdditionalSpawnData, IRendersAsItem {
  private static final DataParameter<Float> attackDamage = EntityDataManager.createKey(CarrierBeeEntity.class, DataSerializers.FLOAT);
  private static ItemStack HONEY_COMB = new ItemStack(Items.field_226635_pU_);
  private float attackDamageValue = 3.5f;

  public HoneyCombEntity(EntityType<? extends HoneyCombEntity> type, World world) {
    super(type, world);
  }

  public HoneyCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.HONEY_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  public HoneyCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, float attackDamageValue, World world) {
    super(ModEntities.HONEY_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
    this.attackDamageValue = attackDamageValue;
  }

  @Override
  public ItemStack getItem() {
    return HONEY_COMB;
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    tag.putFloat("damage", dataManager.get(attackDamage));
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
    dataManager.set(attackDamage, tag.getFloat("damage"));
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(attackDamage, attackDamageValue);
    this.dataManager.set(attackDamage, attackDamageValue);
  }

  @Override
  protected IParticleData getParticle() {
    return ParticleTypes.field_229428_ah_;
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
        EntityRayTraceResult ray2 = (EntityRayTraceResult) ray;
        Entity entity = ray2.getEntity();
        if ((entity != this || entity != this.shootingEntity) && entity instanceof LivingEntity) {
          LivingEntity living = (LivingEntity) ray2.getEntity();
          living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 8 * 20));
          living.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity).setMagicDamage(), 2);
          List<LivingEntity> list = this.world.getEntitiesWithinAABBExcludingEntity(living, this.getBoundingBox().grow(4.0D, 2.0D, 4.0D)).stream().filter(o -> o instanceof LivingEntity).map(o -> (LivingEntity) o).collect(Collectors.toList());
          world.addParticle(ParticleTypes.field_229428_ah_, living.posX, living.posY, living.posZ, 0, 0, 0);
          world.playSound(null, this.getPosition(), SoundEvents.field_226136_eQ_, SoundCategory.HOSTILE, 1f, 0.5f);
          for (LivingEntity l : list) {
            if (l == this.shootingEntity) {
              continue;
            }
            l.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 8 * 20));
            l.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity).setMagicDamage(), 2);
            world.addParticle(ParticleTypes.field_229428_ah_, l.posX, l.posY, l.posZ, 0, 0, 0);
          }
        }
      }
    }
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public void writeSpawnData(PacketBuffer buffer) {
    buffer.writeFloat(dataManager.get(attackDamage));
    buffer.writeDouble(accelerationX);
    buffer.writeDouble(accelerationY);
    buffer.writeDouble(accelerationZ);
  }

  @Override
  public void readSpawnData(PacketBuffer additionalData) {
    dataManager.set(attackDamage, additionalData.readFloat());
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
