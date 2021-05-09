package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import noobanidus.mods.carrierbees.effects.*;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModEffects {
  public static final RegistryEntry<FumbleEffect> FUMBLE = REGISTRATE.effect("fumble", FumbleEffect::new).register();

  public static final RegistryEntry<ExplosiveEffect> EXPLOSIVE = REGISTRATE.effect("explosive", ExplosiveEffect::new).register();

  public static final RegistryEntry<StumbleEffect> STUMBLE = REGISTRATE.effect("stumble", StumbleEffect::new).register();

  public static final RegistryEntry<CrumbleEffect> CRUMBLE = REGISTRATE.effect("crumble", CrumbleEffect::new).register();

  public static final RegistryEntry<DrumbleEffect> DRUMBLE = REGISTRATE.effect("drumble", DrumbleEffect::new).register();

  public static final RegistryEntry<TumbleEffect> TUMBLE = REGISTRATE.effect("tumble", TumbleEffect::new).register();

  public static final RegistryEntry<ThimbleEffect> THIMBLE = REGISTRATE.effect("thimble", ThimbleEffect::new).register();

  public static final RegistryEntry<JumbleEffect> JUMBLE = REGISTRATE.effect("jumble", JumbleEffect::new).register();

  public static void load() {
  }
}
