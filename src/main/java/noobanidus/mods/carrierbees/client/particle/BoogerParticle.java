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
    this.gravity = 0.06F;
  }

  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  public int getLightColor(float partialTick) {
    return this.fullbright ? 240 : super.getLightColor(partialTick);
  }

  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    this.ageParticle();
    if (!this.removed) {
      this.yd -= (double) this.gravity;
      this.move(this.xd, this.yd, this.zd);
      this.updateMotion();
      if (!this.removed) {
        this.xd *= (double) 0.98F;
        this.yd *= (double) 0.98F;
        this.zd *= (double) 0.98F;
      }
    }
  }

  protected void ageParticle() {
    if (this.lifetime-- <= 0) {
      this.remove();
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
      this.gravity *= 0.02F;
      this.lifetime = 40;
    }

    protected void ageParticle() {
      if (this.lifetime-- <= 0) {
        this.remove();
        this.level.addParticle(this.particleData, this.x, this.y, this.z, this.xd, this.yd, this.zd);
      }

    }

    protected void updateMotion() {
      this.xd *= 0.02D;
      this.yd *= 0.02D;
      this.zd *= 0.02D;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class DrippingBoogerFactory implements IParticleFactory<BasicParticleType> {
    protected final IAnimatedSprite spriteWithAge;

    public DrippingBoogerFactory(IAnimatedSprite spriteWithAge) {
      this.spriteWithAge = spriteWithAge;
    }

    public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BoogerParticle.Dripping dripparticle$dripping = new BoogerParticle.Dripping(worldIn, x, y, z, ModParticles.FALLING_BOOGER.get());
      dripparticle$dripping.gravity *= 0.01F;
      dripparticle$dripping.lifetime = 100;
      dripparticle$dripping.setColor(199/255.0f, 176/255.0f, 134/255.0f);
      dripparticle$dripping.pickSprite(this.spriteWithAge);
      return dripparticle$dripping;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class FallingBoogerFactory implements IParticleFactory<BasicParticleType> {
    protected final IAnimatedSprite spriteSet;

    public FallingBoogerFactory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BoogerParticle dripparticle = new BoogerParticle.FallingBoogerParticle(worldIn, x, y, z, ModParticles.LANDING_BOOGER.get());
      dripparticle.gravity = 0.01F;
      dripparticle.setColor(199/255.0f, 176/255.0f, 134/255.0f);
      dripparticle.pickSprite(this.spriteSet);
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
        this.remove();
        this.level.addParticle(this.particleData, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
        this.level.playLocalSound(this.x + 0.5D, this.y, this.z + 0.5D, SoundEvents.BEEHIVE_DRIP, SoundCategory.BLOCKS, 0.3F + this.level.random.nextFloat() * 2.0F / 3.0F, 1.0F, false);
      }

    }
  }

  @OnlyIn(Dist.CLIENT)
  static class FallingLiquidParticle extends BoogerParticle {
    protected final IParticleData particleData;

    private FallingLiquidParticle(ClientWorld world, double x, double y, double z, IParticleData particleData) {
      super(world, x, y, z);
      this.particleData = particleData;
      this.lifetime = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
    }

    protected void updateMotion() {
      if (this.onGround) {
        this.remove();
        this.level.addParticle(this.particleData, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
      }

    }
  }

  @OnlyIn(Dist.CLIENT)
  static class Landing extends BoogerParticle {
    private Landing(ClientWorld world, double x, double y, double z) {
      super(world, x, y, z);
      this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class LandingBoogerFactory implements IParticleFactory<BasicParticleType> {
    protected final IAnimatedSprite spriteSet;

    public LandingBoogerFactory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BoogerParticle dripparticle = new BoogerParticle.Landing(worldIn, x, y, z);
      dripparticle.lifetime = (int) (128.0D / (Math.random() * 0.8D + 0.2D));
      dripparticle.setColor(199/255.0f, 176/255.0f, 134/255.0f);
      dripparticle.pickSprite(this.spriteSet);
      return dripparticle;
    }
  }
}
