package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.particles.ParticleType;
import noobanidus.mods.carrierbees.client.particle.AirBubbleParticle;
import noobanidus.mods.carrierbees.client.particle.AirBubbleParticleType;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModParticles {
  public static final RegistryEntry<AirBubbleParticleType> AIR_BUBBLE = REGISTRATE.simple("air_bubble", ParticleType.class, () -> new AirBubbleParticleType(true));

  public static void load () {
  }
}
