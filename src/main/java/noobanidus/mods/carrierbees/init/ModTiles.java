package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tileentity.TileEntityType;
import noobanidus.mods.carrierbees.tiles.DecayingTileEntity;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModTiles {
  public static final RegistryEntry<TileEntityType<DecayingTileEntity>> DECAYING = REGISTRATE.tileEntity("decaying", DecayingTileEntity::new).validBlock(ModBlocks.CRAWL).register();

  public static void load() {
  }
}
