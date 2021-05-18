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
import noobanidus.mods.carrierbees.entities.*;

@OnlyIn(Dist.CLIENT)
public class CarrierBeeRenderer extends MobRenderer<AppleBeeEntity, CarrierBeeModel<AppleBeeEntity>> {
  private static final ResourceLocation SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/carrier_bee.png");
  private static final ResourceLocation ANGRY_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/carrier_bee_angry.png");
  private static final ResourceLocation FUMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/fumblebee.png");
  private static final ResourceLocation FUMBLE_ANGRY_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/fumblebee_angry.png");
  private static final ResourceLocation BOMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/bomblebee.png");
  private static final ResourceLocation BOMBLE_SKIN_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/bomblebee_angry.png");
  private static final ResourceLocation STUMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/stumblebee.png");
  private static final ResourceLocation STUMBLE_SKIN_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/stumblebee_angry.png");
  private static final ResourceLocation CRUMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/crumblebee.png");
  private static final ResourceLocation CRUMBLE_SKIN_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/crumblebee_angry.png");
  private static final ResourceLocation DRUMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/drumblebee.png");
  private static final ResourceLocation DRUMBLE_SKIN_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/drumblebee_angry.png");
  private static final ResourceLocation TUMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/tumblebee.png");
  private static final ResourceLocation TUMBLE_SKIN_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/tumblebee_angry.png");
  private static final ResourceLocation THIMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/thimblebee.png");
  private static final ResourceLocation THIMBLE_SKIN_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/thimblebee_angry.png");
  private static final ResourceLocation JUMBLE_SKIN = new ResourceLocation(CarrierBees.MODID, "textures/entity/jumblebee.png");
  private static final ResourceLocation JUMBLE_SKIN_ANGRY = new ResourceLocation(CarrierBees.MODID, "textures/entity/jumblebee_angry.png");

  public CarrierBeeRenderer(EntityRendererManager bee, CarrierBeeModel<AppleBeeEntity> model) {
    super(bee, model, 0.4F);
    this.addLayer(new BeeHeldItemLayer<>(this));
  }

  public CarrierBeeRenderer(EntityRendererManager bee) {
    super(bee, new CarrierBeeModel<>(), 0.4F);
    this.addLayer(new BeeHeldItemLayer<>(this));
  }

  @Override
  public void render(AppleBeeEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
    stack.push();
    float scale = 1.5f;
    if (p_225623_1_ instanceof BombleBeeEntity) {
      scale = 1.9f;
    } else if (p_225623_1_ instanceof CrumbleBeeEntity) {
      scale = 2.1f;
    } else if (p_225623_1_ instanceof DrumbleBeeEntity) {
      scale = 3.5f;
    } else if (p_225623_1_ instanceof TumbleBeeEntity) {
      scale = 1.1f;
    } else if (p_225623_1_ instanceof ThimbleBeeEntity) {
      scale = 0.4f;
    } else if (p_225623_1_ instanceof GenericBeeEntity) {
      scale = 1.1f;
    }
    stack.scale(scale, scale, scale);
    super.render(p_225623_1_, p_225623_2_, p_225623_3_, stack, p_225623_5_, p_225623_6_);
    stack.pop();
  }

  @Override
  public ResourceLocation getEntityTexture(AppleBeeEntity bee) {
    if (bee instanceof FumbleBeeEntity) {
      if (bee.isAngry()) {
        return FUMBLE_ANGRY_SKIN;
      }
      return FUMBLE_SKIN;
    }
    if (bee instanceof BombleBeeEntity) {
      if (bee.isAngry()) {
        return BOMBLE_SKIN_ANGRY;
      }
      return BOMBLE_SKIN;
    }
    if (bee instanceof StumbleBeeEntity) {
      if (bee.isAngry()) {
        return STUMBLE_SKIN_ANGRY;
      }
      return STUMBLE_SKIN;
    }
    if (bee instanceof CrumbleBeeEntity) {
      if (bee.isAngry()) {
        return CRUMBLE_SKIN_ANGRY;
      }
      return CRUMBLE_SKIN;
    }
    if (bee instanceof DrumbleBeeEntity) {
      if (bee.isAngry()) {
        return DRUMBLE_SKIN_ANGRY;
      }
      return DRUMBLE_SKIN;
    }
    if (bee instanceof TumbleBeeEntity) {
      if (bee.isAngry()) {
        return TUMBLE_SKIN_ANGRY;
      }
      return TUMBLE_SKIN;
    }
    if (bee instanceof ThimbleBeeEntity) {
      if (bee.isAngry()) {
        return THIMBLE_SKIN_ANGRY;
      }
      return THIMBLE_SKIN;
    }
    if (bee instanceof JumbleBeeEntity) {
      if (bee.isAngry()) {
        return JUMBLE_SKIN_ANGRY;
      }
      return JUMBLE_SKIN;
    }
    if (bee.isAngry()) {
      return ANGRY_SKIN;
    }
    return SKIN;
  }
}
