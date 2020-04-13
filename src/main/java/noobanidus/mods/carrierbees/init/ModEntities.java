package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.LazySpawnEggItem;
import com.tterrag.registrate.util.RegistryEntry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import noobanidus.mods.carrierbees.entities.BombleBeeEntity;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;
import noobanidus.mods.carrierbees.entities.projectiles.BombEntity;
import noobanidus.mods.carrierbees.entities.projectiles.HoneyCombEntity;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModEntities {
  public static RegistryEntry<EntityType<CarrierBeeEntity>> CARRIER_BEE = REGISTRATE.entity("carrier_bee", CarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(1.2F, 1.1F))
      .register();

  public static RegistryEntry<EntityType<BombleBeeEntity>> BOMBLE_BEE = REGISTRATE.entity("bomble_bee", BombleBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(1.2F, 1.1F))
      .register();

  public static RegistryEntry<LazySpawnEggItem<CarrierBeeEntity>> CARRIER_BEE_EGG = REGISTRATE.item("carrier_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(CARRIER_BEE, 0x43241b, 0xedc343, b))
      .register();

  public static RegistryEntry<LazySpawnEggItem<BombleBeeEntity>> BOMBLE_BEE_EGG = REGISTRATE.item("bomble_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(BOMBLE_BEE, 0x43241b, 0xf94d38, b))
      .register();

  public static RegistryEntry<EntityType<HoneyCombEntity>> HONEY_COMB_PROJECTILE = REGISTRATE.<HoneyCombEntity>entity("honey_comb_projectile", HoneyCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<BombEntity>> BOMB_PROJECTILE = REGISTRATE.<BombEntity>entity("bomb_projectile", BombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static void load () {
  }
}
