package noobanidus.mods.carrierbees.setup;

import com.tterrag.registrate.util.LazySpawnEggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import noobanidus.mods.carrierbees.client.render.BombleBeeRenderer;
import noobanidus.mods.carrierbees.client.render.CarrierBeeRenderer;
import noobanidus.mods.carrierbees.init.ModEntities;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
  public static void setup(FMLClientSetupEvent event) {
    DeferredWorkQueue.runLater(() -> {
      Minecraft mc = event.getMinecraftSupplier().get();
      EntityRendererManager manager = mc.getRenderManager();
      manager.register(ModEntities.CARRIER_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.FUMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.BOMBLE_BEE.get(), new BombleBeeRenderer(manager));
      ItemRenderer renderer = mc.getItemRenderer();
      manager.register(ModEntities.HONEY_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9F, true));
      manager.register(ModEntities.FUMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9F, true));
      manager.register(ModEntities.BOMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 1.75F, true));
      for (LazySpawnEggItem<?> item : Arrays.asList(ModEntities.CARRIER_BEE_EGG.get(), ModEntities.BOMBLE_BEE_EGG.get())) {
        mc.getItemColors().register((a, layer) -> item.getColor(layer), item);
      }
    });
  }
}
