package net.minecraft.server;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Locale;

public class ParticleParamRedstone implements ParticleParam {

    public static final ParticleParamRedstone a = new ParticleParamRedstone(1.0F, 0.0F, 0.0F, 1.0F);
    public static final ParticleParam.a<ParticleParamRedstone> b = new ParticleParam.a() {
        public ParticleParamRedstone a(Particle<ParticleParamRedstone> particle, StringReader stringreader) throws CommandSyntaxException {
            stringreader.expect(' ');
            float f = (float) stringreader.readDouble();

            stringreader.expect(' ');
            float f1 = (float) stringreader.readDouble();

            stringreader.expect(' ');
            float f2 = (float) stringreader.readDouble();

            stringreader.expect(' ');
            float f3 = (float) stringreader.readDouble();

            return new ParticleParamRedstone(f, f1, f2, f3);
        }

        public ParticleParamRedstone a(Particle<ParticleParamRedstone> particle, PacketDataSerializer packetdataserializer) {
            return new ParticleParamRedstone(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
        }

        public ParticleParam b(Particle particle, PacketDataSerializer packetdataserializer) {
            return this.a(particle, packetdataserializer);
        }

        public ParticleParam b(Particle particle, StringReader stringreader) throws CommandSyntaxException {
            return this.a(particle, stringreader);
        }
    };
    private final float c;
    private final float d;
    private final float e;
    private final float f;

    public ParticleParamRedstone(float f, float f1, float f2, float f3) {
        this.c = f;
        this.d = f1;
        this.e = f2;
        this.f = MathHelper.a(f3, 0.01F, 4.0F);
    }

    public void a(PacketDataSerializer packetdataserializer) {
        packetdataserializer.writeFloat(this.c);
        packetdataserializer.writeFloat(this.d);
        packetdataserializer.writeFloat(this.e);
        packetdataserializer.writeFloat(this.f);
    }

    public String a() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", new Object[] { this.b().d(), Float.valueOf(this.c), Float.valueOf(this.d), Float.valueOf(this.e), Float.valueOf(this.f)});
    }

    public Particle<ParticleParamRedstone> b() {
        return Particles.m;
    }
}
