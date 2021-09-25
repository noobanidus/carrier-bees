package noobanidus.mods.carrierbees.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import noobanidus.mods.carrierbees.CarrierBees;

public class NetworkHandler {

  public static SimpleChannel CHANNEL_INSTANCE;
  private static int ID = 0;

  public static int nextID() {
    return ID++;
  }

  public static void registerMessages() {
    CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CarrierBees.MODID, "carrierbees"), () -> "1.0", s -> true, s -> true);

    CHANNEL_INSTANCE.messageBuilder(PacketUpdateInput.class, nextID())
        .encoder(PacketUpdateInput::toBytes)
        .decoder(PacketUpdateInput::fromBytes)
        .consumer(PacketUpdateInput::handle)
        .add();
  }

  public static void sendToClient(Object packet, ServerPlayerEntity player) {
    CHANNEL_INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
  }

  public static void sendToServer(Object packet) {
    CHANNEL_INSTANCE.sendToServer(packet);
  }
}
