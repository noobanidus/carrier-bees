package noobanidus.mods.carrierbees.setup;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.client.model.ModelHolder;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CarrierBees.MODID)
public class ClientEvents {
  public static void onResourceReloader(AddReloadListenerEvent event) {
    event.addListener(ModelHolder.Loader.INSTANCE);
  }
}
