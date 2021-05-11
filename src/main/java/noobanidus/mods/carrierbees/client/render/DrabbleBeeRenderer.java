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
import noobanidus.mods.carrierbees.client.model.DrabbleBeeModel;
import noobanidus.mods.carrierbees.entities.DrabbleBeeEntity;

@OnlyIn(Dist.CLIENT)
public class DrabbleBeeRenderer extends MobRenderer<DrabbleBeeEntity, DrabbleBeeModel<DrabbleBeeEntity>> {
  public static final ResourceLocation DRABBLE = new ResourceLocation(CarrierBees.MODID, "textures/entity/drabblebee.png");

  public DrabbleBeeRenderer(EntityRendererManager p_i226033_1_) {
    super(p_i226033_1_, new DrabbleBeeModel<>(), 0.4F);
  }

  public ResourceLocation getEntityTexture(DrabbleBeeEntity p_110775_1_) {
    return DRABBLE;
}
}
