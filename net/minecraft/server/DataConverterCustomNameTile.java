package net.minecraft.server;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class DataConverterCustomNameTile extends DataFix {

    public DataConverterCustomNameTile(Schema schema, boolean flag) {
        super(schema, flag);
    }

    public TypeRewriteRule makeRule() {
        OpticFinder opticfinder = DSL.fieldFinder("id", DSL.namespacedString());

        return this.fixTypeEverywhereTyped("BlockEntityCustomNameToComponentFix", this.getInputSchema().getType(DataConverterTypes.j), (typed) -> {
            return typed.update(DSL.remainderFinder(), (dynamic) -> {
                Optional optional = typed.getOptional(opticfinder);

                return optional.isPresent() && Objects.equals(optional.get(), "minecraft:command_block") ? dynamic : DataConverterCustomNameEntity.a(dynamic);
            });
        });
    }
}
