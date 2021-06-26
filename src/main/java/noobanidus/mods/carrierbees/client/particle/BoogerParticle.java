package noobanidus.mods.carrierbees.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.init.ModParticles;

@OnlyIn(Dist.CLIENT)
public class BoogerParticle extends SpriteTexturedParticle {
  protected boolean fullbright;

  private BoogerParticle(ClientWorld world, double x, double y, double z) {
    super(world, x, y, z);
    this.setSize(0.01F, 0.01F);
    this.particleGravity = 0.06F;
  }

  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  public int getBrightnessForRender(float partialTick) {
    return this.fullbright ? 240 : super.getBrightnessForRender(partialTick);
  }

  public void tick() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.ageParticle();
    if (!this.isExpired) {
      this.motionY -= (double) this.particleGravity;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.updateMotion();
      if (!this.isExpired) {
        this.motionX *= (double) 0.98F;
        this.motionY *= (double) 0.98F;
        this.motionZ *= (double) 0.98F;
      }
    }
  }

  protected void ageParticle() {
    if (this.maxAge-- <= 0) {
      this.setExpired();
    }

  }

  protected void updateMotion() {
  }

  @OnlyIn(Dist.CLIENT)
  static class Dripping extends BoogerParticle {
    private final IParticleData particleData;

    private Dripping(ClientWorld world, double x, double y, double z, IParticleData particleData) {
      super(world, x, y, z);
      this.particleData = particleData;
      this.particleGravity *= 0.02F;
      this.maxAge = 40;
    }

    protected void ageParticle() {
      if (this.maxAge-- <= 0) {
        this.setExpired();
        this.world.addParticle(this.particleData, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ);
      }

    }

    protected void updateMotion() {
      this.motionX *= 0.02D;
      this.motionY *= 0.02D;
      this.motionZ *= 0.02D;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class DrippingBoogerFactory implements IParticleFactory<BasicParticleType> {
    protected final IAnimatedSprite spriteWithAge;

    public DrippingBoogerFactory(IAnimatedSprite spriteWithAge) {
      this.spriteWithAge = spriteWithAge;
    }

    public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BoogerParticle.Dripping dripparticle$dripping = new BoogerParticle.Dripping(worldIn, x, y, z, ModParticles.FALLING_BOOGER.get());
      dripparticle$dripping.particleGravity *= 0.01F;
      dripparticle$dripping.maxAge = 100;
      dripparticle$dripping.setColor(199/255.0f, 176/255.0f, 134/255.0f);
      dripparticle$dripping.selectSpriteRandomly(this.spriteWithAge);
      return dripparticle$dripping;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class FallingBoogerFactory implements IParticleFactory<BasicParticleType> {
    protected final IAnimatedSprite spriteSet;

    public FallingBoogerFactory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BoogerParticle dripparticle = new BoogerParticle.FallingBoogerParticle(worldIn, x, y, z, ModParticles.LANDING_BOOGER.get());
      dripparticle.particleGravity = 0.01F;
      dripparticle.setColor(199/255.0f, 176/255.0f, 134/255.0f);
      dripparticle.selectSpriteRandomly(this.spriteSet);
      return dripparticle;
    }
  }

  @OnlyIn(Dist.CLIENT)
  static class FallingBoogerParticle extends BoogerParticle.FallingLiquidParticle {
    private FallingBoogerParticle(ClientWorld world, double x, double y, double z, IParticleData particleData) {
      super(world, x, y, z, particleData);
    }

    protected void updateMotion() {
      if (this.onGround) {
        this.setExpired();
        this.world.addParticle(this.particleData, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        this.world.playSound(this.posX + 0.5D, this.posY, this.posZ + 0.5D, SoundEvents.BLOCK_BEEHIVE_DROP, SoundCategory.BLOCKS, 0.3F + this.world.rand.nextFloat() * 2.0F / 3.0F, 1.0F, false);
      }

    }
  }

  @OnlyIn(Dist.CLIENT)
  static class FallingLiquidParticle extends BoogerParticle {
    protected final IParticleData particleData;

    private FallingLiquidParticle(ClientWorld world, double x, double y, double z, IParticleData particleData) {
      super(world, x, y, z);
      this.particleData = particleData;
      this.maxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
    }

    protected void updateMotion() {
      if (this.onGround) {
        this.setExpired();
        this.world.addParticle(this.particleData, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
      }

    }
  }

  @OnlyIn(Dist.CLIENT)
  static class Landing extends BoogerParticle {
    private Landing(ClientWorld world, double x, double y, double z) {
      super(world, x, y, z);
      this.maxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class LandingBoogerFactory implements IParticleFactory<BasicParticleType> {
    protected final IAnimatedSprite spriteSet;

    public LandingBoogerFactory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BoogerParticle dripparticle = new BoogerParticle.Landing(worldIn, x, y, z);
      dripparticle.maxAge = (int) (128.0D / (Math.random() * 0.8D + 0.2D));
      dripparticle.setColor(199/255.0f, 176/255.0f, 134/255.0f);
      dripparticle.selectSpriteRandomly(this.spriteSet);
      return dripparticle;
    }
  }
}
