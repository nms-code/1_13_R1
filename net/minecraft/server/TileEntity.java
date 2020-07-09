package net.minecraft.server;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.spigotmc.CustomTimingsHandler; // Spigot
import org.bukkit.inventory.InventoryHolder; // CraftBukkit

public abstract class TileEntity {

    public CustomTimingsHandler tickTimer = org.bukkit.craftbukkit.SpigotTimings.getTileEntityTimings(this); // Spigot
    private static final Logger a = LogManager.getLogger();
    private final TileEntityTypes<?> e;
    protected World world;
    protected BlockPosition position;
    protected boolean d;
    @Nullable
    private IBlockData f;

    public TileEntity(TileEntityTypes<?> tileentitytypes) {
        this.position = BlockPosition.ZERO;
        this.e = tileentitytypes;
    }

    @Nullable
    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean u() {
        return this.world != null;
    }

    public void load(NBTTagCompound nbttagcompound) {
        this.position = new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        return this.d(nbttagcompound);
    }

    private NBTTagCompound d(NBTTagCompound nbttagcompound) {
        MinecraftKey minecraftkey = TileEntityTypes.a(this.C());

        if (minecraftkey == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.setString("id", minecraftkey.toString());
            nbttagcompound.setInt("x", this.position.getX());
            nbttagcompound.setInt("y", this.position.getY());
            nbttagcompound.setInt("z", this.position.getZ());
            return nbttagcompound;
        }
    }

    // CraftBukkit start
    @Nullable
    public static TileEntity create(NBTTagCompound nbttagcompound) {
        return create(nbttagcompound, null);
    }

    @Nullable
    public static TileEntity create(NBTTagCompound nbttagcompound, @Nullable World world) {
        // CraftBukkit end
        TileEntity tileentity = null;
        String s = nbttagcompound.getString("id");

        try {
            tileentity = TileEntityTypes.a(s);
        } catch (Throwable throwable) {
            TileEntity.a.error("Failed to create block entity {}", s, throwable);
        }

        if (tileentity != null) {
            try {
                tileentity.setWorld(world); // CraftBukkit
                tileentity.load(nbttagcompound);
            } catch (Throwable throwable1) {
                TileEntity.a.error("Failed to load data for block entity {}", s, throwable1);
                tileentity = null;
            }
        } else {
            TileEntity.a.warn("Skipping BlockEntity with id {}", s);
        }

        return tileentity;
    }

    public void update() {
        if (this.world != null) {
            this.f = this.world.getType(this.position);
            this.world.b(this.position, this);
            if (!this.f.isAir()) {
                this.world.updateAdjacentComparators(this.position, this.f.getBlock());
            }
        }

    }

    public BlockPosition getPosition() {
        return this.position;
    }

    public IBlockData getBlock() {
        if (this.f == null) {
            this.f = this.world.getType(this.position);
        }

        return this.f;
    }

    @Nullable
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return null;
    }

    public NBTTagCompound aa_() {
        return this.d(new NBTTagCompound());
    }

    public boolean x() {
        return this.d;
    }

    public void y() {
        this.d = true;
    }

    public void z() {
        this.d = false;
    }

    public boolean c(int i, int j) {
        return false;
    }

    public void invalidateBlockCache() {
        this.f = null;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        crashreportsystemdetails.a("Name", () -> {
            return TileEntityTypes.a.b(this.C()) + " // " + this.getClass().getCanonicalName();
        });
        if (this.world != null) {
            CrashReportSystemDetails.a(crashreportsystemdetails, this.position, this.getBlock());
            CrashReportSystemDetails.a(crashreportsystemdetails, this.position, this.world.getType(this.position));
        }
    }

    public void setPosition(BlockPosition blockposition) {
        this.position = blockposition.h();
    }

    public boolean isFilteredNBT() {
        return false;
    }

    public void a(EnumBlockRotation enumblockrotation) {}

    public void a(EnumBlockMirror enumblockmirror) {}

    public TileEntityTypes<?> C() {
        return this.e;
    }

    // CraftBukkit start - add method
    public InventoryHolder getOwner() {
        if (world == null) return null;
        // Spigot start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        if (block == null) {
            org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.WARNING, "No block for owner at %s %d %d %d", new Object[]{world.getWorld(), position.getX(), position.getY(), position.getZ()});
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState();
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
