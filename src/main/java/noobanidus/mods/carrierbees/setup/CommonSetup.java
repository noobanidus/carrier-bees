package noobanidus.mods.carrierbees.setup;

import com.tterrag.registrate.util.LazySpawnEggItem;
import jdk.nashorn.internal.objects.Global;
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
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.libs.noobutil.advancement.GenericTrigger;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.advancements.QueenPredicate;
import noobanidus.mods.carrierbees.entities.*;
import noobanidus.mods.carrierbees.init.ModEntities;

import java.util.Arrays;

public class CommonSetup {
  public static void onAttribute (EntityAttributeCreationEvent event) {
      AttributeModifierMap.MutableAttribute attr = AppleBeeEntity.createAttributes();
      event.put(ModEntities.BOMBLE_BEE.get(), attr.build());
      event.put(ModEntities.FUMBLE_BEE.get(), attr.build());
      event.put(ModEntities.CARRIER_BEE.get(), attr.build());
      event.put(ModEntities.STUMBLE_BEE.get(), attr.build());
      event.put(ModEntities.CRUMBLE_BEE.get(), attr.build());
      event.put(ModEntities.DRUMBLE_BEE.get(), attr.build());
      event.put(ModEntities.TUMBLE_BEE.get(), attr.build());
      event.put(ModEntities.THIMBLE_BEE.get(), ThimbleBeeEntity.createAttributes().build());
      event.put(ModEntities.JUMBLE_BEE.get(), JumbleBeeEntity.createAttributes().build());
      event.put(ModEntities.GENERIC_BEE.get(), attr.build());
      event.put(ModEntities.BEEHEMOTH.get(), BeehemothEntity.createAttributes().build());
      event.put(ModEntities.DRABBLE_BEE.get(), DrabbleBeeEntity.attr().build());
      event.put(ModEntities.BOOGER_BEE.get(), BoogerBeeEntity.createAttributes().build());
  }

  public static void setup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      SpawnEggItem.BY_ID.remove(null);
      SpawnEggItem.BY_ID.put(ModEntities.BOMBLE_BEE.get(), ModEntities.BOMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.FUMBLE_BEE.get(), ModEntities.FUMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.CARRIER_BEE.get(), ModEntities.CARRIER_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.STUMBLE_BEE.get(), ModEntities.STUMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.CRUMBLE_BEE.get(), ModEntities.CRUMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.DRUMBLE_BEE.get(), ModEntities.DRUMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.TUMBLE_BEE.get(), ModEntities.TUMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.THIMBLE_BEE.get(), ModEntities.THIMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.JUMBLE_BEE.get(), ModEntities.JUMBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.DRABBLE_BEE.get(), ModEntities.DRABBLE_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.GENERIC_BEE.get(), ModEntities.GENERIC_BEE_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.BEEHEMOTH.get(), ModEntities.BEEHEMOTH_EGG.get());
      SpawnEggItem.BY_ID.put(ModEntities.BOOGER_BEE.get(), ModEntities.BOOGER_BEE_EGG.get());

      DefaultDispenseItemBehavior spawnEggDispense = new DefaultDispenseItemBehavior() {
        public ItemStack execute(IBlockSource source, ItemStack stack) {
          Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
          EntityType<?> entitytype = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
          entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
          stack.shrink(1);
          return stack;
        }
      };

      for (LazySpawnEggItem<?> item : Arrays.asList(ModEntities.CARRIER_BEE_EGG.get(), ModEntities.BOMBLE_BEE_EGG.get(), ModEntities.FUMBLE_BEE_EGG.get(), ModEntities.STUMBLE_BEE_EGG.get(), ModEntities.CRUMBLE_BEE_EGG.get(), ModEntities.DRUMBLE_BEE_EGG.get(), ModEntities.TUMBLE_BEE_EGG.get(), ModEntities.THIMBLE_BEE_EGG.get(), ModEntities.JUMBLE_BEE_EGG.get(), ModEntities.DRABBLE_BEE_EGG.get(), ModEntities.GENERIC_BEE_EGG.get(), ModEntities.BEEHEMOTH_EGG.get(), ModEntities.BOOGER_BEE_EGG.get())) {
        DispenserBlock.registerBehavior(item, spawnEggDispense);
      }

      CarrierBees.QUEEN_PREDICATE = CriteriaTriggers.register(new GenericTrigger<>(CarrierBees.QUEEN_LOCATION, new QueenPredicate()));
      CarrierBees.STEED_PREDICATE = CriteriaTriggers.register(new GenericTrigger<>(CarrierBees.STEED_LOCATION, new QueenPredicate()));
    });
  }
}
