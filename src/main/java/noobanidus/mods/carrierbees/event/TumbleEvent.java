package noobanidus.mods.carrierbees.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.init.ModEffects;

@Mod.EventBusSubscriber(modid= CarrierBees.MODID, value= Dist.CLIENT)
public class TumbleEvent {
  @SubscribeEvent
  public static void CameraSetupEvent (EntityViewRenderEvent.CameraSetup event) {
    PlayerEntity player = Minecraft.getInstance().player;
    if (player != null && player.getActivePotionEffect(ModEffects.TUMBLE.get()) != null) {
      event.setRoll(180f);
    }
  }
}
