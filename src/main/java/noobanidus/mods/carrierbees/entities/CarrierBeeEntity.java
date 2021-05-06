package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.config.ConfigManager;
import noobanidus.mods.carrierbees.entities.projectiles.HoneyCombEntity;
import noobanidus.mods.carrierbees.util.EffectBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarrierBeeEntity extends AppleBeeEntity {
  private List<EffectBuilder> effects = new ArrayList<>();

  public CarrierBeeEntity(EntityType<? extends CarrierBeeEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    if (ConfigManager.getHoneycombDamage(this) > 0) {
      this.goalSelector.addGoal(1, new CarrierBeeEntity.HoneycombProjectileAttackGoal(this));
    }
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
    if (!effects.isEmpty()) {
      ListNBT list = new ListNBT();
      list.addAll(effects.stream().map(EffectBuilder::asTag).collect(Collectors.toList()));
      tag.put("effects", list);
    }
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
    effects.clear();
    if (tag.contains("effects", Constants.NBT.TAG_LIST)) {
      ListNBT listEffects = tag.getList("effects", Constants.NBT.TAG_COMPOUND);
      for (int i = 0; i < listEffects.size(); i++) {
        CompoundNBT thisTag = listEffects.getCompound(i);
        if (thisTag.contains("d", Constants.NBT.TAG_INT) && thisTag.contains("n", Constants.NBT.TAG_STRING) && thisTag.contains("a", Constants.NBT.TAG_INT)) {
          ResourceLocation rl = new ResourceLocation(thisTag.getString("n"));
          Effect e = ForgeRegistries.POTIONS.getValue(rl);
          if (e == null) {
            CarrierBees.LOG.error("Invalid effect name: '" + rl.toString() + "', full tag: " + thisTag.toString());
          } else {
            int dur = thisTag.getInt("d");
            int amp = thisTag.getInt("a");
            effects.add(new EffectBuilder(e, dur, amp));
          }
        } else {
          CarrierBees.LOG.error("Invalid effect tag: " + thisTag.toString());
        }
      }
    }
  }

  static class HoneycombProjectileAttackGoal extends Goal {
    private final CarrierBeeEntity parentEntity;
    public int attackTimer;

    public HoneycombProjectileAttackGoal(CarrierBeeEntity bee) {
      this.parentEntity = bee;
    }

    @Override
    public boolean shouldExecute() {
      return this.parentEntity.getAttackTarget() != null && this.parentEntity.isAngry();
    }

    @Override
    public boolean shouldContinueExecuting() {
      return this.parentEntity.isAngry();
    }

    @Override
    public void startExecuting() {
      this.attackTimer = 0;
    }

    @Override
    public void resetTask() {
      this.parentEntity.setAttackTarget(null);
      this.parentEntity.setAggroed(false);
      this.parentEntity.getNavigator().clearPath();
    }

    @Override
    public void tick() {
      LivingEntity livingentity = this.parentEntity.getAttackTarget();
      if (livingentity == null) {
        return;
      }
      if (livingentity.getDistanceSq(this.parentEntity) < 400D && this.parentEntity.canEntityBeSeen(livingentity)) {
        World world = this.parentEntity.world;
        ++this.attackTimer;
        if (this.attackTimer == 20) {
          double d2 = livingentity.getPosX() - this.parentEntity.getPosX();
          double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + this.parentEntity.getPosYHeight(0.5D));
          double d4 = livingentity.getPosZ() - this.parentEntity.getPosZ();
          HoneyCombEntity honeycomb = new HoneyCombEntity(this.parentEntity, d2, d3, d4, world);
          honeycomb.setPosition(this.parentEntity.getPosX(), this.parentEntity.getPosYHeight(0.5D) + 0.5D, honeycomb.getPosZ());
          world.addEntity(honeycomb);
          this.attackTimer = -40;
        }
      } else if (this.attackTimer > 0) {
        --this.attackTimer;
      }
    }
  }
}
