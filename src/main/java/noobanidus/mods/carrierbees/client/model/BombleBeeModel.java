package noobanidus.mods.carrierbees.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.entities.BombleBeeEntity;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;

@OnlyIn(Dist.CLIENT)
public class BombleBeeModel<T extends BombleBeeEntity> extends AgeableModel<T> {
  public final ModelRenderer body;
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

  public BombleBeeModel() {
    super(false, 24.0F, 0.0F);
    this.textureWidth = 64;
    this.textureHeight = 64;
    this.body = new ModelRenderer(this);
    this.body.setRotationPoint(0.0F, 19.0F, 0.0F);
    this.torso = new ModelRenderer(this, 0, 0);
    this.torso.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.body.addChild(this.torso);
    this.torso.addCuboid(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.0F);
    this.stinger = new ModelRenderer(this, 26, 7);
    this.stinger.addCuboid(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F, 0.0F);
    this.torso.addChild(this.stinger);
    this.leftAntenna = new ModelRenderer(this, 2, 0);
    this.leftAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
    this.leftAntenna.addCuboid(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F);
    this.rightAntenna = new ModelRenderer(this, 2, 3);
    this.rightAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
    this.rightAntenna.addCuboid(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F);
    this.torso.addChild(this.leftAntenna);
    this.torso.addChild(this.rightAntenna);
    this.rightWing = new ModelRenderer(this, 0, 18);
    this.rightWing.setRotationPoint(-1.5F, -4.0F, -3.0F);
    this.rightWing.rotateAngleX = 0.0F;
    this.rightWing.rotateAngleY = -0.2618F;
    this.rightWing.rotateAngleZ = 0.0F;
    this.body.addChild(this.rightWing);
    this.rightWing.addCuboid(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.001F);
    this.leftWing = new ModelRenderer(this, 0, 18);
    this.leftWing.setRotationPoint(1.5F, -4.0F, -3.0F);
    this.leftWing.rotateAngleX = 0.0F;
    this.leftWing.rotateAngleY = 0.2618F;
    this.leftWing.rotateAngleZ = 0.0F;
    this.leftWing.mirror = true;
    this.body.addChild(this.leftWing);
    this.leftWing.addCuboid(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.001F);
    this.frontLegs = new ModelRenderer(this);
    this.frontLegs.setRotationPoint(1.5F, 3.0F, -2.0F);
    this.body.addChild(this.frontLegs);
    this.frontLegs.func_217178_a("frontLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 1);
    this.middleLegs = new ModelRenderer(this);
    this.middleLegs.setRotationPoint(1.5F, 3.0F, 0.0F);
    this.body.addChild(this.middleLegs);
    this.middleLegs.func_217178_a("midLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 3);
    this.backLegs = new ModelRenderer(this);
    this.backLegs.setRotationPoint(1.5F, 3.0F, 2.0F);
    this.body.addChild(this.backLegs);
    this.backLegs.func_217178_a("backLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 5);
  }

  @Override
  public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
    super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
    this.bodyPitch = entity.getBodyPitch(partialTicks);
  }

  @Override
  public void setAngles(T entity, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, float scaleFactor) {
    this.rightWing.rotateAngleX = 0.0F;
    this.leftAntenna.rotateAngleX = 0.0F;
    this.rightAntenna.rotateAngleX = 0.0F;
    this.body.rotateAngleX = 0.0F;
    this.body.rotationPointY = 19.0F;
    boolean onGround = entity.onGround && entity.getMotion().lengthSquared() < 1.0E-7D;
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
      v1 = netHeadYaw * 2.1F;
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
    }

    if (!entity.isAngry()) {
      this.body.rotateAngleX = 0.0F;
      this.body.rotateAngleY = 0.0F;
      this.body.rotateAngleZ = 0.0F;
      if (!onGround) {
        v1 = MathHelper.cos(netHeadYaw * 0.18F);
        this.body.rotateAngleX = 0.1F + v1 * 3.1415927F * 0.025F;
        this.leftAntenna.rotateAngleX = v1 * 3.1415927F * 0.03F;
        this.rightAntenna.rotateAngleX = v1 * 3.1415927F * 0.03F;
        this.frontLegs.rotateAngleX = -v1 * 3.1415927F * 0.1F + 0.3926991F;
        this.backLegs.rotateAngleX = -v1 * 3.1415927F * 0.05F + 0.7853982F;
        this.body.rotationPointY = 19.0F - MathHelper.cos(netHeadYaw * 0.18F) * 0.9F;
      }
    }

    if (this.bodyPitch > 0.0F) {
      this.body.rotateAngleX = ModelUtils.interpolateAngle(this.body.rotateAngleX, 3.0915928F, this.bodyPitch);
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
}
