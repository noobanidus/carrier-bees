package noobanidus.mods.carrierbees;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.advancement.GenericTrigger;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.mods.carrierbees.commands.BeeSummonCommand;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.init.*;
import noobanidus.mods.carrierbees.setup.ClientInit;
import noobanidus.mods.carrierbees.setup.ClientSetup;
import noobanidus.mods.carrierbees.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("carrierbees")
public class CarrierBees {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "carrierbees";
  public static CustomRegistrate REGISTRATE;

  public static GenericTrigger<Void> QUEEN_PREDICATE = null;
  public static GenericTrigger<Void> STEED_PREDICATE = null;
  public static final ResourceLocation QUEEN_LOCATION = new ResourceLocation(MODID, "queen");
  public static final ResourceLocation STEED_LOCATION = new ResourceLocation(MODID, "steed");

  public static ItemGroup GROUP = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.HONEYCOMB_BLOCK);
    }
  };

  public CarrierBees() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));

    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(CommonSetup::setup);
    modBus.addListener(CommonSetup::onAttribute);
    modBus.addListener(ConfigManager::configReloaded);
    MinecraftForge.EVENT_BUS.addListener(this::onCommands);

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);

    REGISTRATE = CustomRegistrate.create(MODID);
    REGISTRATE.itemGroup(() -> GROUP);

    ModEntities.load();
    ModLang.load();
    ModSounds.load();
    ModItems.load();
    ModEffects.load();
    ModBlocks.load();
    ModTiles.load();
    ModParticles.load();
  }

  private void onCommands(RegisterCommandsEvent event) {
    BeeSummonCommand.register(event.getDispatcher());
  }
}
