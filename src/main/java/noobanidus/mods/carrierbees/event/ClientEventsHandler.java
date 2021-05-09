package noobanidus.mods.carrierbees.event;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;
import noobanidus.mods.carrierbees.init.ModEffects;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.setup.EntityTicker;

import java.util.Locale;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = CarrierBees.MODID, value = Dist.CLIENT)
public class ClientEventsHandler {
  @SubscribeEvent
  public static void tumbleHandler(EntityViewRenderEvent.CameraSetup event) {
    PlayerEntity player = Minecraft.getInstance().player;
    if (player != null && player.getActivePotionEffect(ModEffects.TUMBLE.get()) != null) {
      event.setRoll(180f);
    }
  }

/*  @SubscribeEvent
  public static void beehemothHandler(EntityJoinWorldEvent event) {
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
    *//*if (event.getWorld().isRemote() && entity instanceof IEntitySound) {
      DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ((IEntitySound)entity)::initSound);
    }*//*
  }*/
}
