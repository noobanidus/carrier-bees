package noobanidus.mods.carrierbees.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModItems {
  public static RegistryEntry<Item> FUMBLECOMB = REGISTRATE.item("fumblecomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.FUMBLE.get(), 20*10), 1.0f).fastToEat().hunger(3).saturation(1.5f).build()).rarity(Rarity.UNCOMMON))
      .register();

  public static RegistryEntry<Item> BOMBYCOMB = REGISTRATE.item("bombycomb", Item::new)
      .properties(o -> o.food(new Food.Builder().effect(() -> new EffectInstance(ModEffects.EXPLOSIVE.get(), 1), 1.0f).fastToEat().hunger(8).saturation(0.9f).build()).rarity(Rarity.EPIC))
      .register();

  public static void load() {
  }
}
