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
    texWidth = 64;
    texHeight = 64;

    body = new ModelRenderer(this);
    body.setPos(0.5F, 19.0F, 0.0F);
    body.texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.0F, false);

    rightAntenna = new ModelRenderer(this);
    rightAntenna.setPos(-0.5F, 5.0F, 0.0F);
    body.addChild(rightAntenna);
    rightAntenna.texOffs(2, 3).addBox(-2.0F, -9.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

    leftAntenna = new ModelRenderer(this);
    leftAntenna.setPos(-0.5F, 5.0F, 0.0F);
    body.addChild(leftAntenna);
    leftAntenna.texOffs(2, 0).addBox(2.0F, -9.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

    stinger = new ModelRenderer(this);
    stinger.setPos(0.0F, -1.0F, 1.0F);
    body.addChild(stinger);
    stinger.texOffs(26, 7).addBox(0.0F, 0.0F, 4.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);

    rightWing = new ModelRenderer(this);
    rightWing.setPos(-1.5F, -4.0F, -3.0F);
    body.addChild(rightWing);
    setRotationAngle(rightWing, 0.2618F, -0.2618F, 0.0F);
    rightWing.texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

    leftWing = new ModelRenderer(this);
    leftWing.setPos(1.5F, -4.0F, -3.0F);
    body.addChild(leftWing);
    setRotationAngle(leftWing, 0.2618F, 0.2618F, 0.0F);
    leftWing.texOffs(9, 24).addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

    frontLegs = new ModelRenderer(this);
    frontLegs.setPos(1.5F, 3.0F, -2.0F);
    body.addChild(frontLegs);
    frontLegs.texOffs(26, 1).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    middleLegs = new ModelRenderer(this);
    middleLegs.setPos(1.5F, 3.0F, 0.0F);
    body.addChild(middleLegs);
    middleLegs.texOffs(26, 3).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    backLegs = new ModelRenderer(this);
    backLegs.setPos(1.5F, 3.0F, 2.0F);
    body.addChild(backLegs);
    backLegs.texOffs(26, 5).addBox(-5.0F, 0.0F, 0.0F, 7.0F, 2.0F, 0.0F, 0.0F, false);

    teeth = new ModelRenderer(this);
    teeth.setPos(-0.5F, 5.0F, 0.0F);
    body.addChild(teeth);
    teeth.texOffs(59, 1).addBox(0.75F, -3.25F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    teeth.texOffs(59, 1).addBox(0.75F, -2.75F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    teeth.texOffs(57, 0).addBox(-0.75F, -3.25F, -5.25F, 1.0F, 2.0F, 1.0F, 0.0F, false);

    tutu_up = new ModelRenderer(this);
    tutu_up.setPos(0.0F, -4.75F, 5.0F);
    body.addChild(tutu_up);
    setRotationAngle(tutu_up, 0.3491F, 0.0F, 0.0F);
    tutu_up.texOffs(42, 3).addBox(-3.5F, -0.75F, -4.25F, 7.0F, 0.0F, 4.0F, 0.0F, false);

    tutu_down = new ModelRenderer(this);
    tutu_down.setPos(0.0F, 3.75F, 5.0F);
    body.addChild(tutu_down);
    setRotationAngle(tutu_down, -0.3491F, 0.0F, 0.0F);
    tutu_down.texOffs(42, 7).addBox(-3.5F, 0.75F, -4.25F, 7.0F, 0.0F, 4.0F, 0.0F, false);

    tutu_east = new ModelRenderer(this);
    tutu_east.setPos(-4.5F, -0.5F, 5.0F);
    body.addChild(tutu_east);
    setRotationAngle(tutu_east, 0.0F, -0.3491F, 0.0F);
    tutu_east.texOffs(48, 11).addBox(-0.5F, -3.5F, -4.25F, 0.0F, 7.0F, 4.0F, 0.0F, false);

    tutu_west = new ModelRenderer(this);
    tutu_west.setPos(4.25F, -0.5F, 5.0F);
    body.addChild(tutu_west);
    setRotationAngle(tutu_west, 0.0F, 0.3491F, 0.0F);
    tutu_west.texOffs(56, 11).addBox(0.75F, -3.5F, -4.25F, 0.0F, 7.0F, 4.0F, 0.0F, false);

    cap = new ModelRenderer(this);
    cap.setPos(0.0F, -5.0F, -4.0F);
    body.addChild(cap);
    setRotationAngle(cap, 0.0F, -0.2618F, 0.0F);
    cap.texOffs(50, 22).addBox(-1.5F, 0.0F, -2.25F, 3.0F, 1.0F, 4.0F, 0.0F, false);
    cap.texOffs(52, 27).addBox(-1.5F, -1.0F, -1.25F, 3.0F, 1.0F, 3.0F, 0.0F, false);
  }

  @Override
  public void prepareMobModel(GenericBeeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
    super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
    this.bodyPitch = entity.getBodyPitch(partialTicks);
    this.stinger.visible = !entity.hasStung();
  }


  @Override
  public void setupAnim(GenericBeeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
      this.cap.yRot = -0.2618f;
    }

    if (!entity.isAngry()) {
      this.body.xRot = 0.0F;
      this.body.yRot = 0.0F;
      this.body.zRot = 0.0F;
      this.cap.yRot = -2.9671F;
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

  public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.xRot = x;
    modelRenderer.yRot = y;
    modelRenderer.zRot = z;
  }
}