package noobanidus.mods.carrierbees.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;

public class FlightHandler {
  public static boolean isFlying (PlayerEntity player) {
    Entity lowest = player.getLowestRidingEntity();
    if (lowest instanceof BeehemothEntity && lowest.getControllingPassenger() == player) {
      return true;
    }

    return false;
  }

  public static boolean isGoingUp (PlayerEntity player) {
    return SyncHandler.isHoldingUp(player) && isFlying(player);
  }
}
