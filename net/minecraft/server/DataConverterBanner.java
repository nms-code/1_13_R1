package net.minecraft.server;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class DataConverterBanner extends DataFix {

    public DataConverterBanner(Schema schema, boolean flag) {
        super(schema, flag);
    }

    public TypeRewriteRule makeRule() {
        Type type = this.getInputSchema().getType(DataConverterTypes.ITEM_STACK);
        OpticFinder opticfinder = DSL.fieldFinder("id", DSL.named(DataConverterTypes.q.typeName(), DSL.namespacedString()));
        OpticFinder opticfinder1 = type.findField("tag");
        OpticFinder opticfinder2 = opticfinder1.type().findField("BlockEntityTag");

        return this.fixTypeEverywhereTyped("ItemBannerColorFix", type, (typed) -> {
            Optional optional = typed.getOptional(opticfinder);

            if (optional.isPresent() && Objects.equals(((Pair) optional.get()).getSecond(), "minecraft:banner")) {
                Dynamic dynamic = (Dynamic) typed.get(DSL.remainderFinder());
                Optional optional1 = typed.getOptionalTyped(opticfinder1);

                if (optional1.isPresent()) {
                    Typed typed1 = (Typed) optional1.get();
                    Optional optional2 = typed1.getOptionalTyped(opticfinder2);

                    if (optional2.isPresent()) {
                        Typed typed2 = (Typed) optional2.get();
                        Dynamic dynamic1 = (Dynamic) typed1.get(DSL.remainderFinder());
                        Dynamic dynamic2 = (Dynamic) typed2.getOrCreate(DSL.remainderFinder());

                        if (dynamic2.get("Base").flatMap(Dynamic::getNumberValue).isPresent()) {
                            dynamic = dynamic.set("Damage", dynamic.createShort((short) (dynamic2.getShort("Base") & 15)));
                            Optional optional3 = dynamic1.get("display");

                            if (optional3.isPresent()) {
                                Dynamic dynamic3 = (Dynamic) optional3.get();

                                if (Objects.equals(dynamic3, dynamic3.emptyMap().merge(dynamic3.createString("Lore"), dynamic3.createList(Stream.of(dynamic3.createString("(+NBT")))))) {
                                    return typed.set(DSL.remainderFinder(), dynamic);
                                }
                            }

                            dynamic2.remove("Base");
                            return typed.set(DSL.remainderFinder(), dynamic).set(opticfinder1, typed1.set(opticfinder2, typed2.set(DSL.remainderFinder(), dynamic2)));
                        }
                    }
                }

                return typed.set(DSL.remainderFinder(), dynamic);
            } else {
                return typed;
            }
        });
    }
}
