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
public class JumbleCombEntity extends HoneyCombEntity {
  private static ItemStack JUMBLE_COMB = ItemStack.EMPTY;

  public JumbleCombEntity(EntityType<? extends JumbleCombEntity> type, World world) {
    super(type, world);
  }

  public JumbleCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.JUMBLE_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (JUMBLE_COMB.isEmpty()) {
      JUMBLE_COMB = new ItemStack(ModItems.JUMBLECOMB.get());
    }
    return JUMBLE_COMB;
  }

  @Override
  public EffectInstance getInstance() {
    return new EffectInstance(ModEffects.JUMBLE.get(), 20 * 5);
  }
}
