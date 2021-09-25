package noobanidus.mods.carrierbees.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.client.layers.BeeHeldItemLayer;
import noobanidus.mods.carrierbees.client.model.GenericBeeModel;
import noobanidus.mods.carrierbees.entities.GenericBeeEntity;

@OnlyIn(Dist.CLIENT)
public class GenericBeeRenderer extends MobRenderer<GenericBeeEntity, GenericBeeModel> {
  public static final ResourceLocation GENERIC = new ResourceLocation(CarrierBees.MODID, "textures/entity/genericbee.png");
  public static final ResourceLocation GENERIC_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/genericbee_angry.png");

  public GenericBeeRenderer(EntityRendererManager p_i226033_1_) {
    super(p_i226033_1_, new GenericBeeModel(), 0.4F);
    this.addLayer(new BeeHeldItemLayer<>(this));
  }

  public ResourceLocation getTextureLocation(GenericBeeEntity pEntity) {
    if (pEntity.isAngry()) {
      return GENERIC_ANGRY;
    }
    return GENERIC;
}
}
