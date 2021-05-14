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
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.FUMBLE.get(), 20 * 10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> BOMBYCOMB = REGISTRATE.item("bombycomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.EXPLOSIVE.get(), 1), 1.0f).fastToEat().hunger(8).saturation(0.9f).build()).rarity(Rarity.EPIC))
      .register();

  public static RegistryEntry<Item> STUMBLECOMB = REGISTRATE.item("stumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.STUMBLE.get(), 20 * 10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> CRUMBLECOMB = REGISTRATE.item("crumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.CRUMBLE.get(), 20 * 10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> DRUMBLECOMB = REGISTRATE.item("drumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.DRUMBLE.get(), 20 * 10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> TUMBLECOMB = REGISTRATE.item("tumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.TUMBLE.get(), 20 * 10), 1.0f).fastToEat().hunger(18).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> THIMBLECOMB = REGISTRATE.item("thimblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.THIMBLE.get(), 20 * 10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> JUMBLECOMB = REGISTRATE.item("jumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.JUMBLE.get(), 20 * 10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> GENERICCOMB = REGISTRATE.item("genericcomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.GENERIC.get(), 20 * 10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> ROYAL_JELLY = REGISTRATE.item("royal_jelly", Item::new)
      .properties(o -> o.food(new Food.Builder().fastToEat().hunger(20).saturation(20).setAlwaysEdible().build()).rarity(Rarity.EPIC))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shapedRecipe(ctx::getEntry, 1)
            .patternLine("FBS")
            .patternLine("CHD")
            .patternLine("TJL")
            .key('F', FUMBLECOMB.get())
            .key('B', BOMBYCOMB.get())
            .key('S', STUMBLECOMB.get())
            .key('C', CRUMBLECOMB.get())
            .key('D', DRUMBLECOMB.get())
            .key('T', TUMBLECOMB.get())
            .key('H', Items.HONEYCOMB)
            .key('L', THIMBLECOMB.get())
            .key('J', JUMBLECOMB.get())
            .addCriterion("something", RegistrateRecipeProvider.hasItem(Items.HONEYCOMB))
            .build(p, new ResourceLocation(CarrierBees.MODID, "royal_jelly"));
      })
      .register();

  public static void load() {
  }
}
