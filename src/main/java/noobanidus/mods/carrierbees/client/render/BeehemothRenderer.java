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
  public void render(BeehemothEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack stack, IRenderTypeBuffer pBuffer, int pPackedLight) {
    stack.pushPose();
    float scale = 1.6f;
    stack.scale(scale, scale, scale);
    super.render(pEntity, pEntityYaw, pPartialTicks, stack, pBuffer, pPackedLight);
    stack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(BeehemothEntity bee) {
    return SKIN;
  }
}
