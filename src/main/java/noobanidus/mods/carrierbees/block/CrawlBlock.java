package noobanidus.mods.carrierbees.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import noobanidus.mods.carrierbees.init.ModTiles;
import noobanidus.mods.carrierbees.tiles.DecayingTileEntity;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class CrawlBlock extends BreakableBlock {
  private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

  public CrawlBlock(Properties props) {
    super(props);
  }

  @Override
  public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
    return SHAPE;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new DecayingTileEntity(ModTiles.DECAYING.get());
  }
}
