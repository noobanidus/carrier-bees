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
    if (level != null && !level.isClientSide && decay-- <= 0) {
      level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
    }
  }

  @Override
  public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
    this.decay = p_230337_2_.getInt("decay");
    super.load(p_230337_1_, p_230337_2_);
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    CompoundNBT result = super.save(pCompound);
    result.putInt("decay", this.decay);
    return result;
  }
}
