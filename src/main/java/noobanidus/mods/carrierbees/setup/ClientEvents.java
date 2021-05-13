package noobanidus.mods.carrierbees.setup;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.client.particle.AirBubbleParticle;
import noobanidus.mods.carrierbees.init.ModParticles;

@Mod.EventBusSubscriber(modid = CarrierBees.MODID, value = Dist.CLIENT)
public class ClientEvents {
  @SubscribeEvent
  public static void onParticle(ParticleFactoryRegisterEvent event) {
    Minecraft.getInstance().particles.registerFactory(ModParticles.AIR_BUBBLE.get(), AirBubbleParticle.Factory::new);
  }
}
