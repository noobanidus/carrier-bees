package noobanidus.mods.carrierbees.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class FumbleEffect extends InstantEffect {
  private static final Random rand = new Random();

  public FumbleEffect() {
    super(EffectType.HARMFUL, 0xffffff);
  }

  @Override
  public void performEffect(LivingEntity entity, int amplifier) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      ItemStack stack = ItemStack.EMPTY;
      int slot = 0;
      for (int i = 0; i < amplifier + 1; i++) {
        switch (rand.nextInt(20)) {
          case 0:
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          case 6:
          case 7:
          case 8:
            int tries = 100;
            while (stack.isEmpty()) {
              stack = player.inventory.getStackInSlot(slot = rand.nextInt(36));
              tries--;
              if (tries < 0) {
                break;
              }
            }
            if (!stack.isEmpty() && !stack.onDroppedByPlayer(player)) {
              if (ForgeHooks.onPlayerTossEvent(player, player.inventory.decrStackSize(slot, stack.getCount()), false) != null) {
                break;
              }
            }
          case 9:
          case 10:
          case 11:
          case 12:
            stack = player.inventory.getCurrentItem();
            if (!stack.isEmpty() && !stack.onDroppedByPlayer(player)) {
              if (ForgeHooks.onPlayerTossEvent(player, player.inventory.decrStackSize(player.inventory.currentItem, stack.getCount()), false) != null) {
                break;
              }
            }
          case 13:
          case 14:
          case 15:
            stack = player.getHeldItemOffhand();
            if (!stack.isEmpty() && !stack.onDroppedByPlayer(player)) {
              if (ForgeHooks.onPlayerTossEvent(player, player.inventory.decrStackSize(40 /* off-hand item */, stack.getCount()), false) != null) {
                break;
              }
            }
          case 16:
          case 17:
          case 18:
          case 19:
            slot = rand.nextInt(4);
            stack = player.inventory.getStackInSlot(slot);
            if (!stack.isEmpty() && !stack.onDroppedByPlayer(player)) {
              if (ForgeHooks.onPlayerTossEvent(player, player.inventory.decrStackSize(35 + slot, stack.getCount()), false) != null) {
                break;
              }
            }
        }
      }
    }
  }
}
