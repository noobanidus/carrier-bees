package noobanidus.mods.carrierbees.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import noobanidus.mods.carrierbees.init.ModEntities;

public class BeeSummonCommand {
  private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summon.failed"));
  private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summon.failed.uuid"));
  private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summon.invalidPosition"));

  public static void register(CommandDispatcher<CommandSource> dispatch) {
    dispatch.register(Commands.literal("bee").requires((c) -> c.hasPermission(2)).executes((source) -> spawnRandomBee(source.getSource(), source.getSource().getPosition(), new CompoundNBT(), true)).then(Commands.argument("pos", Vec3Argument.vec3()).executes((c) -> {
      return spawnRandomBee(c.getSource(), Vec3Argument.getVec3(c, "pos"), new CompoundNBT(), true);
    }).then(Commands.argument("nbt", NBTCompoundTagArgument.compoundTag()).executes((c) -> {
      return spawnRandomBee(c.getSource(), Vec3Argument.getVec3(c, "pos"), NBTCompoundTagArgument.getCompoundTag(c, "nbt"), false);
    }))));
  }

  private static int spawnRandomBee(CommandSource source, Vector3d pos, CompoundNBT nbt, boolean v) throws CommandSyntaxException {
    BlockPos blockpos = new BlockPos(pos);
    if (!World.isInWorldBounds(blockpos)) {
      throw INVALID_POSITION.create();
    } else {
      CompoundNBT compoundnbt = nbt.copy();
      ResourceLocation random = ModEntities.SUMMONABLES.get(source.getLevel().getRandom().nextInt(ModEntities.SUMMONABLES.size()));
      compoundnbt.putString("id", random.toString());
      ServerWorld serverworld = source.getLevel();
      Entity entity = EntityType.loadEntityRecursive(compoundnbt, serverworld, (p_218914_1_) -> {
        p_218914_1_.moveTo(pos.x, pos.y, pos.z, p_218914_1_.yRot, p_218914_1_.xRot);
        return p_218914_1_;
      });
      if (entity == null) {
        throw ERROR_FAILED.create();
      } else {
        if (v && entity instanceof MobEntity) {
          ((MobEntity) entity).finalizeSpawn(source.getLevel(), source.getLevel().getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.COMMAND, null, null);
        }

        if (!serverworld.tryAddFreshEntityWithPassengers(entity)) {
          throw ERROR_DUPLICATE_UUID.create();
        } else {
          source.sendSuccess(new TranslationTextComponent("commands.summon.success", entity.getDisplayName()), true);
          return 1;
        }
      }
    }
  }
}
