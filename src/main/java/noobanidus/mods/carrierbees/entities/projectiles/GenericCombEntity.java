package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.BoogerBeeEntity;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModItems;
import noobanidus.mods.carrierbees.init.ModParticles;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class GenericCombEntity extends HoneyCombEntity {
  private static ItemStack GENERIC_COMB = ItemStack.EMPTY;

  public GenericCombEntity(EntityType<? extends GenericCombEntity> type, World world) {
    super(type, world);
  }

  public GenericCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.GENERIC_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (GENERIC_COMB.isEmpty()) {
      GENERIC_COMB = new ItemStack(ModItems.BOOGERCOMB.get());
    }
    return GENERIC_COMB;
  }

  @Override
  public EffectInstance getInstance() {
    return null;
  }

  @Override
  protected IParticleData getTrailParticle() {
    return ModParticles.FALLING_BOOGER.get();
  }

  @Override
  protected void onHit(RayTraceResult ray) {
    if (!level.isClientSide) {
      BlockPos position;
      if (ray.getType() == RayTraceResult.Type.ENTITY) {
        position = ((EntityRayTraceResult) ray).getEntity().blockPosition();
      } else if (ray.getType() == RayTraceResult.Type.BLOCK) {
        position = ((BlockRayTraceResult) ray).getBlockPos();
      } else {
        return;
      }

      int counter = 15;
      while (!level.isEmptyBlock(position) && counter > 0) {
        counter--;
        double x = position.getX() + (random.nextDouble() - 0.5d) * 3;
        double y = position.getY() + (random.nextDouble() - 0.5d) * 3;
        double z = position.getZ() + (random.nextDouble() - 0.5d) * 3;
        position = new BlockPos(x, y, z);
      }

      if (!level.isEmptyBlock(position)) {
        return;
      }

      Entity bee = ModEntities.BOOGER_BEE.get().spawn((ServerWorld) level, null, null, position, SpawnReason.COMMAND, true, false);
      Entity parent = getOwner();
      if (bee != null && parent != null) {
        for (EffectInstance instance : ((LivingEntity)parent).getActiveEffects()) {
          EffectInstance copy = new EffectInstance(instance.getEffect(), instance.getDuration(), instance.getAmplifier(), instance.isAmbient(), instance.isVisible());
          ((LivingEntity)bee).addEffect(copy);
        }
      }

      level.addParticle(ModParticles.FALLING_BOOGER.get(), position.getX(), position.getY(), position.getZ(), 0, 0, 0);
      this.remove();
    }
  }
}
