package noobanidus.mods.carrierbees.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import noobanidus.mods.carrierbees.client.render.CarrierBeeRenderer;
import noobanidus.mods.carrierbees.init.ModEntities;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
  public static void setup(FMLClientSetupEvent event) {
    Minecraft mc = event.getMinecraftSupplier().get();
    EntityRendererManager manager = mc.getRenderManager();
    manager.register(ModEntities.CARRIER_BEE.get(), new CarrierBeeRenderer(manager));
  }
}
