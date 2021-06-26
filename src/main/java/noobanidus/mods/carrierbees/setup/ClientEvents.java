package noobanidus.mods.carrierbees.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.client.particle.BoogerParticle;
import noobanidus.mods.carrierbees.init.ModParticles;

@Mod.EventBusSubscriber(modid = CarrierBees.MODID, value = Dist.CLIENT)
public class ClientEvents {
  @SubscribeEvent
  public static void onParticle(ParticleFactoryRegisterEvent event) {
    ParticleManager manager = Minecraft.getInstance().particles;
    manager.registerFactory(ModParticles.DRIPPING_BOOGER.get(), BoogerParticle.DrippingBoogerFactory::new);
    manager.registerFactory(ModParticles.LANDING_BOOGER.get(), BoogerParticle.LandingBoogerFactory::new);
    manager.registerFactory(ModParticles.FALLING_BOOGER.get(), BoogerParticle.FallingBoogerFactory::new);
  }
}
