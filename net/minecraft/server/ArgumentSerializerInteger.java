package net.minecraft.server;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

public class ArgumentSerializerInteger implements ArgumentSerializer<IntegerArgumentType> {

    public ArgumentSerializerInteger() {}

    public void a(IntegerArgumentType integerargumenttype, PacketDataSerializer packetdataserializer) {
        boolean flag = integerargumenttype.getMinimum() != Integer.MIN_VALUE;
        boolean flag1 = integerargumenttype.getMaximum() != Integer.MAX_VALUE;

        packetdataserializer.writeByte(ArgumentSerializers.a(flag, flag1));
        if (flag) {
            packetdataserializer.writeInt(integerargumenttype.getMinimum());
        }

        if (flag1) {
            packetdataserializer.writeInt(integerargumenttype.getMaximum());
        }

    }

    public IntegerArgumentType a(PacketDataSerializer packetdataserializer) {
        byte b0 = packetdataserializer.readByte();
        int i = ArgumentSerializers.a(b0) ? packetdataserializer.readInt() : Integer.MIN_VALUE;
        int j = ArgumentSerializers.b(b0) ? packetdataserializer.readInt() : Integer.MAX_VALUE;

        return IntegerArgumentType.integer(i, j);
    }

    public void a(IntegerArgumentType integerargumenttype, JsonObject jsonobject) {
        if (integerargumenttype.getMinimum() != Integer.MIN_VALUE) {
            jsonobject.addProperty("min", Integer.valueOf(integerargumenttype.getMinimum()));
        }

        if (integerargumenttype.getMaximum() != Integer.MAX_VALUE) {
            jsonobject.addProperty("max", Integer.valueOf(integerargumenttype.getMaximum()));
        }

    }

    public ArgumentType b(PacketDataSerializer packetdataserializer) {
        return this.a(packetdataserializer);
    }
}
