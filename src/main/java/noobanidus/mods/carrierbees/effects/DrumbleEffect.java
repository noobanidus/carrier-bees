package noobanidus.mods.carrierbees.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.CapabilityItemHandler;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.init.ModSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrumbleEffect extends Effect {
  private static final Random rand = new Random();

  public DrumbleEffect() {
    super(EffectType.HARMFUL, 0x9c0000);
  }

  @Override
  public boolean isReady(int p_76397_1_, int p_76397_2_) {
    return true;
  }

  @Override
  public void performEffect(LivingEntity entity, int amplifier) {
    if (rand.nextInt(8) == 0) {
      entity.world.addParticle(ParticleTypes.END_ROD, entity.getPosXRandom(1.0), entity.getPosYRandom() + 0.5, entity.getPosZRandom(1.0), 0, 0, 0);
    }
    if (!entity.world.isRemote && rand.nextInt(30) == 0) {
      if (entity.getActivePotionEffect(Effects.SLOWNESS) == null) {
        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 15, 10, false, false, true));
      }
    }
  }
}
