package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.IAppleBee;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModItems;
import noobanidus.mods.carrierbees.init.ModParticles;
import noobanidus.mods.carrierbees.init.ModSounds;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class BoogerCombEntity extends HoneyCombEntity {
  private static ItemStack BOOGER_COMB = ItemStack.EMPTY;

  public BoogerCombEntity(EntityType<? extends BoogerCombEntity> type, World world) {
    super(type, world);
  }

  public BoogerCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.BOOGER_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (BOOGER_COMB.isEmpty()) {
      BOOGER_COMB = new ItemStack(ModItems.BOOGERCOMB.get());
    }
    return BOOGER_COMB;
  }

  @Override
  protected IParticleData getTrailParticle() {
    return ModParticles.FALLING_BOOGER.get();
  }

  @Override
  protected void onHit(RayTraceResult ray) {
    RayTraceResult.Type raytraceresult$type = ray.getType();
    if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
      this.onHitEntity((EntityRayTraceResult) ray);
    } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
      this.onHitBlock((BlockRayTraceResult) ray);
    }
    if (!level.isClientSide()) {
      if (ray.getType() == RayTraceResult.Type.ENTITY) {
        EntityRayTraceResult ray2 = (EntityRayTraceResult) ray;
        Entity entity = ray2.getEntity();
        Entity shootingEntity = this.getOwner();
        if ((entity != this || entity != shootingEntity) && entity instanceof LivingEntity && !(entity instanceof IAppleBee) && !(entity instanceof BeeEntity)) {
          LivingEntity living = (LivingEntity) ray2.getEntity();
          DamageSource source;
          if (shootingEntity instanceof LivingEntity) {
            source = DamageSource.mobAttack((LivingEntity) shootingEntity).setMagic();
          } else {
            source = DamageSource.MAGIC;
          }
          living.hurt(source, ConfigManager.getHoneycombDamage(shootingEntity));
          double val = ConfigManager.getHoneycombSize();
          List<LivingEntity> list = this.level.getEntities(living, this.getBoundingBox().inflate(val, val, val)).stream().filter(o -> o instanceof LivingEntity).map(o -> (LivingEntity) o).collect(Collectors.toList());
          level.addParticle(ModParticles.FALLING_BOOGER.get(), living.getX(), living.getY(), living.getZ(), 0, 0, 0);
          level.playSound(null, this.blockPosition(), ModSounds.SPLOOSH.get(), SoundCategory.HOSTILE, 1f, 0.5f);
          for (LivingEntity l : list) {
            if (l == shootingEntity || l instanceof AppleBeeEntity || l instanceof BeeEntity) {
              continue;
            }
            l.hurt(source, ConfigManager.getHoneycombDamage(shootingEntity));
            level.addParticle(ModParticles.FALLING_BOOGER.get(), l.getX(), l.getY(), l.getZ(), 0, 0, 0);
          }
        }
      }
    }
    if (!level.isClientSide) {
      this.remove();
    }
  }

  @Override
  public EffectInstance getInstance() {
    return null;
  }
}
