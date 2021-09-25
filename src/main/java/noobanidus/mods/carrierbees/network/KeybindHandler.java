package noobanidus.mods.carrierbees.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CarrierBees.MODID)
public class KeybindHandler {

  private static boolean lastFlyState = false;
  private static boolean lastDescendState = false;
  private static boolean lastForwardState = false;
  private static boolean lastBackwardState = false;
  private static boolean lastLeftState = false;
  private static boolean lastRightState = false;

  private static void tickEnd() {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null) {
      boolean flyState = mc.player.input.jumping;
      boolean descendState = mc.player.input.shiftKeyDown;
      boolean forwardState = mc.player.input.up;
      boolean backwardState = mc.player.input.down;
      boolean leftState = mc.player.input.left;
      boolean rightState = mc.player.input.right;
      if (flyState != lastFlyState || descendState != lastDescendState || forwardState != lastForwardState || backwardState != lastBackwardState || leftState != lastLeftState || rightState != lastRightState) {
        lastFlyState = flyState;
        lastDescendState = descendState;
        lastForwardState = forwardState;
        lastBackwardState = backwardState;
        lastLeftState = leftState;
        lastRightState = rightState;
        NetworkHandler.sendToServer(new PacketUpdateInput(flyState, descendState, forwardState, backwardState, leftState, rightState));
        SyncHandler.update(mc.player, flyState, descendState, forwardState, backwardState, leftState, rightState);
      }
    }
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent evt) {
    if (evt.phase == TickEvent.Phase.END) {
      tickEnd();
    }
  }
}
