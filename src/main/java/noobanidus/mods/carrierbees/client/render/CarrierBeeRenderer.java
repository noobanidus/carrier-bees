package noobanidus.mods.carrierbees.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.client.layers.BeeHeldItemLayer;
import noobanidus.mods.carrierbees.client.model.CarrierBeeModel;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;

@OnlyIn(Dist.CLIENT)
public class CarrierBeeRenderer extends MobRenderer<CarrierBeeEntity, CarrierBeeModel<CarrierBeeEntity>> {
  private static final ResourceLocation ANGRY_SKIN = new ResourceLocation("textures/entity/bee/bee_angry.png");
  private static final ResourceLocation PASSIVE_SKIN = new ResourceLocation("textures/entity/bee/bee.png");

  public CarrierBeeRenderer(EntityRendererManager bee) {
    super(bee, new CarrierBeeModel<>(), 0.4F);
    this.addLayer(new BeeHeldItemLayer<>(this));
  }

  @Override
  public void render(CarrierBeeEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
    stack.push();
    stack.scale(1.5f, 1.5f, 1.5f);
    super.render(p_225623_1_, p_225623_2_, p_225623_3_, stack, p_225623_5_, p_225623_6_);
    stack.pop();
  }

  public ResourceLocation getEntityTexture(CarrierBeeEntity bee) {
    if (bee.isAngry()) {
      return ANGRY_SKIN;
    } else {
      return PASSIVE_SKIN;
    }
  }
}
