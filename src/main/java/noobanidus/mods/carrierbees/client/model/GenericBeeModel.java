package noobanidus.mods.carrierbees.client.model;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import noobanidus.mods.carrierbees.entities.GenericBeeEntity;

public class GenericBeeModel extends BodyModel<GenericBeeEntity> {
  private final ModelRenderer rightAntenna;
  private final ModelRenderer leftAntenna;
  private final ModelRenderer stinger;
  private final ModelRenderer rightWing;
  private final ModelRenderer leftWing;
  private final ModelRenderer frontLegs;
  private final ModelRenderer middleLegs;
  private final ModelRenderer backLegs;
  private final ModelRenderer teeth;
  private final ModelRenderer tutu_up;
  private final ModelRenderer tutu_down;
  private final ModelRenderer tutu_east;
  private final ModelRenderer tutu_west;
  private final ModelRenderer cap;
  private float bodyPitch;

  public GenericBeeModel() {
    textureWidth = 64;
    textureHeight = 64;

    body = new ModelRenderer(this);
    body.setRotationPoint(0.5F, 19.0F, 0.0F);
    body.setTextureOffset(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.0F, false);

    rightAntenna = new ModelRenderer(this);
    rightAntenna.setRotationPoint(-0.5F, 5.0F, 0.0F);
    body.addChild(rightAntenna);
    rightAntenna.setTextureOffset(2, 3).addBox(-2.0F, -9.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

    leftAntenna = new ModelRenderer(this);
    leftAntenna.setRotationPoint(-0.5F, 5.0F, 0.0F);
    body.addChild(leftAntenna);
    leftAntenna.setTextureOffset(2, 0).addBox(2.0F, -9.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

    stinger = new ModelRenderer(this);
    stinger.setRotationPoint(0.0F, -1.0F, 1.0F);
    body.addChild(stinger);
    stinger.setTextureOffset(26, 7).addBox(0.0F, 0.0F, 4.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);

    rightWing = new ModelRenderer(this);
    rightWing.setRotationPoint(-1.5F, -4.0F, -3.0F);
    body.addChild(rightWing);
    setRotationAngle(rightWing, 0.2618F, -0.2618F, 0.0F);
    rightWing.setTextureOffset(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

    leftWing = new ModelRenderer(this);
    leftWing.setRotationPoint(1.5F, -4.0F, -3.0F);
    body.addChild(leftWing);
    setRotationAngle(leftWing, 0.2618F, 0.2618F, 0.0F);
    leftWing.setTextureOffset(9, 24).addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

    frontLegs = new ModelRenderer(this);
    frontLegs.setRotationPoint(1.5F, 3.0F, -2.0F);
    body.addChild(frontLegs);
    frontLegs.setTextureOffset(26, 1).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    middleLegs = new ModelRenderer(this);
    middleLegs.setRotationPoint(1.5F, 3.0F, 0.0F);
    body.addChild(middleLegs);
    middleLegs.setTextureOffset(26, 3).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    backLegs = new ModelRenderer(this);
    backLegs.setRotationPoint(1.5F, 3.0F, 2.0F);
    body.addChild(backLegs);
    backLegs.setTextureOffset(26, 5).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    teeth = new ModelRenderer(this);
    teeth.setRotationPoint(-0.5F, 5.0F, 0.0F);
    body.addChild(teeth);
    teeth.setTextureOffset(59, 1).addBox(0.75F, -3.25F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    teeth.setTextureOffset(59, 1).addBox(0.75F, -2.75F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    teeth.setTextureOffset(57, 0).addBox(-0.75F, -3.25F, -5.25F, 1.0F, 2.0F, 1.0F, 0.0F, false);

    tutu_up = new ModelRenderer(this);
    tutu_up.setRotationPoint(0.0F, -4.75F, 5.0F);
    body.addChild(tutu_up);
    setRotationAngle(tutu_up, 0.3491F, 0.0F, 0.0F);
    tutu_up.setTextureOffset(42, 3).addBox(-3.5F, -0.75F, -4.25F, 7.0F, 0.0F, 4.0F, 0.0F, false);

    tutu_down = new ModelRenderer(this);
    tutu_down.setRotationPoint(0.0F, 3.75F, 5.0F);
    body.addChild(tutu_down);
    setRotationAngle(tutu_down, -0.3491F, 0.0F, 0.0F);
    tutu_down.setTextureOffset(42, 7).addBox(-3.5F, 0.75F, -4.25F, 7.0F, 0.0F, 4.0F, 0.0F, false);

    tutu_east = new ModelRenderer(this);
    tutu_east.setRotationPoint(-4.5F, -0.5F, 5.0F);
    body.addChild(tutu_east);
    setRotationAngle(tutu_east, 0.0F, -0.3491F, 0.0F);
    tutu_east.setTextureOffset(48, 11).addBox(-0.5F, -3.5F, -4.25F, 0.0F, 7.0F, 4.0F, 0.0F, false);

    tutu_west = new ModelRenderer(this);
    tutu_west.setRotationPoint(4.25F, -0.5F, 5.0F);
    body.addChild(tutu_west);
    setRotationAngle(tutu_west, 0.0F, 0.3491F, 0.0F);
    tutu_west.setTextureOffset(56, 11).addBox(0.75F, -3.5F, -4.25F, 0.0F, 7.0F, 4.0F, 0.0F, false);

    cap = new ModelRenderer(this);
    cap.setRotationPoint(0.0F, -5.0F, -4.0F);
    body.addChild(cap);
    setRotationAngle(cap, 0.0F, -0.2618F, 0.0F);
    cap.setTextureOffset(50, 22).addBox(-1.5F, 0.0F, -2.25F, 3.0F, 1.0F, 4.0F, 0.0F, false);
    cap.setTextureOffset(52, 27).addBox(-1.5F, -1.0F, -1.25F, 3.0F, 1.0F, 3.0F, 0.0F, false);
  }

  @Override
  public void setLivingAnimations(GenericBeeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
    super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
    this.bodyPitch = entity.getBodyPitch(partialTicks);
    this.stinger.showModel = !entity.hasStung();
  }


  @Override
  public void setRotationAngles(GenericBeeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.rightWing.rotateAngleX = 0.0F;
    this.leftAntenna.rotateAngleX = 0.0F;
    this.rightAntenna.rotateAngleX = 0.0F;
    this.body.rotateAngleX = 0.0F;
    this.body.rotationPointY = 19.0F;
    boolean onGround = entity.isOnGround() && entity.getMotion().lengthSquared() < 1.0E-7D;
    float v1;
    if (onGround) {
      this.rightWing.rotateAngleY = -0.2618F;
      this.rightWing.rotateAngleZ = 0.0F;
      this.leftWing.rotateAngleX = 0.0F;
      this.leftWing.rotateAngleY = 0.2618F;
      this.leftWing.rotateAngleZ = 0.0F;
      this.frontLegs.rotateAngleX = 0.0F;
      this.middleLegs.rotateAngleX = 0.0F;
      this.backLegs.rotateAngleX = 0.0F;
    } else {
      v1 = ageInTicks * 2.1F;
      this.rightWing.rotateAngleY = 0.0F;
      this.rightWing.rotateAngleZ = MathHelper.cos(v1) * 3.1415927F * 0.15F;
      this.leftWing.rotateAngleX = this.rightWing.rotateAngleX;
      this.leftWing.rotateAngleY = this.rightWing.rotateAngleY;
      this.leftWing.rotateAngleZ = -this.rightWing.rotateAngleZ;
      this.frontLegs.rotateAngleX = 0.7853982F;
      this.middleLegs.rotateAngleX = 0.7853982F;
      this.backLegs.rotateAngleX = 0.7853982F;
      this.body.rotateAngleX = 0.0F;
      this.body.rotateAngleY = 0.0F;
      this.body.rotateAngleZ = 0.0F;
      this.cap.rotateAngleY = -0.2618f;
    }

    if (!entity.isAngry()) {
      this.body.rotateAngleX = 0.0F;
      this.body.rotateAngleY = 0.0F;
      this.body.rotateAngleZ = 0.0F;
      this.cap.rotateAngleY = -2.9671F;
      if (!onGround) {
        float f1 = MathHelper.cos(ageInTicks * 0.18F);
        this.body.rotateAngleX = 0.1F + f1 * (float) Math.PI * 0.025F;
        this.leftAntenna.rotateAngleX = f1 * (float) Math.PI * 0.03F;
        this.rightAntenna.rotateAngleX = f1 * (float) Math.PI * 0.03F;
        this.frontLegs.rotateAngleX = -f1 * (float) Math.PI * 0.1F + ((float) Math.PI / 8F);
        this.backLegs.rotateAngleX = -f1 * (float) Math.PI * 0.05F + ((float) Math.PI / 4F);
        this.body.rotationPointY = 19.0F - MathHelper.cos(ageInTicks * 0.18F) * 0.9F;
      }
    }

    if (this.bodyPitch > 0.0F) {
      this.body.rotateAngleX = ModelUtils.func_228283_a_(this.body.rotateAngleX, 3.0915928F, this.bodyPitch);
    }
  }

  @Override
  protected Iterable<ModelRenderer> getHeadParts() {
    return ImmutableList.of();
  }

  @Override
  protected Iterable<ModelRenderer> getBodyParts() {
    return ImmutableList.of(this.body);
  }

  public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }
}