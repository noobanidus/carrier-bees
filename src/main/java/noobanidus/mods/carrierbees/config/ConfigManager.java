package noobanidus.mods.carrierbees.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.projectiles.ThimbleCombEntity;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class ConfigManager {
  private static Random rand = new Random();
  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

  public static ForgeConfigSpec COMMON_CONFIG;

  private static ForgeConfigSpec.BooleanValue IMPROVE_AI;
  private static ForgeConfigSpec.DoubleValue HONEYCOMB_DAMAGE;
  private static ForgeConfigSpec.DoubleValue EXPLOSION_DAMAGE;
  private static ForgeConfigSpec.DoubleValue EXPLOSION_SIZE;
  private static ForgeConfigSpec.IntValue HONEYCOMB_SLOW;
  private static ForgeConfigSpec.BooleanValue ALWAYS_ANGRY;
  private static ForgeConfigSpec.DoubleValue HONEYCOMB_SIZE;
  private static ForgeConfigSpec.BooleanValue STING_KILLS;
  private static ForgeConfigSpec.IntValue DAMAGE_AMOUNT;
  private static ForgeConfigSpec.DoubleValue DAMAGE_CHANCE;
  private static ForgeConfigSpec.BooleanValue NICE_MODE;
  private static ForgeConfigSpec.IntValue FUMBLE_CHANCE;
  private static ForgeConfigSpec.IntValue DRUMBLE_CHANCE;

  private static int always_angry = -1;
  private static float honeycomb_damage = -1;
  private static float explosion_damage = -1;
  private static float explosion_size = -1;
  private static int honeycomb_slow = -1;
  private static double honeycomb_size = -1;
  private static int sting_kills = -1;
  private static int damage_amount = -1;
  private static double damage_chance = -1;
  private static int nice_mode = -1;
  private static int fumble_chance = -1;
  private static int drumble_chance = -1;
  private static int improve_ai = -1;

  public static boolean getImprovedAI () {
    if (improve_ai == -1) {
      improve_ai = IMPROVE_AI.get() ? 1 : 0;
    }
    return improve_ai == 1;
  }

  public static int getDrumbleChance() {
    if (drumble_chance == -1) {
      drumble_chance = DRUMBLE_CHANCE.get();
    }
    return drumble_chance;
  }

  public static int getFumbleChance() {
    if (fumble_chance == -1) {
      fumble_chance = FUMBLE_CHANCE.get();
    }
    return fumble_chance;
  }

  public static boolean getNiceMode() {
    if (nice_mode == -1) {
      nice_mode = NICE_MODE.get() ? 1 : 0;
    }
    return nice_mode == 1;
  }

  public static int getDamageAmount() {
    if (damage_amount == -1) {
      damage_amount = DAMAGE_AMOUNT.get();
    }
    return damage_amount;
  }

  public static double getDamageChance() {
    if (damage_chance == -1) {
      damage_chance = DAMAGE_CHANCE.get();
    }
    return damage_chance;
  }

  public static boolean getStingKills() {
    if (sting_kills == -1) {
      sting_kills = STING_KILLS.get() ? 1 : 0;
    }
    return sting_kills == 1;
  }

  public static boolean getAlwaysAngry() {
    if (always_angry == -1) {
      always_angry = ALWAYS_ANGRY.get() ? 1 : 0;
    }
    return always_angry == 1;
  }

  public static float getHoneycombDamage(Entity entity) {
    if (honeycomb_damage == -1) {
      honeycomb_damage = (float) (double) HONEYCOMB_DAMAGE.get();
    }
    if (entity instanceof ThimbleCombEntity) {
      return 1f;
    }
    return honeycomb_damage;
  }

  public static float getExplosionDamage() {
    if (explosion_damage == -1) {
      explosion_damage = (float) (double) EXPLOSION_DAMAGE.get();
    }
    return explosion_damage;
  }

  public static float getExplosionSize() {
    if (explosion_size == -1) {
      explosion_size = (float) (double) EXPLOSION_SIZE.get();
    }

    return explosion_size;
  }

  public static int getHoneycombSlow() {
    if (honeycomb_slow == -1) {
      honeycomb_slow = HONEYCOMB_SLOW.get();
    }
    return honeycomb_slow;
  }

  public static double getHoneycombSize() {
    if (honeycomb_size == -1) {
      honeycomb_size = HONEYCOMB_SIZE.get();
    }
    return honeycomb_size * 2;
  }

  public static ForgeConfigSpec.ConfigValue<List<? extends String>> COMMANDS;

  static {
    COMMON_BUILDER.push("carrier bees");
    HONEYCOMB_DAMAGE = COMMON_BUILDER.comment("the amount of damage the honeycomb projectile does").defineInRange("honeycomb_damage", 3.5, 0, Double.MAX_VALUE);
    HONEYCOMB_SLOW = COMMON_BUILDER.comment("the length of time in ticks that honeycomb slows for").defineInRange("honeycomb_slow", 8 * 20, 0, Integer.MAX_VALUE);
    HONEYCOMB_SIZE = COMMON_BUILDER.comment("the radius of the honeycomb projectile's splash damage/slow").defineInRange("honeycomb_size", 4, 0, Double.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("bomblebees");
    EXPLOSION_DAMAGE = COMMON_BUILDER.comment("the amount of damage that the bomblebee's explosive projectile does").defineInRange("explosion_damage", 3.5, 0, Double.MAX_VALUE);
    EXPLOSION_SIZE = COMMON_BUILDER.comment("the size of the bomblebee's explosive projectile's explosion").defineInRange("explosion_size", 4, 0, Double.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("fumblebee");
    FUMBLE_CHANCE = COMMON_BUILDER.comment("the chance of dropping an item per tick, expressed as 1 in X").defineInRange("fumble_chance", 24, 0, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("drumblebee");
    DRUMBLE_CHANCE = COMMON_BUILDER.comment("the chance of being granted slowness x while under the effects of the drumble debuff, expressed as 1 in X").defineInRange("fumble_chance", 30, 0, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("crumblebees");
    DAMAGE_AMOUNT = COMMON_BUILDER.comment("the maximum amount of durability damage applied (randomly from 1 to X)").defineInRange("durability_damage", 3, 0, Integer.MAX_VALUE);
    DAMAGE_CHANCE = COMMON_BUILDER.comment("the chance as a percent per tick of the potion effect that a tool or sword will take durability damage").defineInRange("durability_chance", 0.015, 0, Double.MAX_VALUE);
    NICE_MODE = COMMON_BUILDER.comment("whether or not crumble will damage items at or below 10 durability").define("nice_mode", true);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("general");
    ALWAYS_ANGRY = COMMON_BUILDER.comment("whether or not bees will always attack or only if angered").define("always_angry", true);
    STING_KILLS = COMMON_BUILDER.comment("whether or not bees will die after stinging").define("sting_kills", false);
    IMPROVE_AI = COMMON_BUILDER.comment("whether or not bees should have their AI improved").define("improved_ai", true);
    COMMON_BUILDER.pop();
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

  public static void configReloaded(ModConfig.Reloading event) {
    if (event.getConfig().getType() == ModConfig.Type.COMMON) {
      COMMON_CONFIG.setConfig(event.getConfig().getConfigData());
      honeycomb_damage = -1;
      explosion_damage = -1;
      explosion_size = -1;
      honeycomb_slow = -1;
      honeycomb_size = -1;
      always_angry = -1;
      damage_amount = -1;
      damage_chance = -1;
      nice_mode = -1;
      CarrierBees.LOG.info("CarrierBees config reloaded!");
    }
  }
}
