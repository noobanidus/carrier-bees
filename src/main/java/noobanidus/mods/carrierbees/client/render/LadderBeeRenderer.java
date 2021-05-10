package noobanidus.mods.carrierbees.client.render;

import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.CarrierBees;
import noobanidus.mods.carrierbees.client.model.LadderBeeModel;
import noobanidus.mods.carrierbees.entities.LadderBeeEntity;

@OnlyIn(Dist.CLIENT)
public class LadderBeeRenderer extends MobRenderer<LadderBeeEntity, LadderBeeModel<LadderBeeEntity>> {
  public static final ResourceLocation LADDER = new ResourceLocation(CarrierBees.MODID, "textures/entity/ladder_bee.png");

  public LadderBeeRenderer(EntityRendererManager p_i226033_1_) {
    super(p_i226033_1_, new LadderBeeModel<>(), 0.4F);
  }

  public ResourceLocation getEntityTexture(LadderBeeEntity p_110775_1_) {
    return LADDER;
}
}
