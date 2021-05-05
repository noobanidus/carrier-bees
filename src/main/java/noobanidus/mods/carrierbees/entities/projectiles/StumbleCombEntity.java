package noobanidus.mods.carrierbees.entities.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.init.ModEffects;
import noobanidus.mods.carrierbees.init.ModEntities;
import noobanidus.mods.carrierbees.init.ModItems;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IRendersAsItem.class
)
public class StumbleCombEntity extends HoneyCombEntity {
  private static ItemStack STUMBLE_COMB = ItemStack.EMPTY;

  public StumbleCombEntity(EntityType<? extends StumbleCombEntity> type, World world) {
    super(type, world);
  }

  public StumbleCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.STUMBLE_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (STUMBLE_COMB.isEmpty()) {
      STUMBLE_COMB = new ItemStack(ModItems.STUMBLECOMB.get());
    }
    return STUMBLE_COMB;
  }

  @Override
  public EffectInstance getInstance() {
    return new EffectInstance(ModEffects.STUMBLE.get(), 20 * 10);
  }
}
