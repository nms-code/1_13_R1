package net.minecraft.server;

import java.io.IOException;

public class PacketPlayOutBlockAction implements Packet<PacketListenerPlayOut> {

    private BlockPosition a;
    private int b;
    private int c;
    private Block d;

    public PacketPlayOutBlockAction() {}

    public PacketPlayOutBlockAction(BlockPosition blockposition, Block block, int i, int j) {
        this.a = blockposition;
        this.d = block;
        this.b = i;
        this.c = j;
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
        this.b = packetdataserializer.readUnsignedByte();
        this.c = packetdataserializer.readUnsignedByte();
        this.d = Block.getById(packetdataserializer.g());
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.writeByte(this.b);
        packetdataserializer.writeByte(this.c);
        packetdataserializer.d(Block.REGISTRY.a((Object) this.d));
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
