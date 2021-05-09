package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.SoundEvent;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModSounds {
  public static RegistryEntry<SoundEvent> SPLOOSH = REGISTRATE.soundEvent("honey_projectile.sploosh").register();

  public static RegistryEntry<SoundEvent> CRUMBLE = REGISTRATE.soundEvent("crumbled_item").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_DEATH = REGISTRATE.soundEvent("entity.beehemoth.death").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_HURT = REGISTRATE.soundEvent("entity.beehemoth.hurt").register();

  public static void load() {
  }
}
