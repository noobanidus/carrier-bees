package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModParticles {
  public static final RegistryEntry<BasicParticleType> DRIPPING_BOOGER = REGISTRATE.simple("dripping_booger", ParticleType.class, () -> new BasicParticleType(false));
  public static final RegistryEntry<BasicParticleType> FALLING_BOOGER = REGISTRATE.simple("falling_booger", ParticleType.class, () -> new BasicParticleType(false));
  public static final RegistryEntry<BasicParticleType> LANDING_BOOGER = REGISTRATE.simple("landing_booger", ParticleType.class, () -> new BasicParticleType(false));

  public static void load() {
  }
}
