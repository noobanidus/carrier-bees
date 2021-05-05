package noobanidus.mods.carrierbees.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.client.model.BeehemothModel;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;

@OnlyIn(Dist.CLIENT)
public class BeehemothRenderer extends MobRenderer<BeehemothEntity, BeehemothModel> {
  private static final ResourceLocation SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/beehemoth.png");

  public BeehemothRenderer(EntityRendererManager bee) {
    super(bee, new BeehemothModel(), 0.4F);
  }

  @Override
  public void render(BeehemothEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
    stack.push();
    float scale = 1.6f;
    stack.scale(scale, scale, scale);
    super.render(p_225623_1_, p_225623_2_, p_225623_3_, stack, p_225623_5_, p_225623_6_);
    stack.pop();
  }

  @Override
  public ResourceLocation getEntityTexture(BeehemothEntity bee) {
    return SKIN;
  }
}
