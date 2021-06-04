package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModSounds {
  public static RegistryEntry<SoundEvent> SPLOOSH = REGISTRATE.soundEvent("honey_projectile.sploosh").register();

  public static RegistryEntry<SoundEvent> CRUMBLE = REGISTRATE.soundEvent("crumbled_item").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_DEATH = REGISTRATE.soundEvent("entity.beehemoth.death").register();

  public static RegistryEntry<SoundEvent> BEEHEMOTH_HURT = REGISTRATE.soundEvent("entity.beehemoth.hurt").register();

  public static RegistryEntry<SoundEvent> GENERIC_BEE_DIES = REGISTRATE.soundEvent("entity.genericbee.dies").register();

  public static RegistryEntry<SoundEvent> GENERIC_BEE_HURT = REGISTRATE.soundEvent("entity.genericbee.hurt").register();

  public static RegistryEntry<SoundEvent> GENERIC_BEE_AMBIENT = REGISTRATE.soundEvent("entity.genericbee.ambient").register();

  public static RegistryEntry<SoundEvent> GENERIC_BEE_LOOP = REGISTRATE.soundEvent("entity.genericbee.loop").register();

  public static RegistryEntry<SoundEvent> GENERIC_BEE_LOOP_AGGRESSIVE = REGISTRATE.soundEvent("entity.genericbee.loop_aggressive").register();

  public static void load() {
  }
}
