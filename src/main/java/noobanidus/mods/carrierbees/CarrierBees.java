package noobanidus.mods.carrierbees;

import com.tterrag.registrate.Registrate;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModLang;
import noobanidus.mods.carrierbees.setup.ClientSetup;
import noobanidus.mods.carrierbees.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("carrierbees")
public class CarrierBees {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "carrierbees";
  public static Registrate REGISTRATE;

  public static ItemGroup GROUP = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.field_226906_mb_);
    }
  };

  public CarrierBees() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));

    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(CommonSetup::setup);
    modBus.addListener(ConfigManager::configReloaded);

    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
      modBus.addListener(ClientSetup::setup);
    });

    REGISTRATE = Registrate.create(MODID);
    REGISTRATE.itemGroup(() -> GROUP);

    ModEntities.load();
    ModLang.load();
  }
}
