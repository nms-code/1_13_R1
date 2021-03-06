package net.minecraft.server;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import java.util.function.Function;

public class DataConverterFlattenState extends DataFix {

    public DataConverterFlattenState(Schema schema, boolean flag) {
        super(schema, flag);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("BlockStateStructureTemplateFix", this.getInputSchema().getType(DataConverterTypes.l), (typed) -> {
            return typed.update(DSL.remainderFinder(), DataConverterFlattenData::a);
        });
    }
}
