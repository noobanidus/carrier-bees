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
public class CrumbleCombEntity extends HoneyCombEntity {
  private static ItemStack CRUMBLE_COMB = ItemStack.EMPTY;

  public CrumbleCombEntity(EntityType<? extends CrumbleCombEntity> type, World world) {
    super(type, world);
  }

  public CrumbleCombEntity(LivingEntity parent, double accelX, double accelY, double accelZ, World world) {
    super(ModEntities.CRUMBLE_COMB_PROJECTILE.get(), parent, accelX, accelY, accelZ, world);
  }

  @Override
  public ItemStack getItem() {
    if (CRUMBLE_COMB.isEmpty()) {
      CRUMBLE_COMB = new ItemStack(ModItems.CRUMBLECOMB.get());
    }
    return CRUMBLE_COMB;
  }

  @Override
  public EffectInstance getInstance() {
    return new EffectInstance(ModEffects.CRUMBLE.get(), 20*10);
  }
}
