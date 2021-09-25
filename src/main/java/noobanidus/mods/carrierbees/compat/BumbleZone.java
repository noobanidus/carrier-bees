package noobanidus.mods.carrierbees.compat;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BumbleZone {
  public static final String MODID = "the_bumblezone";
  public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(MODID, MODID);
  public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.create(Registry.DIMENSION_REGISTRY, MOD_DIMENSION_ID);
}
