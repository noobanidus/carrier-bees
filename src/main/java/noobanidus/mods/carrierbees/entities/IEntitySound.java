package noobanidus.mods.carrierbees.entities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface IEntitySound {
  @OnlyIn(Dist.CLIENT)
  boolean initSound();

  default Supplier<Callable<Boolean>> getSound() {
    return () -> this::initSound;
  }
}
