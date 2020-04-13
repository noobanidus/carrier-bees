package noobanidus.mods.carrierbees.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import noobanidus.mods.carrierbees.entities.BombleBeeEntity;
import noobanidus.mods.carrierbees.entities.projectiles.BombEntity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BeeExplosion extends Explosion {
  private final boolean causesFire;
  private final Explosion.Mode mode;
  private final Random random = new Random();
  private final World world;
  private final double x;
  private final double y;
  private final double z;
  @Nullable
  private final Entity exploder;
  private final float size;
  private DamageSource damageSource;
  private final List<BlockPos> affectedBlockPositions = Lists.newArrayList();
  private final Map<PlayerEntity, Vec3d> playerKnockbackMap = Maps.newHashMap();
  private final Vec3d position;

  public static BeeExplosion createExplosion(World world, Entity entity, double x, double y, double z, float size, boolean causesFire) {
    final BeeExplosion explosion = new BeeExplosion(world, entity, x, y, z, size, true, Mode.BREAK);
    if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;

    explosion.doExplosionA();
    explosion.doExplosionB(true);
    return explosion;
  }

  public BeeExplosion(World world, @Nullable Entity entity, double x, double y, double z, float size, boolean causesFire, Explosion.Mode mode) {
    super(world, entity, x, y, z, size, causesFire, mode);
    this.world = world;
    this.exploder = entity;
    this.size = size;
    this.x = x;
    this.y = y;
    this.z = z;
    this.causesFire = causesFire;
    this.mode = mode;
    this.damageSource = DamageSource.causeExplosionDamage(this);
    this.position = new Vec3d(this.x, this.y, this.z);
  }

  public static float getBlockDensity(Vec3d pos, Entity entity) {
    AxisAlignedBB axisalignedbb = entity.getBoundingBox();
    double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
    double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
    double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
    double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
    double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
    if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
      int i = 0;
      int j = 0;

      for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
        for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
          for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
            double d5 = MathHelper.lerp((double) f, axisalignedbb.minX, axisalignedbb.maxX);
            double d6 = MathHelper.lerp((double) f1, axisalignedbb.minY, axisalignedbb.maxY);
            double d7 = MathHelper.lerp((double) f2, axisalignedbb.minZ, axisalignedbb.maxZ);
            Vec3d vec3d = new Vec3d(d5 + d3, d6, d7 + d4);
            if (entity.world.rayTraceBlocks(new RayTraceContext(vec3d, pos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, entity)).getType() == RayTraceResult.Type.MISS) {
              ++i;
            }

            ++j;
          }
        }
      }

      return (float) i / (float) j;
    } else {
      return 0.0F;
    }
  }

  @Override
  public void doExplosionA() {
    float f3 = this.size * 2.0F;
    int k1 = MathHelper.floor(this.x - (double) f3 - 1.0D);
    int l1 = MathHelper.floor(this.x + (double) f3 + 1.0D);
    int i2 = MathHelper.floor(this.y - (double) f3 - 1.0D);
    int i1 = MathHelper.floor(this.y + (double) f3 + 1.0D);
    int j2 = MathHelper.floor(this.z - (double) f3 - 1.0D);
    int j1 = MathHelper.floor(this.z + (double) f3 + 1.0D);
    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
    ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
    Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

    for (Entity entity : list) {
      if (!entity.isImmuneToExplosions() && entity instanceof LivingEntity && !(entity instanceof BombleBeeEntity)) {
        double d12 = (double) (MathHelper.sqrt(entity.getDistanceSq(vec3d)) / f3);
        if (d12 <= 1.0D) {
          double d5 = entity.getX() - this.x;
          double d7 = entity.getEyeY() - this.y;
          double d9 = entity.getZ() - this.z;
          double d13 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
          if (d13 != 0.0D) {
            d5 = d5 / d13;
            d7 = d7 / d13;
            d9 = d9 / d13;
            double d14 = (double) getBlockDensity(vec3d, entity);
            double d10 = (1.0D - d12) * d14;
            float dam = (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D));
            entity.attackEntityFrom(this.getDamageSource(), dam);
            double d11 = ProtectionEnchantment.getBlastDamageReduction((LivingEntity) entity, d10);

            entity.setMotion(entity.getMotion().add(d5 * d11, d7 * d11, d9 * d11));
            if (entity instanceof PlayerEntity) {
              PlayerEntity playerentity = (PlayerEntity) entity;
              if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.isFlying)) {
                this.playerKnockbackMap.put(playerentity, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
              }
            }
          }
        }
      }
    }
  }


  @Override
  public void doExplosionB(boolean ignored) {
    if (this.world.isRemote) {
      this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
    }

    if (!(this.size < 2.0F)) {
      this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
    } else {
      this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
    }

    if (this.causesFire) {
      for (BlockPos blockpos2 : this.affectedBlockPositions) {
        if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockpos2).isAir(world, blockpos2) && this.world.getBlockState(blockpos2.down()).isOpaqueCube(this.world, blockpos2.down())) {
          this.world.setBlockState(blockpos2, Blocks.FIRE.getDefaultState());
        }
      }
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
  public Map<PlayerEntity, Vec3d> getPlayerKnockbackMap() {
    return this.playerKnockbackMap;
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
