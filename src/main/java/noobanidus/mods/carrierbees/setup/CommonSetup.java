package noobanidus.mods.carrierbees.setup;

import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
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
    });
  }
}
