package noobanidus.mods.carrierbees.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

@SuppressWarnings("deprecation")
public class AirBubbleParticleType extends ParticleType<AirBubbleParticleType> implements IParticleData {
  private final Codec<AirBubbleParticleType> CODEC = Codec.unit(this::getType);

  private static final IDeserializer<AirBubbleParticleType> DESERIALIZER = new IDeserializer<AirBubbleParticleType>() {
    @Override
    public AirBubbleParticleType deserialize(ParticleType<AirBubbleParticleType> particleTypeIn, StringReader reader) throws CommandSyntaxException {
      return (AirBubbleParticleType) particleTypeIn;
    }

    @Override
    public AirBubbleParticleType read(ParticleType<AirBubbleParticleType> particleTypeIn, PacketBuffer buffer) {
      return (AirBubbleParticleType) particleTypeIn;
    }
  };

  public AirBubbleParticleType(boolean alwaysShow, IDeserializer<AirBubbleParticleType> deserializer) {
    super(alwaysShow, deserializer);
  }

  public AirBubbleParticleType(boolean alwaysShow) {
    this(alwaysShow, DESERIALIZER);
  }

  @Override
  public AirBubbleParticleType getType() {
    return this;
  }

  @Override
  public void write(PacketBuffer buffer) {
  }

  @Override
  public String getParameters() {
    return "carrierbees:air_bubble";
  }

  @Override
  public Codec<AirBubbleParticleType> func_230522_e_() {
    return CODEC;
  }
}
