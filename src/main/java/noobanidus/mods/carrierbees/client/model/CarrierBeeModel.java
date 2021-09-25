package noobanidus.mods.carrierbees.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;

@OnlyIn(Dist.CLIENT)
public class CarrierBeeModel<T extends AppleBeeEntity> extends BodyModel<T> {
  private final ModelRenderer torso;
  private final ModelRenderer rightWing;
  private final ModelRenderer leftWing;
  private final ModelRenderer frontLegs;
  private final ModelRenderer middleLegs;
  private final ModelRenderer backLegs;
  private final ModelRenderer stinger;
  private final ModelRenderer leftAntenna;
  private final ModelRenderer rightAntenna;
  private float bodyPitch;

  public CarrierBeeModel() {
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
  }

  @Override
  public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
    super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
    this.bodyPitch = entity.getBodyPitch(partialTicks);
    this.stinger.visible = !entity.hasStung();
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.rightWing.xRot = 0.0F;
    this.leftAntenna.xRot = 0.0F;
    this.rightAntenna.xRot = 0.0F;
    this.body.xRot = 0.0F;
    this.body.y = 19.0F;
    boolean onGround = entity.isOnGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7D;
    float v1;
    if (onGround) {
      this.rightWing.yRot = -0.2618F;
      this.rightWing.zRot = 0.0F;
      this.leftWing.xRot = 0.0F;
      this.leftWing.yRot = 0.2618F;
      this.leftWing.zRot = 0.0F;
      this.frontLegs.xRot = 0.0F;
      this.middleLegs.xRot = 0.0F;
      this.backLegs.xRot = 0.0F;
    } else {
      v1 = ageInTicks * 2.1F;
      this.rightWing.yRot = 0.0F;
      this.rightWing.zRot = MathHelper.cos(v1) * 3.1415927F * 0.15F;
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

    if (!entity.isAngry()) {
      this.body.xRot = 0.0F;
      this.body.yRot = 0.0F;
      this.body.zRot = 0.0F;
      if (!onGround) {
        float f1 = MathHelper.cos(ageInTicks * 0.18F);
        this.body.xRot = 0.1F + f1 * (float) Math.PI * 0.025F;
        this.leftAntenna.xRot = f1 * (float) Math.PI * 0.03F;
        this.rightAntenna.xRot = f1 * (float) Math.PI * 0.03F;
        this.frontLegs.xRot = -f1 * (float) Math.PI * 0.1F + ((float) Math.PI / 8F);
        this.backLegs.xRot = -f1 * (float) Math.PI * 0.05F + ((float) Math.PI / 4F);
        this.body.y = 19.0F - MathHelper.cos(ageInTicks * 0.18F) * 0.9F;
      }
    }

    if (this.bodyPitch > 0.0F) {
      this.body.xRot = ModelUtils.rotlerpRad(this.body.xRot, 3.0915928F, this.bodyPitch);
    }
  }

  @Override
  protected Iterable<ModelRenderer> headParts() {
    return ImmutableList.of();
  }

  @Override
  protected Iterable<ModelRenderer> bodyParts() {
    return ImmutableList.of(this.body);
  }
}
