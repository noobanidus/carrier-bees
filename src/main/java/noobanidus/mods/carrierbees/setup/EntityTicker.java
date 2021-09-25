package noobanidus.mods.carrierbees.setup;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = CarrierBees.MODID)
public class EntityTicker {
  private static final List<BeehemothEntity> entities = new ArrayList<>();

  @SubscribeEvent
  public static void onServerTick(TickEvent.ServerTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      entities.removeIf(Entity::isAddedToWorld);
      for (BeehemothEntity entity : entities) {
        ServerWorld world = (ServerWorld) entity.level;
        ServerChunkProvider provider = world.getChunkSource();
        IChunk ichunk = provider.getChunk(MathHelper.floor(entity.getX() / 16.0D), MathHelper.floor(entity.getZ() / 16.0D), ChunkStatus.FULL, false);
        if (ichunk != null) {
          world.addFreshEntity(entity);
        }
      }
    }
  }

  public static void addEntity(BeehemothEntity entity) {
    entities.add(entity);
  }
}
