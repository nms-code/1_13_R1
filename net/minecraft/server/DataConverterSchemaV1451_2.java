package net.minecraft.server;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataConverterSchemaV1451_2 extends DataConverterSchemaNamed {

    public DataConverterSchemaV1451_2(int i, Schema schema) {
        super(i, schema);
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        Map map = super.registerBlockEntities(schema);

        schema.register(map, "minecraft:piston", (s) -> {
            return DSL.optionalFields("blockState", DataConverterTypes.l.in(schema));
        });
        return map;
    }
}
