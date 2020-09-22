package noobanidus.mods.carrierbees.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.BombleBeeEntity;
import noobanidus.mods.carrierbees.entities.IAppleBee;
import noobanidus.mods.carrierbees.entities.projectiles.BombEntity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BeeExplosion extends Explosion {
  private final World world;
  private final double x;
  private final double y;
  private final double z;
  private final Entity exploder;
  private DamageSource damageSource;
  private final Vec3d position;

  public static BeeExplosion createExplosion(World world, Entity entity, double x, double y, double z) {
    final BeeExplosion explosion = new BeeExplosion(world, entity, x, y, z, Mode.BREAK);
    if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;

    explosion.doExplosionA();
    explosion.doExplosionB(true);
    return explosion;
  }

  private BeeExplosion(World world, @Nullable Entity entity, double x, double y, double z, Explosion.Mode mode) {
    super(world, entity, x, y, z, 4f, false, mode);
    this.world = world;
    this.exploder = entity;
    this.x = x;
    this.y = y;
    this.z = z;
    this.damageSource = DamageSource.causeExplosionDamage(this);
    this.position = new Vec3d(this.x, this.y, this.z);
  }

  @Override
  public void doExplosionA() {
    float f3 = ConfigManager.getExplosionSize() * 2f;
    int k1 = MathHelper.floor(this.x - (double) f3 - 1.0D);
    int l1 = MathHelper.floor(this.x + (double) f3 + 1.0D);
    int i2 = MathHelper.floor(this.y - (double) f3 - 1.0D);
    int i1 = MathHelper.floor(this.y + (double) f3 + 1.0D);
    int j2 = MathHelper.floor(this.z - (double) f3 - 1.0D);
    int j1 = MathHelper.floor(this.z + (double) f3 + 1.0D);
    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
    ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);

    for (Entity entity : list) {
      if (entity.isImmuneToExplosions() || entity instanceof BombEntity || entity instanceof AppleBeeEntity) {
        continue;
      }
      if (entity instanceof LivingEntity) {
        entity.attackEntityFrom(this.getDamageSource(), ConfigManager.getExplosionDamage());
      }
    }
  }


  @Override
  public void doExplosionB(boolean ignored) {
    if (this.world.isRemote) {
      this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
    }

    if (!(ConfigManager.getExplosionSize() < 2.0F)) {
      this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
    } else {
      this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
    }
  }

  @Override
  public DamageSource getDamageSource() {
    return this.damageSource;
  }

  @Override
  public void setDamageSource(DamageSource source) {
    this.damageSource = source;
  }

  @Override
  @Nullable
  public LivingEntity getExplosivePlacedBy() {
    if (this.exploder == null) {
      return null;
    } else if (this.exploder instanceof TNTEntity) {
      return ((TNTEntity) this.exploder).getTntPlacedBy();
    } else if (this.exploder instanceof LivingEntity) {
      return (LivingEntity) this.exploder;
    } else {
      return this.exploder instanceof DamagingProjectileEntity ? ((DamagingProjectileEntity) this.exploder).shootingEntity : null;
    }
  }

  @Override
  public void clearAffectedBlockPositions() {
  }

  @Override
  public List<BlockPos> getAffectedBlockPositions() {
    return Collections.emptyList();
  }

  @Override
  public Vec3d getPosition() {
    return this.position;
  }
}
