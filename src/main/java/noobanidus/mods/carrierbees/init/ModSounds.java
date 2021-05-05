package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.SoundEvent;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModSounds {
  public static RegistryEntry<SoundEvent> SPLOOSH = REGISTRATE.soundEvent("honey_projectile.sploosh").register();

  public static RegistryEntry<SoundEvent> CRUMBLE = REGISTRATE.soundEvent("crumbled_item").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_LOOP = REGISTRATE.soundEvent("entity.beehmoth.loop").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_LOOP_AGGRESSIVE = REGISTRATE.soundEvent("entity.beehmoth.loop_aggressive").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_DEATH = REGISTRATE.soundEvent("entity.beehmoth.death").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_HURT = REGISTRATE.soundEvent("entity.beehmoth.hurt").register();

  public static void load() {
  }
}
