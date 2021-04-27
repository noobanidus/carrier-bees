package noobanidus.mods.carrierbees.client.model;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import noobanidus.mods.carrierbees.entities.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModelHolder<E extends AppleBeeEntity, T extends EntityModel<E>> implements Supplier<T> {
  public static ModelHolder<BombleBeeEntity, CarrierBeeModel<BombleBeeEntity>> BOMBLE = new ModelHolder<>(CarrierBeeModel::new);
  public static ModelHolder<CarrierBeeEntity, CarrierBeeModel<CarrierBeeEntity>> CARRIER = new ModelHolder<>(CarrierBeeModel::new);
  public static ModelHolder<CrumbleCarrierBeeEntity, CarrierBeeModel<CrumbleCarrierBeeEntity>> CRUMBLE = new ModelHolder<>(CarrierBeeModel::new);
  public static ModelHolder<DrumbleCarrierBeeEntity, CarrierBeeModel<DrumbleCarrierBeeEntity>> DRUMBLE = new ModelHolder<>(CarrierBeeModel::new);
  public static ModelHolder<TumbleCarrierBeeEntity, CarrierBeeModel<TumbleCarrierBeeEntity>> TUMBLE = new ModelHolder<>(CarrierBeeModel::new);
  public static ModelHolder<FumbleCarrierBeeEntity, CarrierBeeModel<FumbleCarrierBeeEntity>> FUMBLE = new ModelHolder<>(CarrierBeeModel::new);
  public static ModelHolder<StumbleCarrierBeeEntity, CarrierBeeModel<StumbleCarrierBeeEntity>> STUMBLE = new ModelHolder<>(CarrierBeeModel::new);
  public static ModelHolder<BeehemothEntity, BeehemothModel> BEEHEMOTH = new ModelHolder<>(BeehemothModel::new);
  private static List<ModelHolder<?, ?>> HOLDERS = Arrays.asList(BOMBLE, CARRIER, CRUMBLE, DRUMBLE, TUMBLE, FUMBLE, STUMBLE, BEEHEMOTH);

  private Supplier<T> builder;
  private T result = null;
  private MobRenderer<E, T> renderer;

  ModelHolder(Supplier<T> builder) {
    this.builder = builder;
  }

  @Override
  public T get() {
    if (result == null) {
      result = builder.get();
    }
    return result;
  }

  @Nullable
  public MobRenderer<E, T> getRenderer() {
    return renderer;
  }

  public MobRenderer<E, T> setRenderer(MobRenderer<E, T> renderer) {
    this.renderer = renderer;
    return renderer;
  }

  private void resetInternal() {
    this.result = null;
    get();
    if (this.renderer != null) {
      this.renderer.entityModel = this.result;
    }
  }

  private static void reset() {
    HOLDERS.forEach(ModelHolder::resetInternal);
  }

  public static class Loader implements ISelectiveResourceReloadListener {
    public static Loader INSTANCE = new Loader();

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
      if (resourcePredicate.test(VanillaResourceType.MODELS)) {
        reset();
      }
    }
  }
}
