package noobanidus.mods.carrierbees.client.model;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import noobanidus.mods.carrierbees.entities.GenericBeeEntity;

import java.util.Iterator;

public class GenericBeeModel extends AgeableModel<GenericBeeEntity> {
  private final ModelRenderer body;
  private final ModelRenderer stinger;
  private final ModelRenderer rightwing_bone;
  private final ModelRenderer leftwing_bone;
  private final ModelRenderer leg_front;
  private final ModelRenderer leg_mid;
  private final ModelRenderer leg_back;
  private final ModelRenderer teeth;
  private final ModelRenderer tutu_up;
  private final ModelRenderer tutu_down;
  private final ModelRenderer tutu_east;
  private final ModelRenderer tutu_west;
  private final ModelRenderer cap;

  public GenericBeeModel() {
    textureWidth = 64;
    textureHeight = 64;

    body = new ModelRenderer(this);
    body.setRotationPoint(0.5F, 19.0F, 0.0F);
    body.setTextureOffset(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.0F, false);
    body.setTextureOffset(2, 0).addBox(1.5F, -4.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
    body.setTextureOffset(2, 3).addBox(-2.5F, -4.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

    stinger = new ModelRenderer(this);
    stinger.setRotationPoint(0.0F, -1.0F, 1.0F);
    body.addChild(stinger);
    stinger.setTextureOffset(26, 7).addBox(0.0F, 0.0F, 4.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);

    rightwing_bone = new ModelRenderer(this);
    rightwing_bone.setRotationPoint(-1.5F, -4.0F, -3.0F);
    body.addChild(rightwing_bone);
    setRotationAngle(rightwing_bone, 0.2618F, -0.2618F, 0.0F);
    rightwing_bone.setTextureOffset(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

    leftwing_bone = new ModelRenderer(this);
    leftwing_bone.setRotationPoint(1.5F, -4.0F, -3.0F);
    body.addChild(leftwing_bone);
    setRotationAngle(leftwing_bone, 0.2618F, 0.2618F, 0.0F);
    leftwing_bone.setTextureOffset(9, 24).addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

    leg_front = new ModelRenderer(this);
    leg_front.setRotationPoint(1.5F, 3.0F, -2.0F);
    body.addChild(leg_front);
    leg_front.setTextureOffset(26, 1).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    leg_mid = new ModelRenderer(this);
    leg_mid.setRotationPoint(1.5F, 3.0F, 0.0F);
    body.addChild(leg_mid);
    leg_mid.setTextureOffset(26, 3).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    leg_back = new ModelRenderer(this);
    leg_back.setRotationPoint(1.5F, 3.0F, 2.0F);
    body.addChild(leg_back);
    leg_back.setTextureOffset(26, 5).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    teeth = new ModelRenderer(this);
    teeth.setRotationPoint(-0.5F, 5.0F, 0.0F);
    body.addChild(teeth);
    teeth.setTextureOffset(59, 1).addBox(1.0F, -3.0F, -5.25F, 1.0F, 1.0F, 0.0F, 0.0F, false);
    teeth.setTextureOffset(57, 0).addBox(-1.0F, -3.0F, -5.25F, 1.0F, 2.0F, 0.0F, 0.0F, false);

    tutu_up = new ModelRenderer(this);
    tutu_up.setRotationPoint(0.0F, -4.75F, 5.0F);
    body.addChild(tutu_up);
    setRotationAngle(tutu_up, 0.3491F, 0.0F, 0.0F);
    tutu_up.setTextureOffset(42, 3).addBox(-3.5F, 0.25F, -1.75F, 7.0F, 0.0F, 4.0F, 0.0F, false);

    tutu_down = new ModelRenderer(this);
    tutu_down.setRotationPoint(0.0F, 3.75F, 5.0F);
    body.addChild(tutu_down);
    setRotationAngle(tutu_down, -0.3491F, 0.0F, 0.0F);
    tutu_down.setTextureOffset(42, 7).addBox(-3.5F, -0.25F, -1.75F, 7.0F, 0.0F, 4.0F, 0.0F, false);

    tutu_east = new ModelRenderer(this);
    tutu_east.setRotationPoint(-4.5F, -0.5F, 5.0F);
    body.addChild(tutu_east);
    setRotationAngle(tutu_east, 0.0F, -0.3491F, 0.0F);
    tutu_east.setTextureOffset(48, 11).addBox(0.5F, -3.5F, -1.75F, 0.0F, 7.0F, 4.0F, 0.0F, false);

    tutu_west = new ModelRenderer(this);
    tutu_west.setRotationPoint(4.25F, -0.5F, 5.0F);
    body.addChild(tutu_west);
    setRotationAngle(tutu_west, 0.0F, 0.3491F, 0.0F);
    tutu_west.setTextureOffset(56, 11).addBox(-0.25F, -3.5F, -1.75F, 0.0F, 7.0F, 4.0F, 0.0F, false);

    cap = new ModelRenderer(this);
    cap.setRotationPoint(0.0F, -5.0F, -4.0F);
    body.addChild(cap);
    setRotationAngle(cap, 0.0F, -0.2618F, 0.0F);
    cap.setTextureOffset(50, 22).addBox(-1.5F, 0.0F, -2.25F, 3.0F, 1.0F, 4.0F, 0.0F, false);
    cap.setTextureOffset(52, 27).addBox(-1.5F, -1.0F, -1.25F, 3.0F, 1.0F, 3.0F, 0.0F, false);
  }

  @Override
  public void setRotationAngles(GenericBeeEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.rightWing.rotateAngleX = 0.0F;
    this.leftAntenna.rotateAngleX = 0.0F;
    this.rightAntenna.rotateAngleX = 0.0F;
    this.body.rotateAngleX = 0.0F;
    this.body.rotationPointY = 19.0F;
    boolean lvt_7_1_ = p_225597_1_.isOnGround() && p_225597_1_.getMotion().lengthSquared() < 1.0E-7D;
    float lvt_8_2_;
    if (lvt_7_1_) {
      this.rightWing.rotateAngleY = -0.2618F;
      this.rightWing.rotateAngleZ = 0.0F;
      this.leftWing.rotateAngleX = 0.0F;
      this.leftWing.rotateAngleY = 0.2618F;
      this.leftWing.rotateAngleZ = 0.0F;
      this.frontLegs.rotateAngleX = 0.0F;
      this.middleLegs.rotateAngleX = 0.0F;
      this.backLegs.rotateAngleX = 0.0F;
    } else {
      lvt_8_2_ = p_225597_4_ * 2.1F;
      this.rightWing.rotateAngleY = 0.0F;
      this.rightWing.rotateAngleZ = MathHelper.cos(lvt_8_2_) * 3.1415927F * 0.15F;
      this.leftWing.rotateAngleX = this.rightWing.rotateAngleX;
      this.leftWing.rotateAngleY = this.rightWing.rotateAngleY;
      this.leftWing.rotateAngleZ = -this.rightWing.rotateAngleZ;
      this.frontLegs.rotateAngleX = 0.7853982F;
      this.middleLegs.rotateAngleX = 0.7853982F;
      this.backLegs.rotateAngleX = 0.7853982F;
      this.body.rotateAngleX = 0.0F;
      this.body.rotateAngleY = 0.0F;
      this.body.rotateAngleZ = 0.0F;
    }

    this.body.rotateAngleX = 0.0F;
    this.body.rotateAngleY = 0.0F;
    this.body.rotateAngleZ = 0.0F;
    if (!lvt_7_1_) {
      lvt_8_2_ = MathHelper.cos(p_225597_4_ * 0.18F);
      this.body.rotateAngleX = 0.1F + lvt_8_2_ * 3.1415927F * 0.025F;
      this.leftAntenna.rotateAngleX = lvt_8_2_ * 3.1415927F * 0.03F;
      this.rightAntenna.rotateAngleX = lvt_8_2_ * 3.1415927F * 0.03F;
      this.frontLegs.rotateAngleX = -lvt_8_2_ * 3.1415927F * 0.1F + 0.3926991F;
      this.backLegs.rotateAngleX = -lvt_8_2_ * 3.1415927F * 0.05F + 0.7853982F;
      this.body.rotationPointY = 19.0F - MathHelper.cos(p_225597_4_ * 0.18F) * 0.9F;
    }

    if (this.bodyPitch > 0.0F) {
      this.body.rotateAngleX = ModelUtils.func_228283_a_(this.body.rotateAngleX, 3.0915928F, this.bodyPitch);
    }
  }

  protected Iterable<ModelRenderer> getHeadParts() {
    return ImmutableList.of();
  }

  protected Iterable<ModelRenderer> getBodyParts() {
    return ImmutableList.of(this.body);
  }

  public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }
}