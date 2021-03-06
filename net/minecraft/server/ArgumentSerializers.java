package net.minecraft.server;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.function.Supplier;

public class ArgumentSerializers {

    public static void a() {
        ArgumentRegistry.a(new MinecraftKey("brigadier:bool"), BoolArgumentType.class, new ArgumentSerializerVoid(BoolArgumentType::bool));
        ArgumentRegistry.a(new MinecraftKey("brigadier:float"), FloatArgumentType.class, new ArgumentSerializerFloat());
        ArgumentRegistry.a(new MinecraftKey("brigadier:double"), DoubleArgumentType.class, new ArgumentSerializerDouble());
        ArgumentRegistry.a(new MinecraftKey("brigadier:integer"), IntegerArgumentType.class, new ArgumentSerializerInteger());
        ArgumentRegistry.a(new MinecraftKey("brigadier:string"), StringArgumentType.class, new ArgumentSerializerString());
    }

    public static byte a(boolean flag, boolean flag1) {
        byte b0 = 0;

        if (flag) {
            b0 = (byte) (b0 | 1);
        }

        if (flag1) {
            b0 = (byte) (b0 | 2);
        }

        return b0;
    }

    public static boolean a(byte b0) {
        return (b0 & 1) != 0;
    }

    public static boolean b(byte b0) {
        return (b0 & 2) != 0;
    }
}
