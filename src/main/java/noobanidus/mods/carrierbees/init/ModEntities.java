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
import noobanidus.mods.carrierbees.entities.*;
import noobanidus.mods.carrierbees.entities.projectiles.*;

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

  public static RegistryEntry<EntityType<StumbleCarrierBeeEntity>> STUMBLE_BEE = REGISTRATE.entity("stumble_bee", StumbleCarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(0.9F, 0.8F))
      .loot((p, e) -> p.registerLootTable(e, LootTable.builder()
              .addLootPool(LootPool.builder()
                  .addEntry(ItemLootEntry.builder(ModItems.STUMBLECOMB.get())
                      .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                      .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 3)))
                  )
                  .rolls(ConstantRange.of(1))
              )
          )
      )
      .register();

  public static RegistryEntry<EntityType<CrumbleCarrierBeeEntity>> CRUMBLE_BEE = REGISTRATE.entity("crumble_bee", CrumbleCarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(1.3F, 1.2F))
      .loot((p, e) -> p.registerLootTable(e, LootTable.builder()
              .addLootPool(LootPool.builder()
                  .addEntry(ItemLootEntry.builder(ModItems.CRUMBLECOMB.get())
                      .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                      .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 3)))
                  )
                  .rolls(ConstantRange.of(1))
              )
          )
      )
      .register();

  public static RegistryEntry<EntityType<DrumbleCarrierBeeEntity>> DRUMBLE_BEE = REGISTRATE.entity("drumble_bee", DrumbleCarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(2.F, 2F))
      .loot((p, e) -> p.registerLootTable(e, LootTable.builder()
              .addLootPool(LootPool.builder()
                  .addEntry(ItemLootEntry.builder(ModItems.DRUMBLECOMB.get())
                      .acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
                      .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 3)))
                  )
                  .rolls(ConstantRange.of(1))
              )
          )
      )
      .register();

  public static RegistryEntry<EntityType<TumbleCarrierBeeEntity>> TUMBLE_BEE = REGISTRATE.entity("tumble_bee", TumbleCarrierBeeEntity::new, EntityClassification.CREATURE)
      .properties(o -> o.size(0.8F, 0.7F))
      .loot((p, e) -> p.registerLootTable(e, LootTable.builder()
              .addLootPool(LootPool.builder()
                  .addEntry(ItemLootEntry.builder(ModItems.TUMBLECOMB.get())
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

  public static RegistryEntry<LazySpawnEggItem<StumbleCarrierBeeEntity>> STUMBLE_BEE_EGG = REGISTRATE.item("stumble_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(STUMBLE_BEE, 0xd01adb, 0xcf97b5, b)).model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"))).register();

  public static RegistryEntry<LazySpawnEggItem<CrumbleCarrierBeeEntity>> CRUMBLE_BEE_EGG = REGISTRATE.item("crumble_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(CRUMBLE_BEE, 0xeecf98, 0xa0814a, b)).model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"))).register();

  public static RegistryEntry<LazySpawnEggItem<DrumbleCarrierBeeEntity>> DRUMBLE_BEE_EGG = REGISTRATE.item("drumble_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(DRUMBLE_BEE, 0xd8d2c0, 0x640000, b)).model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"))).register();

  public static RegistryEntry<LazySpawnEggItem<TumbleCarrierBeeEntity>> TUMBLE_BEE_EGG = REGISTRATE.item("tumble_bee_spawn_egg", (b) -> new LazySpawnEggItem<>(TUMBLE_BEE, 0xd8d2c0, 0x640000, b)).model((ctx, prov) -> prov.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"))).register();

  public static RegistryEntry<EntityType<HoneyCombEntity>> HONEY_COMB_PROJECTILE = REGISTRATE.<HoneyCombEntity>entity("honey_comb_projectile", HoneyCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<BombEntity>> BOMB_PROJECTILE = REGISTRATE.<BombEntity>entity("bomb_projectile", BombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<FumbleCombEntity>> FUMBLE_COMB_PROJECTILE = REGISTRATE.<FumbleCombEntity>entity("fumble_comb_entity", FumbleCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<StumbleCombEntity>> STUMBLE_COMB_PROJECTILE = REGISTRATE.<StumbleCombEntity>entity("stumble_comb_entity", StumbleCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<CrumbleCombEntity>> CRUMBLE_COMB_PROJECTILE = REGISTRATE.<CrumbleCombEntity>entity("crumble_comb_entity", CrumbleCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<DrumbleCombEntity>> DRUMBLE_COMB_PROJECTILE = REGISTRATE.<DrumbleCombEntity>entity("drumble_comb_entity", DrumbleCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static RegistryEntry<EntityType<TumbleCombEntity>> TUMBLE_COMB_PROJECTILE = REGISTRATE.<TumbleCombEntity>entity("tumble_comb_entity", TumbleCombEntity::new, EntityClassification.MISC)
      .properties(o -> o.size(1.0f, 1.0f))
      .register();

  public static void load() {
  }
}
