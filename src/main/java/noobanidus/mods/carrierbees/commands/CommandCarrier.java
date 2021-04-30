package noobanidus.mods.carrierbees.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import noobanidus.mods.carrierbees.init.ModEntities;

public class CommandCarrier {
  private final CommandDispatcher<CommandSource> dispatcher;

  public CommandCarrier(CommandDispatcher<CommandSource> dispatcher) {
    this.dispatcher = dispatcher;
  }

  public CommandCarrier register() {
    this.dispatcher.register(builder(Commands.literal("carrier").requires(p -> p.hasPermissionLevel(2))));
    return this;
  }

  public LiteralArgumentBuilder<CommandSource> builder(LiteralArgumentBuilder<CommandSource> builder) {
    builder.executes(c -> {
      c.getSource().sendFeedback(new StringTextComponent("/carrier carriers"), true);
      return 1;
    });
    builder.then(Commands.literal("carriers").executes(c -> {
      ServerWorld world = c.getSource().getWorld();
      ModEntities.BEEHEMOTH.get().spawn(world, null, c.getSource().asPlayer(), c.getSource().asPlayer().getPosition().up().up(), SpawnReason.SPAWN_EGG, true, true);
      ModEntities.BEEHEMOTH.get().spawn(world, null, c.getSource().asPlayer(), c.getSource().asPlayer().getPosition().up().up(), SpawnReason.SPAWN_EGG, true, true);
      return 1;
    }));
    return builder;
  }
}

