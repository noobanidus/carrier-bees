package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
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
import noobanidus.mods.carrierbees.init.ModSounds;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class ThimbleCombEntity extends HoneyCombEntity {
  private static ItemStack THIMBLE_COMB = ItemStack.EMPTY;

  public ThimbleCombEntity(EntityType<? extends ThimbleCombEntity> type, World world) {
    super(type, world);
  }

  public ThimbleCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.THIMBLE_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (THIMBLE_COMB.isEmpty()) {
      THIMBLE_COMB = new ItemStack(ModItems.THIMBLECOMB.get());
    }
    return THIMBLE_COMB;
  }

  @Override
  protected void onImpact(RayTraceResult ray) {
    RayTraceResult.Type raytraceresult$type = ray.getType();
    if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
      this.onEntityHit((EntityRayTraceResult) ray);
    } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
      this.func_230299_a_((BlockRayTraceResult) ray);
    }
    if (!world.isRemote()) {
      if (ray.getType() == RayTraceResult.Type.ENTITY) {
        EntityRayTraceResult ray2 = (EntityRayTraceResult) ray;
        Entity entity = ray2.getEntity();
        Entity shootingEntity = this.func_234616_v_();
        if ((entity != this || entity != shootingEntity) && entity instanceof LivingEntity && !(entity instanceof IAppleBee) && !(entity instanceof BeeEntity)) {
          LivingEntity living = (LivingEntity) ray2.getEntity();
          DamageSource source;
          if (shootingEntity instanceof LivingEntity) {
            source = DamageSource.causeMobDamage((LivingEntity) shootingEntity).setMagicDamage();
          } else {
            source = DamageSource.MAGIC;
          }
          living.attackEntityFrom(source, ConfigManager.getHoneycombDamage(shootingEntity));
          BlockState state = entity.world.getBlockState(entity.getPosition());
          VoxelShape shape = state.getShape(entity.world, entity.getPosition());
          if (shape.isEmpty() || !(shape.getBoundingBox().getYSize() < 1)) {
            entity.world.setBlockState(entity.getPosition().up(), Blocks.COBWEB.getDefaultState());
          }
          double val = ConfigManager.getHoneycombSize();
          List<LivingEntity> list = this.world.getEntitiesWithinAABBExcludingEntity(living, this.getBoundingBox().grow(val, val, val)).stream().filter(o -> o instanceof LivingEntity).map(o -> (LivingEntity) o).collect(Collectors.toList());
          world.addParticle(ParticleTypes.FALLING_HONEY, living.getPosX(), living.getPosY(), living.getPosZ(), 0, 0, 0);
          world.playSound(null, this.getPosition(), ModSounds.SPLOOSH.get(), SoundCategory.HOSTILE, 1f, 0.5f);
          for (LivingEntity l : list) {
            if (l == shootingEntity || l instanceof AppleBeeEntity || l instanceof BeeEntity) {
              continue;
            }
            state = entity.world.getBlockState(entity.getPosition());
            shape = state.getShape(entity.world, entity.getPosition());
            if (shape.isEmpty() || !(shape.getBoundingBox().getYSize() < 1)) {
              entity.world.setBlockState(entity.getPosition().up(), Blocks.COBWEB.getDefaultState());
            }
            l.attackEntityFrom(source, ConfigManager.getHoneycombDamage(shootingEntity));
            world.addParticle(ParticleTypes.FALLING_HONEY, l.getPosX(), l.getPosY(), l.getPosZ(), 0, 0, 0);
          }
        }
      }
    }
    if (!world.isRemote) {
      this.remove();
    }
  }

  @Override
  public EffectInstance getInstance() {
    return null;
  }
}
