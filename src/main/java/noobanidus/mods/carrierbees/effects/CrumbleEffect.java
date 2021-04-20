package noobanidus.mods.carrierbees.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.CapabilityItemHandler;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.init.ModSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrumbleEffect extends Effect {
  private static final Random rand = new Random();

  public CrumbleEffect() {
    super(EffectType.HARMFUL, 0xa0814a);
  }

  @Override
  public boolean isReady(int p_76397_1_, int p_76397_2_) {
    return true;
  }

  @Override
  public void performEffect(LivingEntity entity, int amplifier) {
    if (entity instanceof PlayerEntity) {
      if (rand.nextDouble() <= ConfigManager.getDamageChance()) {
        List<ItemStack> tools = new ArrayList<>();
        entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(cap -> {
          for (int i = 0; i < cap.getSlots(); i++) {
            ItemStack inSlot = cap.getStackInSlot(i);
            if ((!inSlot.getToolTypes().isEmpty() || inSlot.getItem() instanceof SwordItem || inSlot.getItem() instanceof ArmorItem) && inSlot.isDamageable()) {
              if (ConfigManager.getNiceMode() && inSlot.getDamage() >= inSlot.getMaxDamage() + 10) {
                continue;
              }
              tools.add(inSlot);
            }
          }
        });
        if (entity.getHeldItemOffhand().getItem() instanceof ShieldItem) {
          ItemStack inSlot = entity.getHeldItemOffhand();
          if (!ConfigManager.getNiceMode() || inSlot.getDamage() < inSlot.getMaxDamage() + 10) {
            tools.add(inSlot);
          }
        }
        if (!tools.isEmpty()) {
          ItemStack tool = tools.get(rand.nextInt(tools.size()));
          tool.damageItem(rand.nextInt(Math.max(1, ConfigManager.getDamageAmount())) + 1, entity, (playerEntity) -> {
          });
          entity.world.playSound(null, entity.getPosition(), ModSounds.CRUMBLE.get(), SoundCategory.PLAYERS, 1f, 2f);
        }
      }
    }
  }
}
