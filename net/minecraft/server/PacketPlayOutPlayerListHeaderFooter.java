package net.minecraft.server;

import java.io.IOException;

public class PacketPlayOutPlayerListHeaderFooter implements Packet<PacketListenerPlayOut> {

    public IChatBaseComponent a; // PAIL
    public IChatBaseComponent b; // PAIL

    public PacketPlayOutPlayerListHeaderFooter() {}

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.f();
        this.b = packetdataserializer.f();
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.a(this.b);
    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
