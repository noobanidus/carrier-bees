package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.particles.ParticleType;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModParticles {
  public static final RegistryEntry<BasicParticleType> AIR_BUBBLE = REGISTRATE.simple("air_bubbles", ParticleType.class, BasicParticleType::new);

  private static class BasicParticleType extends net.minecraft.particles.BasicParticleType {
    public BasicParticleType() {
      super(true);
    }
  }
}
