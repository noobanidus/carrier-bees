package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.LazySpawnEggItem;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import noobanidus.mods.carrierbees.entities.BombleBeeEntity;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;
import noobanidus.mods.carrierbees.entities.FumbleCarrierBeeEntity;
import noobanidus.mods.carrierbees.entities.projectiles.BombEntity;
import noobanidus.mods.carrierbees.entities.projectiles.FumbleCombEntity;
import noobanidus.mods.carrierbees.entities.projectiles.HoneyCombEntity;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModEntities {
  public static RegistryEntry<EntityType<CarrierBeeEntity>> CARRIER_BEE = REGISTRATE.entity("carrier_bee", CarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(0.9F, 0.8F))
      .loot((p, e) -> p.registerLootTable(e, LootTable.builder()
              .addLootPool(LootPool.builder()
                  .addEntry(ItemLootEntry.builder(Items.HONEYCOMB)
                      .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                      .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 3)))
                  )
                  .rolls(ConstantRange.of(1))
              )
          )
      )
      .register();

  public static RegistryEntry<EntityType<FumbleCarrierBeeEntity>> FUMBLE_BEE = REGISTRATE.entity("fumble_bee", FumbleCarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(0.9F, 0.8F))
      .loot((p, e) -> p.registerLootTable(e, LootTable.builder()
              .addLootPool(LootPool.builder()
                  .addEntry(ItemLootEntry.builder(ModItems.FUMBLECOMB.get())
                      .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                      .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 3)))
                  )
                  .rolls(ConstantRange.of(1))
              )
          )
      )
      .register();

  public static RegistryEntry<EntityType<BombleBeeEntity>> BOMBLE_BEE = REGISTRATE.entity("bomble_bee", BombleBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(1.0F, 0.9F))
      .loot((p, e) -> p.registerLootTable(e, LootTable.builder()
              .addLootPool(LootPool.builder()
                  .addEntry(ItemLootEntry.builder(ModItems.BOMBYCOMB.get())
                      .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                      .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 3)))
                  )
                  .rolls(ConstantRange.of(1))
              )
          )
      )
      .register();

  public static RegistryEntry<LazySpawnEggItem<CarrierBeeEntity>> CARRIER_BEE_EGG = REGISTRATE.item("carrier_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(CARRIER_BEE, 0xedc343, 0x43241b, b)).model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"))).register();

  public static RegistryEntry<LazySpawnEggItem<BombleBeeEntity>> BOMBLE_BEE_EGG = REGISTRATE.item("bomble_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(BOMBLE_BEE, 0xedc343, 0xf94d38, b)).model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"))).register();

  public static RegistryEntry<LazySpawnEggItem<FumbleCarrierBeeEntity>> FUMBLE_BEE_EGG = REGISTRATE.item("fumble_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(FUMBLE_BEE, 0xedc343, 0x6eae08, b)).model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"))).register();

  public static RegistryEntry<EntityType<HoneyCombEntity>> HONEY_COMB_PROJECTILE = REGISTRATE.<HoneyCombEntity>entity("honey_comb_projectile", HoneyCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<BombEntity>> BOMB_PROJECTILE = REGISTRATE.<BombEntity>entity("bomb_projectile", BombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<FumbleCombEntity>> FUMBLE_COMB_PROJECTILE = REGISTRATE.<FumbleCombEntity>entity("fumble_comb_entity", FumbleCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static void load() {
  }
}
