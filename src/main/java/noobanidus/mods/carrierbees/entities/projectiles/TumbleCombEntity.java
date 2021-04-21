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
public class TumbleCombEntity extends HoneyCombEntity {
  private static ItemStack TUMBLE_COMB = ItemStack.EMPTY;

  public TumbleCombEntity(EntityType<? extends TumbleCombEntity> type, World world) {
    super(type, world);
  }

  public TumbleCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.TUMBLE_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (TUMBLE_COMB.isEmpty()) {
      TUMBLE_COMB = new ItemStack(ModItems.TUMBLECOMB.get());
    }
    return TUMBLE_COMB;
  }

  @Override
  public EffectInstance getInstance() {
    return new EffectInstance(ModEffects.TUMBLE.get(), 30);
  }
}
