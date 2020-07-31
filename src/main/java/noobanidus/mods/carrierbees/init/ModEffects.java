package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import noobanidus.mods.carrierbees.effects.ExplosiveEffect;
import noobanidus.mods.carrierbees.effects.FumbleEffect;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModEffects {
  public static final RegistryEntry<FumbleEffect> FUMBLE = REGISTRATE.effect("fumble", FumbleEffect::new).register();

  public static final RegistryEntry<ExplosiveEffect> EXPLOSIVE = REGISTRATE.effect("explosive", ExplosiveEffect::new).register();

  public static void load () {
  }
}
