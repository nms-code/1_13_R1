package net.minecraft.server;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import java.util.UUID;
import java.util.function.Function;

public class DataConverterUUID extends DataFix {

    public DataConverterUUID(Schema schema, boolean flag) {
        super(schema, flag);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityStringUuidFix", this.getInputSchema().getType(DataConverterTypes.ENTITY), (typed) -> {
            return typed.update(DSL.remainderFinder(), (dynamic) -> {
                if (dynamic.get("UUID").flatMap(Dynamic::getStringValue).isPresent()) {
                    UUID uuid = UUID.fromString(dynamic.getString("UUID"));

                    return dynamic.remove("UUID").set("UUIDMost", dynamic.createLong(uuid.getMostSignificantBits())).set("UUIDLeast", dynamic.createLong(uuid.getLeastSignificantBits()));
                } else {
                    return dynamic;
                }
            });
        });
    }
}
