package noobanidus.mods.carrierbees.init;

import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.loot.HasSaddle;

public class ModLoot {
  public static final LootConditionType HAS_SADDLE = new LootConditionType(new HasSaddle.Serializer());

  public static void load() {
    Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(CarrierBees.MODID, "has_saddle"), HAS_SADDLE);
  }
}
