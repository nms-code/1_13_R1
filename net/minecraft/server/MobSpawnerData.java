package net.minecraft.server;

public class MobSpawnerData extends WeightedRandom.WeightedRandomChoice {

    private final NBTTagCompound b;

    public MobSpawnerData() {
        super(1);
        this.b = new NBTTagCompound();
        this.b.setString("id", "minecraft:pig");
    }

    public MobSpawnerData(NBTTagCompound nbttagcompound) {
        this(nbttagcompound.hasKeyOfType("Weight", 99) ? nbttagcompound.getInt("Weight") : 1, nbttagcompound.getCompound("Entity"));
    }

    public MobSpawnerData(int i, NBTTagCompound nbttagcompound) {
        super(i);
        this.b = nbttagcompound;
    }

    public NBTTagCompound a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!this.b.hasKeyOfType("id", 8)) {
            this.b.setString("id", "minecraft:pig");
        } else if (!this.b.getString("id").contains(":")) {
            this.b.setString("id", (new MinecraftKey(this.b.getString("id"))).toString());
        }

        nbttagcompound.set("Entity", this.b);
        nbttagcompound.setInt("Weight", this.a);
        return nbttagcompound;
    }

    public NBTTagCompound b() {
        return this.b;
    }
}
