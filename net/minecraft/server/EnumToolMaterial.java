package net.minecraft.server;

import java.util.function.Supplier;

public enum EnumToolMaterial implements ToolMaterial {

    WOOD(0, 59, 2.0F, 0.0F, 15, () -> {
        return RecipeItemStack.a(TagsItem.b);
    }), STONE(1, 131, 4.0F, 1.0F, 5, () -> {
        return RecipeItemStack.a(new IMaterial[] { Blocks.COBBLESTONE});
    }), IRON(2, 250, 6.0F, 2.0F, 14, () -> {
        return RecipeItemStack.a(new IMaterial[] { Items.IRON_INGOT});
    }), DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> {
        return RecipeItemStack.a(new IMaterial[] { Items.DIAMOND});
    }), GOLD(0, 32, 12.0F, 0.0F, 22, () -> {
        return RecipeItemStack.a(new IMaterial[] { Items.GOLD_INGOT});
    });

    private final int f;
    private final int g;
    private final float h;
    private final float i;
    private final int j;
    private final LazyInitVar<RecipeItemStack> k;

    private EnumToolMaterial(int i, int j, float f, float f1, int k, Supplier supplier) {
        this.f = i;
        this.g = j;
        this.h = f;
        this.i = f1;
        this.j = k;
        this.k = new LazyInitVar(supplier);
    }

    public int a() {
        return this.g;
    }

    public float b() {
        return this.h;
    }

    public float c() {
        return this.i;
    }

    public int d() {
        return this.f;
    }

    public int e() {
        return this.j;
    }

    public RecipeItemStack f() {
        return (RecipeItemStack) this.k.a();
    }
}
