package noobanidus.mods.carrierbees.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class DecayingTileEntity extends TileEntity implements ITickableTileEntity {
  private int decay = 35;

  public DecayingTileEntity(TileEntityType<?> p_i48289_1_) {
    super(p_i48289_1_);
  }

  @Override
  public void tick() {
    if (world != null && !world.isRemote && decay-- <= 0) {
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
  }

  @Override
  public void read(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
    this.decay = p_230337_2_.getInt("decay");
    super.read(p_230337_1_, p_230337_2_);
  }

  @Override
  public CompoundNBT write(CompoundNBT p_189515_1_) {
    CompoundNBT result = super.write(p_189515_1_);
    result.putInt("decay", this.decay);
    return result;
  }
}
