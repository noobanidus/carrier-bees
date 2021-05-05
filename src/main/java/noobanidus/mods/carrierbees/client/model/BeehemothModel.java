package noobanidus.mods.carrierbees.client.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;

public class BeehemothModel extends EntityModel<BeehemothEntity> {
  private float bodyPitch;

  private final ModelRenderer ROOT;
  private final ModelRenderer FACE;
  private final ModelRenderer THORAX;
  private final ModelRenderer LEG_FRONTLEFT;
  private final ModelRenderer KNEE_FRONTLEFT;
  private final ModelRenderer KneeFrontLeftCube_r1;
  private final ModelRenderer LEG_MIDLEFT;
  private final ModelRenderer KNEE_MIDLEFT;
  private final ModelRenderer KneeMidLeftCube_r1;
  private final ModelRenderer LEG_REARLEFT;
  private final ModelRenderer KNEE_REARLEFT;
  private final ModelRenderer KneeRearLeftCube_r1;
  private final ModelRenderer LEG_FRONTRIGHT;
  private final ModelRenderer KNEE_FRONTRIGHT;
  private final ModelRenderer KneeFrontRightCube_r1;
  private final ModelRenderer LEG_MIDRIGHT;
  private final ModelRenderer KNEE_MIDRIGHT;
  private final ModelRenderer KneeMidRightCube_r1;
  private final ModelRenderer LEG_REARRIGHT;
  private final ModelRenderer KNEE_REARRIGHT;
  private final ModelRenderer KneeRearRightCube_r1;
  private final ModelRenderer WING_LEFT;
  private final ModelRenderer WingLeftPlane_r1;
  private final ModelRenderer WING_RIGHT;
  private final ModelRenderer WingRightPlane_r1;
  private final ModelRenderer ABDOMEN;
  private final ModelRenderer SADDLE;

  public BeehemothModel() {
    textureWidth = 64;
    textureHeight = 64;

    ROOT = new ModelRenderer(this);
    ROOT.setRotationPoint(0.0F, 24.0F, 0.0F);


    FACE = new ModelRenderer(this);
    FACE.setRotationPoint(0.0F, -8.0F, -6.0F);
    ROOT.addChild(FACE);
    setRotationAngle(FACE, 0.3927F, 0.0F, 0.0F);
    FACE.setTextureOffset(0, 0).addBox(-3.5F, 0.0F, -6.0F, 7.0F, 7.0F, 7.0F, 0.0F, false);
    FACE.setTextureOffset(57, 1).addBox(-1.5F, 0.0F, -9.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
    FACE.setTextureOffset(57, 1).addBox(1.5F, 0.0F, -9.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

    THORAX = new ModelRenderer(this);
    THORAX.setRotationPoint(0.0F, 0.0F, 0.0F);
    ROOT.addChild(THORAX);
    THORAX.setTextureOffset(0, 14).addBox(-4.5F, -9.0F, -6.0F, 9.0F, 9.0F, 11.0F, 0.0F, false);

    LEG_FRONTLEFT = new ModelRenderer(this);
    LEG_FRONTLEFT.setRotationPoint(4.5F, -2.0F, -4.0F);
    THORAX.addChild(LEG_FRONTLEFT);
    setRotationAngle(LEG_FRONTLEFT, 0.0F, 0.0873F, 1.309F);
    LEG_FRONTLEFT.setTextureOffset(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, false);

    KNEE_FRONTLEFT = new ModelRenderer(this);
    KNEE_FRONTLEFT.setRotationPoint(4.0F, 1.5F, 4.5F);
    LEG_FRONTLEFT.addChild(KNEE_FRONTLEFT);


    KneeFrontLeftCube_r1 = new ModelRenderer(this);
    KneeFrontLeftCube_r1.setRotationPoint(0.0F, 0.0F, -6.0F);
    KNEE_FRONTLEFT.addChild(KneeFrontLeftCube_r1);
    setRotationAngle(KneeFrontLeftCube_r1, 0.0F, -0.6981F, 0.0F);
    KneeFrontLeftCube_r1.setTextureOffset(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

    LEG_MIDLEFT = new ModelRenderer(this);
    LEG_MIDLEFT.setRotationPoint(4.5F, -2.0F, -0.5F);
    THORAX.addChild(LEG_MIDLEFT);
    setRotationAngle(LEG_MIDLEFT, 0.0F, 0.0F, 1.1345F);
    LEG_MIDLEFT.setTextureOffset(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, false);

    KNEE_MIDLEFT = new ModelRenderer(this);
    KNEE_MIDLEFT.setRotationPoint(4.0F, 1.5F, 4.5F);
    LEG_MIDLEFT.addChild(KNEE_MIDLEFT);


    KneeMidLeftCube_r1 = new ModelRenderer(this);
    KneeMidLeftCube_r1.setRotationPoint(0.0F, 0.0F, -6.0F);
    KNEE_MIDLEFT.addChild(KneeMidLeftCube_r1);
    setRotationAngle(KneeMidLeftCube_r1, 0.0F, -0.8727F, 0.0F);
    KneeMidLeftCube_r1.setTextureOffset(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

    LEG_REARLEFT = new ModelRenderer(this);
    LEG_REARLEFT.setRotationPoint(4.5F, -2.0F, 3.0F);
    THORAX.addChild(LEG_REARLEFT);
    setRotationAngle(LEG_REARLEFT, 0.0F, -0.2182F, 0.9599F);
    LEG_REARLEFT.setTextureOffset(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, false);

    KNEE_REARLEFT = new ModelRenderer(this);
    KNEE_REARLEFT.setRotationPoint(4.0F, 1.5F, 4.5F);
    LEG_REARLEFT.addChild(KNEE_REARLEFT);


    KneeRearLeftCube_r1 = new ModelRenderer(this);
    KneeRearLeftCube_r1.setRotationPoint(0.0F, 0.0F, -6.0F);
    KNEE_REARLEFT.addChild(KneeRearLeftCube_r1);
    setRotationAngle(KneeRearLeftCube_r1, 0.0F, -1.0472F, 0.0F);
    KneeRearLeftCube_r1.setTextureOffset(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

    LEG_FRONTRIGHT = new ModelRenderer(this);
    LEG_FRONTRIGHT.setRotationPoint(-4.5F, -2.0F, -4.0F);
    THORAX.addChild(LEG_FRONTRIGHT);
    setRotationAngle(LEG_FRONTRIGHT, 0.0F, -0.0873F, -1.309F);
    LEG_FRONTRIGHT.setTextureOffset(21, 0).addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, true);

    KNEE_FRONTRIGHT = new ModelRenderer(this);
    KNEE_FRONTRIGHT.setRotationPoint(-4.0F, 1.5F, 4.5F);
    LEG_FRONTRIGHT.addChild(KNEE_FRONTRIGHT);


    KneeFrontRightCube_r1 = new ModelRenderer(this);
    KneeFrontRightCube_r1.setRotationPoint(0.0F, 0.0F, -6.0F);
    KNEE_FRONTRIGHT.addChild(KneeFrontRightCube_r1);
    setRotationAngle(KneeFrontRightCube_r1, 0.0F, 0.6981F, 0.0F);
    KneeFrontRightCube_r1.setTextureOffset(37, 2).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, true);

    LEG_MIDRIGHT = new ModelRenderer(this);
    LEG_MIDRIGHT.setRotationPoint(-4.5F, -2.0F, -0.5F);
    THORAX.addChild(LEG_MIDRIGHT);
    setRotationAngle(LEG_MIDRIGHT, 0.0F, 0.0F, -1.1345F);
    LEG_MIDRIGHT.setTextureOffset(21, 0).addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, true);

    KNEE_MIDRIGHT = new ModelRenderer(this);
    KNEE_MIDRIGHT.setRotationPoint(-4.0F, 1.5F, 4.5F);
    LEG_MIDRIGHT.addChild(KNEE_MIDRIGHT);


    KneeMidRightCube_r1 = new ModelRenderer(this);
    KneeMidRightCube_r1.setRotationPoint(0.0F, 0.0F, -6.0F);
    KNEE_MIDRIGHT.addChild(KneeMidRightCube_r1);
    setRotationAngle(KneeMidRightCube_r1, 0.0F, 0.8727F, 0.0F);
    KneeMidRightCube_r1.setTextureOffset(37, 2).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, true);

    LEG_REARRIGHT = new ModelRenderer(this);
    LEG_REARRIGHT.setRotationPoint(-4.5F, -2.0F, 3.0F);
    THORAX.addChild(LEG_REARRIGHT);
    setRotationAngle(LEG_REARRIGHT, 0.0F, 0.2182F, -0.9599F);
    LEG_REARRIGHT.setTextureOffset(21, 0).addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, true);

    KNEE_REARRIGHT = new ModelRenderer(this);
    KNEE_REARRIGHT.setRotationPoint(-4.0F, 1.5F, 4.5F);
    LEG_REARRIGHT.addChild(KNEE_REARRIGHT);


    KneeRearRightCube_r1 = new ModelRenderer(this);
    KneeRearRightCube_r1.setRotationPoint(0.0F, 0.0F, -6.0F);
    KNEE_REARRIGHT.addChild(KneeRearRightCube_r1);
    setRotationAngle(KneeRearRightCube_r1, 0.0F, 1.0472F, 0.0F);
    KneeRearRightCube_r1.setTextureOffset(37, 2).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, true);

    WING_LEFT = new ModelRenderer(this);
    WING_LEFT.setRotationPoint(3.5F, -9.0F, -5.0F);
    THORAX.addChild(WING_LEFT);

    WingLeftPlane_r1 = new ModelRenderer(this);
    WingLeftPlane_r1.setRotationPoint(0.0F, 0.0F, 1.0F);
    WING_LEFT.addChild(WingLeftPlane_r1);
    setRotationAngle(WingLeftPlane_r1, 0.0F, 0.2182F, -0.1745F);
    WingLeftPlane_r1.setTextureOffset(5, 34).addBox(0.0F, 0.0F, -1.0F, 7.0F, 0.0F, 8.0F, 0.0F, false);

    WING_RIGHT = new ModelRenderer(this);
    WING_RIGHT.setRotationPoint(-3.5F, -9.0F, -5.0F);
    THORAX.addChild(WING_RIGHT);


    WingRightPlane_r1 = new ModelRenderer(this);
    WingRightPlane_r1.setRotationPoint(0.0F, 0.0F, 1.0F);
    WING_RIGHT.addChild(WingRightPlane_r1);
    setRotationAngle(WingRightPlane_r1, 0.0F, -0.2182F, 0.1745F);
    WingRightPlane_r1.setTextureOffset(5, 34).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 0.0F, 8.0F, 0.0F, true);

    ABDOMEN = new ModelRenderer(this);
    ABDOMEN.setRotationPoint(0.0F, -8.0F, 5.0F);
    ROOT.addChild(ABDOMEN);
    setRotationAngle(ABDOMEN, -0.3927F, 0.0F, 0.0F);
    ABDOMEN.setTextureOffset(29, 8).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 7.0F, 10.0F, 0.0F, false);
    ABDOMEN.setTextureOffset(51, 3).addBox(-0.5F, 3.0F, 9.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

    SADDLE = new ModelRenderer(this);
    SADDLE.setRotationPoint(0.0F, 0.0F, 0.0F);
    ROOT.addChild(SADDLE);
    SADDLE.setTextureOffset(0, 42).addBox(-5.5F, -9.25F, -4.0F, 11.0F, 5.0F, 9.0F, 0.0F, false);
  }

  @Override
  public void setLivingAnimations(BeehemothEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
    super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
  }

  private float getSine(float time, float max, float min) {
    float so = MathHelper.sin(time * 0.25f);
    float range = max - min;
    float out = (so * range) + min;
    return out;
  }

  @Override
  public void setRotationAngles(BeehemothEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    SADDLE.showModel = entity.isSaddled();
    WING_RIGHT.rotateAngleX = 0.0f;
    ROOT.rotateAngleX = 0.0f;
    ROOT.rotationPointY = 19.0f;
    boolean onGround = entity.isOnGround() && entity.getMotion().lengthSquared() < 1.0E-7D;
    if (onGround) {
      WING_RIGHT.rotateAngleY = -0.2618f;
      WING_RIGHT.rotateAngleZ = 0.0f;
      WING_LEFT.rotateAngleX = 0.0f;
      WING_LEFT.rotateAngleY = 0.2618f;
      WING_LEFT.rotateAngleZ = 0.0f;
      LEG_FRONTLEFT.rotateAngleX = 0.0f;
      LEG_FRONTRIGHT.rotateAngleX = 0.0f;
      LEG_MIDLEFT.rotateAngleX = 0.0f;
      LEG_MIDRIGHT.rotateAngleX = 0.0f;
      LEG_REARLEFT.rotateAngleX = 0.0f;
      LEG_REARRIGHT.rotateAngleX = 0.0f;
    } else {
      limbSwing = limbSwing * 2.f;
      WING_RIGHT.rotateAngleY = 0.0f;
      WING_LEFT.rotateAngleZ = ((float) (MathHelper.cos(limbSwing) * Math.PI * 0.15f));
      WING_LEFT.rotateAngleX = WING_RIGHT.rotateAngleX;
      WING_LEFT.rotateAngleY = WING_RIGHT.rotateAngleY;
      WING_RIGHT.rotateAngleZ = -WING_LEFT.rotateAngleZ;
      LEG_FRONTLEFT.rotateAngleX = 0.7853982f;
      LEG_FRONTRIGHT.rotateAngleX = 0.7853982f;
      LEG_MIDLEFT.rotateAngleX = 0.7853982f;
      LEG_MIDRIGHT.rotateAngleX = 0.7853982f;
      LEG_REARLEFT.rotateAngleX = 0.7853982f;
      LEG_REARRIGHT.rotateAngleX = 0.7853982f;
      ROOT.rotateAngleX = 0.0f;
      ROOT.rotateAngleY = 0.0f;
      ROOT.rotateAngleZ = 0.0f;

      // fr 20, 47.5
      // mr 37.5, 52.5
      // br 45, 62.5

      KneeFrontRightCube_r1.rotateAngleY = getSine(ageInTicks + entity.offset1, 0.2f, 0.475f);
      KneeMidRightCube_r1.rotateAngleY = getSine(ageInTicks + entity.offset2, 0.375f, 0.525f);
      KneeRearRightCube_r1.rotateAngleY = getSine(ageInTicks + entity.offset3, 0.45f, 0.625f);
      KneeFrontLeftCube_r1.rotateAngleY = getSine(ageInTicks + entity.offset4, -0.20f, -0.475f);
      KneeMidLeftCube_r1.rotateAngleY = getSine(ageInTicks + entity.offset5, -0.375f, -0.525f);
      KneeRearLeftCube_r1.rotateAngleY = getSine(ageInTicks + entity.offset6, -0.45f, -0.625f);
    }

    if (this.bodyPitch > 0.0F) {
      // Change pitch to affect abdomen and head
      ROOT.rotateAngleX = ModelUtils.func_228283_a_(ROOT.rotateAngleX, 3.0915928F, this.bodyPitch);
    }

    THORAX.rotateAngleX = 0;
    FACE.rotateAngleX = (float) (netHeadYaw / Math.PI / 180) + 0.3f;
    ABDOMEN.rotateAngleX = (float) (netHeadYaw / Math.PI / 180) - 0.3f;
  }

  @Override
  public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    ROOT.render(matrixStack, buffer, packedLight, packedOverlay);
  }

  public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }
}