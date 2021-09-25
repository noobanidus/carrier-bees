package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import noobanidus.mods.carrierbees.block.CrawlBlock;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModBlocks {
  public static final RegistryEntry<CrawlBlock> CRAWL = REGISTRATE.block("crawl", Material.HEAVY_METAL, CrawlBlock::new).properties(o -> o.randomTicks().strength(0, 0))
      .blockstate((ctx, p) -> p.simpleBlock(ctx.getEntry(), p.models().getExistingFile(new ResourceLocation("minecraft", "barrier")))).register();

  public static void load() {
  }
}
