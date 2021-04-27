package noobanidus.mods.carrierbees.setup;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;
import noobanidus.mods.carrierbees.init.ModEntities;

import java.util.Locale;
import java.util.Objects;

@Mod.EventBusSubscriber(modid= CarrierBees.MODID)
public class WorldEvents {
  @SubscribeEvent
  public static void onJoin (EntityJoinWorldEvent event) {
    Entity entity = event.getEntity();
    if (entity.hasCustomName() && !(entity instanceof BeehemothEntity)) {
      if (Objects.requireNonNull(entity.getCustomName()).getUnformattedComponentText().toLowerCase(Locale.ROOT).contains("aranaira")) {
        event.setCanceled(true);
        BeehemothEntity newEntity = new BeehemothEntity(ModEntities.BEEHEMOTH.get(), entity.world);
        newEntity.setCustomName(entity.getCustomName());
        newEntity.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
        EntityTicker.addEntity(newEntity);
      }
    }
  }
}
