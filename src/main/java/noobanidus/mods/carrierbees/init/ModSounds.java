package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.SoundEvent;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModSounds {
  public static RegistryEntry<SoundEvent> SPLOOSH = REGISTRATE.soundEvent("honey_projectile.sploosh").register();

  public static void load () {
  }
}
