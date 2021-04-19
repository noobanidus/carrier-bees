package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModSounds {
  public static RegistryEntry<SoundEvent> SPLOOSH = REGISTRATE.soundEvent("honey_projectile.sploosh").register();

  public static RegistryEntry<SoundEvent> CRUMBLE = REGISTRATE.soundEvent("crumbled_item").register();

  public static void load () {
  }
}
