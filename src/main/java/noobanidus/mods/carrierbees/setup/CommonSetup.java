package noobanidus.mods.carrierbees.setup;

import com.tterrag.registrate.util.LazySpawnEggItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.libs.noobutil.advancement.GenericTrigger;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.advancements.QueenPredicate;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;
import noobanidus.mods.carrierbees.entities.DrabbleBeeEntity;
import noobanidus.mods.carrierbees.init.ModEntities;

import java.util.Arrays;

public class CommonSetup {
  public static void setup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      AttributeModifierMap.MutableAttribute attr = AppleBeeEntity.createAttributes();
      GlobalEntityTypeAttributes.put(ModEntities.BOMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.FUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.CARRIER_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.STUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.CRUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.DRUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.TUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.THIMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.JUMBLE_BEE.get(), attr.create());
      GlobalEntityTypeAttributes.put(ModEntities.BEEHEMOTH.get(), BeehemothEntity.createAttributes().create());
      GlobalEntityTypeAttributes.put(ModEntities.DRABBLE_BEE.get(), DrabbleBeeEntity.attr().create());

      SpawnEggItem.EGGS.remove(null);
      SpawnEggItem.EGGS.put(ModEntities.BOMBLE_BEE.get(), ModEntities.BOMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.FUMBLE_BEE.get(), ModEntities.FUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.CARRIER_BEE.get(), ModEntities.CARRIER_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.STUMBLE_BEE.get(), ModEntities.STUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.CRUMBLE_BEE.get(), ModEntities.CRUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.DRUMBLE_BEE.get(), ModEntities.DRUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.TUMBLE_BEE.get(), ModEntities.TUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.THIMBLE_BEE.get(), ModEntities.THIMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.JUMBLE_BEE.get(), ModEntities.JUMBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.DRABBLE_BEE.get(), ModEntities.DRABBLE_BEE_EGG.get());
      SpawnEggItem.EGGS.put(ModEntities.BEEHEMOTH.get(), ModEntities.BEEHEMOTH_EGG.get());

      DefaultDispenseItemBehavior spawnEggDispense = new DefaultDispenseItemBehavior() {
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
          Direction direction = source.getBlockState().get(DispenserBlock.FACING);
          EntityType<?> entitytype = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
          entitytype.spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
          stack.shrink(1);
          return stack;
        }
      };

      for (LazySpawnEggItem<?> item : Arrays.asList(ModEntities.CARRIER_BEE_EGG.get(), ModEntities.BOMBLE_BEE_EGG.get(), ModEntities.FUMBLE_BEE_EGG.get(), ModEntities.STUMBLE_BEE_EGG.get(), ModEntities.CRUMBLE_BEE_EGG.get(), ModEntities.DRUMBLE_BEE_EGG.get(), ModEntities.TUMBLE_BEE_EGG.get(), ModEntities.THIMBLE_BEE_EGG.get(), ModEntities.JUMBLE_BEE_EGG.get(), ModEntities.DRABBLE_BEE_EGG.get(), ModEntities.BEEHEMOTH_EGG.get())) {
        DispenserBlock.registerDispenseBehavior(item, spawnEggDispense);
      }

      CarrierBees.QUEEN_PREDICATE = CriteriaTriggers.register(new GenericTrigger<>(CarrierBees.QUEEN_LOCATION, new QueenPredicate()));
      CarrierBees.STEED_PREDICATE = CriteriaTriggers.register(new GenericTrigger<>(CarrierBees.STEED_LOCATION, new QueenPredicate()));
    });
  }
}
