package noobanidus.mods.carrierbees.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AirBubbleParticle extends SpriteTexturedParticle {
  private AirBubbleParticle(ClientWorld p_i232351_1_, double p_i232351_2_, double p_i232351_4_, double p_i232351_6_, double p_i232351_8_, double p_i232351_10_, double p_i232351_12_) {
    super(p_i232351_1_, p_i232351_2_, p_i232351_4_, p_i232351_6_);
    this.setSize(0.02F, 0.02F);
    this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
    this.motionX = p_i232351_8_ * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
    this.motionY = p_i232351_10_ * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
    this.motionZ = p_i232351_12_ * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
    this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
  }

  public void tick() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.maxAge-- <= 0) {
      this.setExpired();
    } else {
      this.motionY += 0.002D;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.8500000238418579D;
      this.motionY *= 0.8500000238418579D;
      this.motionZ *= 0.8500000238418579D;
    }
  }

  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  @OnlyIn(Dist.CLIENT)
  public static class Factory implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public Factory(IAnimatedSprite p_i50227_1_) {
      this.spriteSet = p_i50227_1_;
    }

    public Particle makeParticle(BasicParticleType p_199234_1_, ClientWorld p_199234_2_, double p_199234_3_, double p_199234_5_, double p_199234_7_, double p_199234_9_, double p_199234_11_, double p_199234_13_) {
      AirBubbleParticle lvt_15_1_ = new AirBubbleParticle(p_199234_2_, p_199234_3_, p_199234_5_, p_199234_7_, p_199234_9_, p_199234_11_, p_199234_13_);
      lvt_15_1_.selectSpriteRandomly(this.spriteSet);
      return lvt_15_1_;
    }
  }
}

