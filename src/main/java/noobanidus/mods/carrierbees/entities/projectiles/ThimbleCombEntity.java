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
public class ThimbleCombEntity extends HoneyCombEntity {
  private static ItemStack THIMBLE_COMB = ItemStack.EMPTY;

  public ThimbleCombEntity(EntityType<? extends ThimbleCombEntity> type, World world) {
    super(type, world);
  }

  public ThimbleCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.THIMBLE_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (THIMBLE_COMB.isEmpty()) {
      THIMBLE_COMB = new ItemStack(ModItems.THIMBLECOMB.get());
    }
    return THIMBLE_COMB;
  }

  @Override
  public EffectInstance getInstance() {
    return new EffectInstance(ModEffects.THIMBLE.get(), 20 * 10);
  }
}
