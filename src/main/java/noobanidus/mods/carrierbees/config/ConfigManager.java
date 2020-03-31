package noobanidus.mods.carrierbees.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import noobanidus.mods.carrierbees.CarrierBees;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ConfigManager {
  private static Random rand = new Random();
  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

  public static ForgeConfigSpec COMMON_CONFIG;

  public static ForgeConfigSpec.ConfigValue<List<? extends String>> COMMANDS;

  static {
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static void loadConfig(ForgeConfigSpec spec, Path path) {
    CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
    configData.load();
    spec.setConfig(configData);
  }

  public static String getCommand() {
    List<? extends String> COMMAND_LIST = COMMANDS.get();

    if (COMMAND_LIST.isEmpty()) {
      return null;
    }

    return COMMAND_LIST.get(rand.nextInt(COMMAND_LIST.size()));
  }

  public static void configReloaded (ModConfig.Reloading event) {
    if (event.getConfig().getType() == ModConfig.Type.COMMON) {
      COMMON_CONFIG.setConfig(event.getConfig().getConfigData());
      CarrierBees.LOG.info("CarrierBees config reloaded!");
    }
  }
}
