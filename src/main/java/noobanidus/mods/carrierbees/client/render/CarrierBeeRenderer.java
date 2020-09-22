package noobanidus.mods.carrierbees.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.client.layers.BeeHeldItemLayer;
import noobanidus.mods.carrierbees.client.model.CarrierBeeModel;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;
import noobanidus.mods.carrierbees.entities.FumbleCarrierBeeEntity;

@OnlyIn(Dist.CLIENT)
public class CarrierBeeRenderer extends MobRenderer<AppleBeeEntity, CarrierBeeModel<AppleBeeEntity>> {
  private static final ResourceLocation SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/carrier_bee.png");
  private static final ResourceLocation ANGRY_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/carrier_bee_angry.png");
  private static final ResourceLocation FUMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/fumblebee.png");
  private static final ResourceLocation FUMBLE_ANGRY_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/fumblebee_angry.png");

  public CarrierBeeRenderer(EntityRendererManager bee) {
    super(bee, new CarrierBeeModel<>(), 0.4F);
    this.addLayer(new BeeHeldItemLayer<>(this));
  }

  @Override
  public void render(AppleBeeEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
    stack.push();
    stack.scale(1.5f, 1.5f, 1.5f);
    super.render(p_225623_1_, p_225623_2_, p_225623_3_, stack, p_225623_5_, p_225623_6_);
    stack.pop();
  }

  @Override
  public ResourceLocation getEntityTexture(AppleBeeEntity bee) {
    if (bee instanceof FumbleCarrierBeeEntity) {
      if (bee.isAngry()) {
        return FUMBLE_ANGRY_SKIN;
      }
      return FUMBLE_SKIN;
    }
    if (bee.isAngry()) {
      return ANGRY_SKIN;
    }
    return SKIN;
  }
}
