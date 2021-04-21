package noobanidus.mods.carrierbees.setup;

import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.init.ModEntities;

public class CommonSetup {
  public static void setup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      AttributeModifierMap.MutableAttribute attr = AppleBeeEntity.createAttributes();
      GlobalEntityTypeAttributes.put(ModEntities.BOMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.FUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.CARRIER_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.STUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.CRUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.DRUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.TUMBLE_BEE.get(), attr.create());

      SpawnEggItem.EGGS.remove(null);
      SpawnEggItem.EGGS.put(ModEntities.BOMBLE_BEE.get(), ModEntities.BOMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.FUMBLE_BEE.get(), ModEntities.FUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.CARRIER_BEE.get(), ModEntities.CARRIER_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.STUMBLE_BEE.get(), ModEntities.STUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.CRUMBLE_BEE.get(), ModEntities.CRUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.DRUMBLE_BEE.get(), ModEntities.DRUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.TUMBLE_BEE.get(), ModEntities.TUMBLE_BEE_EGG.get());
    });
  }
}
