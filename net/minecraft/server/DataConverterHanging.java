package net.minecraft.server;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.function.Function;

public class DataConverterHanging extends DataFix {

    private static final int[][] a = new int[][] { { 0, 0, 1}, { -1, 0, 0}, { 0, 0, -1}, { 1, 0, 0}};

    public DataConverterHanging(Schema schema, boolean flag) {
        super(schema, flag);
    }

    private Dynamic<?> a(Dynamic<?> dynamic, boolean flag, boolean flag1) {
        if ((flag || flag1) && !dynamic.get("Facing").flatMap(Dynamic::getNumberValue).isPresent()) {
            int i;

            if (dynamic.get("Direction").flatMap(Dynamic::getNumberValue).isPresent()) {
                i = dynamic.getByte("Direction") % DataConverterHanging.a.length;
                int[] aint = DataConverterHanging.a[i];

                dynamic = dynamic.set("TileX", dynamic.createInt(dynamic.getInt("TileX") + aint[0]));
                dynamic = dynamic.set("TileY", dynamic.createInt(dynamic.getInt("TileY") + aint[1]));
                dynamic = dynamic.set("TileZ", dynamic.createInt(dynamic.getInt("TileZ") + aint[2]));
                dynamic = dynamic.remove("Direction");
                if (flag1 && dynamic.get("ItemRotation").flatMap(Dynamic::getNumberValue).isPresent()) {
                    dynamic = dynamic.set("ItemRotation", dynamic.createByte((byte) (dynamic.getByte("ItemRotation") * 2)));
                }
            } else {
                i = dynamic.getByte("Dir") % DataConverterHanging.a.length;
                dynamic = dynamic.remove("Dir");
            }

            dynamic = dynamic.set("Facing", dynamic.createByte((byte) i));
        }

        return dynamic;
    }

    public TypeRewriteRule makeRule() {
        Type type = this.getInputSchema().getChoiceType(DataConverterTypes.ENTITY, "Painting");
        OpticFinder opticfinder = DSL.namedChoice("Painting", type);
        Type type1 = this.getInputSchema().getChoiceType(DataConverterTypes.ENTITY, "ItemFrame");
        OpticFinder opticfinder1 = DSL.namedChoice("ItemFrame", type1);
        Type type2 = this.getInputSchema().getType(DataConverterTypes.ENTITY);
        TypeRewriteRule typerewriterule = this.fixTypeEverywhereTyped("EntityPaintingFix", type2, (typed) -> {
            return typed.updateTyped(opticfinder, type, (typedx) -> {
                return typedx.update(DSL.remainderFinder(), (dynamic) -> {
                    return this.a(dynamic, true, false);
                });
            });
        });
        TypeRewriteRule typerewriterule1 = this.fixTypeEverywhereTyped("EntityItemFrameFix", type2, (typed) -> {
            return typed.updateTyped(opticfinder, type, (typedx) -> {
                return typedx.update(DSL.remainderFinder(), (dynamic) -> {
                    return this.a(dynamic, false, true);
                });
            });
        });

        return TypeRewriteRule.seq(typerewriterule, typerewriterule1);
    }
}
