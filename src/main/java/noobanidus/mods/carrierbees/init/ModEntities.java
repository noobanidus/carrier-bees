package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.RegistryEntry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModEntities {
  public static RegistryEntry<EntityType<CarrierBeeEntity>> CARRIER_BEE = REGISTRATE.entity("carrier_bee", CarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(1.2F, 1.1F))
      .spawnEgg(0xeba134, 0xeb34ae)
      .build()
      .register();

  public static void load () {
  }
}
