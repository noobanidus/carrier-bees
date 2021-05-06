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

public class ThimbleEffect extends Effect {
  private static final Random rand = new Random();

  public ThimbleEffect() {
    super(EffectType.HARMFUL, 0xcacbc1);
  }

  @Override
  public boolean isReady(int p_76397_1_, int p_76397_2_) {
    return true;
  }

  @Override
  public void performEffect(LivingEntity entity, int amplifier) {
    if (entity instanceof PlayerEntity) {
      if (!entity.world.isRemote() && rand.nextInt(24) == 0) {
        BlockState state = entity.world.getBlockState(entity.getPosition());
        VoxelShape shape = state.getShape(entity.world, entity.getPosition());
        if (!shape.isEmpty() && shape.getBoundingBox().getYSize() < 1) {
          return;
        }
        entity.world.setBlockState(entity.getPosition().up(), Blocks.COBWEB.getDefaultState());
      }
    }
  }
}
