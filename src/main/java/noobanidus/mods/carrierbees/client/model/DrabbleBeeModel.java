package noobanidus.mods.carrierbees.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.DrabbleBeeEntity;

/**
 * BeeModel - Either Mojang or a mod author (Taken From Memory)
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class DrabbleBeeModel<T extends DrabbleBeeEntity> extends BodyModel<T> {
  public ModelRenderer torso;
  public ModelRenderer rightWing;
  public ModelRenderer leftWing;
  public ModelRenderer frontLegs;
  public ModelRenderer middleLegs;
  public ModelRenderer backLegs;
  public ModelRenderer hat1;
  public ModelRenderer stinger;
  public ModelRenderer leftAntenna;
  public ModelRenderer rightAntenna;
  public ModelRenderer hat2;
  public ModelRenderer hat3;
  private float bodyPitch;

  public DrabbleBeeModel() {
    super(false, 24.0F, 0.0F);
    this.texWidth = 64;
    this.texHeight = 64;
    this.body = new ModelRenderer(this);
    this.body.setPos(0.0F, 19.0F, 0.0F);
    this.torso = new ModelRenderer(this, 0, 0);
    this.torso.setPos(0.0F, 0.0F, 0.0F);
    this.body.addChild(this.torso);
    this.torso.addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.0F);
    this.stinger = new ModelRenderer(this, 26, 7);
    this.stinger.addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F, 0.0F);
    this.torso.addChild(this.stinger);
    this.leftAntenna = new ModelRenderer(this, 2, 0);
    this.leftAntenna.setPos(0.0F, -2.0F, -5.0F);
    this.leftAntenna.addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F);
    this.rightAntenna = new ModelRenderer(this, 2, 3);
    this.rightAntenna.setPos(0.0F, -2.0F, -5.0F);
    this.rightAntenna.addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F);
    this.torso.addChild(this.leftAntenna);
    this.torso.addChild(this.rightAntenna);
    this.rightWing = new ModelRenderer(this, 0, 18);
    this.rightWing.setPos(-1.5F, -4.0F, -3.0F);
    this.rightWing.xRot = 0.0F;
    this.rightWing.yRot = -0.2618F;
    this.rightWing.zRot = 0.0F;
    this.body.addChild(this.rightWing);
    this.rightWing.addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.001F);
    this.leftWing = new ModelRenderer(this, 0, 18);
    this.leftWing.setPos(1.5F, -4.0F, -3.0F);
    this.leftWing.xRot = 0.0F;
    this.leftWing.yRot = 0.2618F;
    this.leftWing.zRot = 0.0F;
    this.leftWing.mirror = true;
    this.body.addChild(this.leftWing);
    this.leftWing.addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.001F);
    this.frontLegs = new ModelRenderer(this);
    this.frontLegs.setPos(1.5F, 3.0F, -2.0F);
    this.body.addChild(this.frontLegs);
    this.frontLegs.addBox("frontLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 1);
    this.middleLegs = new ModelRenderer(this);
    this.middleLegs.setPos(1.5F, 3.0F, 0.0F);
    this.body.addChild(this.middleLegs);
    this.middleLegs.addBox("midLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 3);
    this.backLegs = new ModelRenderer(this);
    this.backLegs.setPos(1.5F, 3.0F, 2.0F);
    this.body.addChild(this.backLegs);
    this.backLegs.addBox("backLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 5);

    this.hat1 = new ModelRenderer(this, 0, 0);
    this.hat1.setPos(0.0F, -4.0F, -5.0F);
    this.hat1.addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
    this.hat2 = new ModelRenderer(this, 44, 0);
    this.hat2.setPos(0.0F, 0.0F, 0.0F);
    this.hat2.addBox(-2.0F, -3.0F, 0.1F, 4.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
    this.hat3 = new ModelRenderer(this, 40, 6);
    this.hat3.setPos(0.0F, 0.0F, 0.0F);
    this.hat3.addBox(-3.0F, -1.0F, -0.5F, 6.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
    this.body.addChild(this.hat1);
    this.hat1.addChild(this.hat2);
    this.hat1.addChild(this.hat3);
  }

  @Override
  public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    ImmutableList.of(this.body).forEach((modelRenderer) -> {
      modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    });
  }

  public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
    super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
    this.bodyPitch = pEntity.getBodyPitch(pPartialTick);
    this.stinger.visible = false;
  }

  public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    this.rightWing.xRot = 0.0F;
    this.leftAntenna.xRot = 0.0F;
    this.rightAntenna.xRot = 0.0F;
    this.body.xRot = 0.0F;
    this.body.y = 19.0F;
    boolean lvt_7_1_ = pEntity.isOnGround() && pEntity.getDeltaMovement().lengthSqr() < 1.0E-7D;
    float lvt_8_2_;
    if (lvt_7_1_) {
      this.rightWing.yRot = -0.2618F;
      this.rightWing.zRot = 0.0F;
      this.leftWing.xRot = 0.0F;
      this.leftWing.yRot = 0.2618F;
      this.leftWing.zRot = 0.0F;
      this.frontLegs.xRot = 0.0F;
      this.middleLegs.xRot = 0.0F;
      this.backLegs.xRot = 0.0F;
    } else {
      lvt_8_2_ = pAgeInTicks * 2.1F;
      this.rightWing.yRot = 0.0F;
      this.rightWing.zRot = MathHelper.cos(lvt_8_2_) * 3.1415927F * 0.15F;
      this.leftWing.xRot = this.rightWing.xRot;
      this.leftWing.yRot = this.rightWing.yRot;
      this.leftWing.zRot = -this.rightWing.zRot;
      this.frontLegs.xRot = 0.7853982F;
      this.middleLegs.xRot = 0.7853982F;
      this.backLegs.xRot = 0.7853982F;
      this.body.xRot = 0.0F;
      this.body.yRot = 0.0F;
      this.body.zRot = 0.0F;
    }

    this.body.xRot = 0.0F;
    this.body.yRot = 0.0F;
    this.body.zRot = 0.0F;
    if (!lvt_7_1_) {
      lvt_8_2_ = MathHelper.cos(pAgeInTicks * 0.18F);
      this.body.xRot = 0.1F + lvt_8_2_ * 3.1415927F * 0.025F;
      this.leftAntenna.xRot = lvt_8_2_ * 3.1415927F * 0.03F;
      this.rightAntenna.xRot = lvt_8_2_ * 3.1415927F * 0.03F;
      this.frontLegs.xRot = -lvt_8_2_ * 3.1415927F * 0.1F + 0.3926991F;
      this.backLegs.xRot = -lvt_8_2_ * 3.1415927F * 0.05F + 0.7853982F;
      this.body.y = 19.0F - MathHelper.cos(pAgeInTicks * 0.18F) * 0.9F;
    }

    if (this.bodyPitch > 0.0F) {
      this.body.xRot = ModelUtils.rotlerpRad(this.body.xRot, 3.0915928F, this.bodyPitch);
    }
  }

  protected Iterable<ModelRenderer> headParts() {
    return ImmutableList.of();
  }

  protected Iterable<ModelRenderer> bodyParts() {
    return ImmutableList.of(this.body);
  }
}
