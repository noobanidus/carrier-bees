package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import noobanidus.mods.carrierbees.CarrierBees;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModItems {
  public static RegistryEntry<Item> FUMBLECOMB = REGISTRATE.item("fumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.FUMBLE.get(), 20 * 10), 1.0f).fast().nutrition(3).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> BOMBYCOMB = REGISTRATE.item("bombycomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.EXPLOSIVE.get(), 1), 1.0f).fast().nutrition(8).saturationMod(0.9f).build()).rarity(Rarity.EPIC))
      .register();

  public static RegistryEntry<Item> STUMBLECOMB = REGISTRATE.item("stumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.STUMBLE.get(), 20 * 10), 1.0f).fast().nutrition(3).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> CRUMBLECOMB = REGISTRATE.item("crumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.CRUMBLE.get(), 20 * 10), 1.0f).fast().nutrition(3).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> DRUMBLECOMB = REGISTRATE.item("drumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.DRUMBLE.get(), 20 * 10), 1.0f).fast().nutrition(3).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> TUMBLECOMB = REGISTRATE.item("tumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.TUMBLE.get(), 20 * 10), 1.0f).fast().nutrition(18).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> THIMBLECOMB = REGISTRATE.item("thimblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.THIMBLE.get(), 20 * 10), 1.0f).fast().nutrition(3).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> JUMBLECOMB = REGISTRATE.item("jumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.JUMBLE.get(), 20 * 10), 1.0f).fast().nutrition(3).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> GENERICCOMB = REGISTRATE.item("genericcomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.GENERIC.get(), 20 * 10), 1.0f).fast().nutrition(3).saturationMod(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> BOOGERCOMB = REGISTRATE.item("boogercomb", Item::new)
      .properties(o -> o.food(new Food.Builder().fast().nutrition(1).saturationMod(0.5f).build()).rarity(Rarity.COMMON))
      .register();

  public static RegistryEntry<Item> ROYAL_JELLY = REGISTRATE.item("royal_jelly", Item::new)
      .properties(o -> o.food(new Food.Builder().fast().nutrition(20).saturationMod(20).alwaysEat().build()).rarity(Rarity.EPIC))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shaped(ctx::getEntry, 1)
            .pattern("FBS")
            .pattern("CHD")
            .pattern("TJL")
            .define('F', FUMBLECOMB.get())
            .define('B', BOMBYCOMB.get())
            .define('S', STUMBLECOMB.get())
            .define('C', CRUMBLECOMB.get())
            .define('D', DRUMBLECOMB.get())
            .define('T', TUMBLECOMB.get())
            .define('H', Items.HONEYCOMB)
            .define('L', THIMBLECOMB.get())
            .define('J', JUMBLECOMB.get())
            .unlockedBy("something", RegistrateRecipeProvider.hasItem(Items.HONEYCOMB))
            .save(p, new ResourceLocation(CarrierBees.MODID, "royal_jelly"));
      })
      .register();

  public static void load() {
  }
}
