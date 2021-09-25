package noobanidus.mods.carrierbees.effects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.shapes.VoxelShape;
import noobanidus.mods.carrierbees.init.ModBlocks;

import java.util.Random;

public class ThimbleEffect extends Effect implements IBeeEffect {
  private static final Random rand = new Random();

  public ThimbleEffect() {
    super(EffectType.HARMFUL, 0xcacbc1);
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (entity instanceof PlayerEntity) {
      if (!entity.level.isClientSide() && rand.nextInt(24) == 0) {
        BlockState state = entity.level.getBlockState(entity.blockPosition());
        VoxelShape shape = state.getShape(entity.level, entity.blockPosition());
        if (!shape.isEmpty() && shape.bounds().getYsize() < 1) {
          return;
        }
        entity.level.setBlockAndUpdate(entity.blockPosition().above(), Blocks.COBWEB.defaultBlockState());
      }
    }
  }
}
