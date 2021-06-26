package noobanidus.mods.carrierbees.setup;

import com.tterrag.registrate.util.LazySpawnEggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import noobanidus.mods.carrierbees.client.render.BeehemothRenderer;
import noobanidus.mods.carrierbees.client.render.CarrierBeeRenderer;
import noobanidus.mods.carrierbees.client.render.DrabbleBeeRenderer;
import noobanidus.mods.carrierbees.client.render.GenericBeeRenderer;
import noobanidus.mods.carrierbees.init.ModEntities;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
  public static void setup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
      Minecraft mc = event.getMinecraftSupplier().get();
      EntityRendererManager manager = mc.getRenderManager();
      manager.register(ModEntities.CARRIER_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.FUMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.BOMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.STUMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.CRUMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.DRUMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.TUMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.THIMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.JUMBLE_BEE.get(), new CarrierBeeRenderer(manager));
      manager.register(ModEntities.BEEHEMOTH.get(), new BeehemothRenderer(manager));
      manager.register(ModEntities.DRABBLE_BEE.get(), new DrabbleBeeRenderer(manager));
      manager.register(ModEntities.GENERIC_BEE.get(), new GenericBeeRenderer(manager));
      manager.register(ModEntities.BOOGER_BEE.get(), new CarrierBeeRenderer(manager));
      ItemRenderer renderer = mc.getItemRenderer();
      manager.register(ModEntities.HONEY_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9F, true));
      manager.register(ModEntities.FUMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9F, true));
      manager.register(ModEntities.BOMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 1.75F, true));
      manager.register(ModEntities.STUMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.CRUMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.DRUMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.TUMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.THIMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.JUMBLE_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.GENERIC_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.BUCKET_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      manager.register(ModEntities.BOOGER_COMB_PROJECTILE.get(), new SpriteRenderer<>(manager, renderer, 0.9f, true));
      for (LazySpawnEggItem<?> item : Arrays.asList(ModEntities.CARRIER_BEE_EGG.get(), ModEntities.BOMBLE_BEE_EGG.get(), ModEntities.FUMBLE_BEE_EGG.get(), ModEntities.STUMBLE_BEE_EGG.get(), ModEntities.CRUMBLE_BEE_EGG.get(), ModEntities.DRUMBLE_BEE_EGG.get(), ModEntities.TUMBLE_BEE_EGG.get(), ModEntities.THIMBLE_BEE_EGG.get(), ModEntities.JUMBLE_BEE_EGG.get(), ModEntities.DRABBLE_BEE_EGG.get(), ModEntities.GENERIC_BEE_EGG.get(), ModEntities.BEEHEMOTH_EGG.get(), ModEntities.BOOGER_BEE_EGG.get())) {
        mc.getItemColors().register((a, layer) -> item.getColor(layer), item);
      }
    });
  }
}
