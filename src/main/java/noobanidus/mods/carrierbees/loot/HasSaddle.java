package noobanidus.mods.carrierbees.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;
import noobanidus.mods.carrierbees.init.ModLoot;

import java.util.Set;

public class HasSaddle implements ILootCondition {
  private HasSaddle() {
  }

  @Override
  public Set<LootParameter<?>> getRequiredParameters() {
    return ImmutableSet.of(LootParameters.THIS_ENTITY);
  }

  @Override
  public boolean test(LootContext lootContext) {
    Entity looted = lootContext.get(LootParameters.THIS_ENTITY);
    if (looted instanceof BeehemothEntity) {
      BeehemothEntity bee = (BeehemothEntity) looted;
      return bee.getDataManager().get(BeehemothEntity.isSaddled);
    } else {
      return false;
    }
  }

  private static final HasSaddle INSTANCE = new HasSaddle();

  public static ILootCondition.IBuilder builder() {
    return () -> INSTANCE;
  }

  public static class Serializer implements ILootSerializer<HasSaddle> {
    @Override
    public void serialize(JsonObject p_230424_1_, HasSaddle p_230424_2_, JsonSerializationContext p_230424_3_) {
    }

    @Override
    public HasSaddle deserialize(JsonObject p_230423_1_, JsonDeserializationContext p_230423_2_) {
      return INSTANCE;
    }
  }

  @Override
  public LootConditionType func_230419_b_() {
    return ModLoot.HAS_SADDLE;
  }
}

