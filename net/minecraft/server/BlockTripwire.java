package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.event.entity.EntityInteractEvent; // CraftBukkit

public class BlockTripwire extends Block {

    public static final BlockStateBoolean POWERED = BlockProperties.t;
    public static final BlockStateBoolean ATTACHED = BlockProperties.a;
    public static final BlockStateBoolean DISARMED = BlockProperties.c;
    public static final BlockStateBoolean NORTH = BlockSprawling.a;
    public static final BlockStateBoolean EAST = BlockSprawling.b;
    public static final BlockStateBoolean SOUTH = BlockSprawling.c;
    public static final BlockStateBoolean WEST = BlockSprawling.p;
    private static final Map<EnumDirection, BlockStateBoolean> v = BlockTall.r;
    protected static final VoxelShape t = Block.a(0.0D, 1.0D, 0.0D, 16.0D, 2.5D, 16.0D);
    protected static final VoxelShape u = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private final BlockTripwireHook w;

    public BlockTripwire(BlockTripwireHook blocktripwirehook, Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockTripwire.POWERED, Boolean.valueOf(false))).set(BlockTripwire.ATTACHED, Boolean.valueOf(false))).set(BlockTripwire.DISARMED, Boolean.valueOf(false))).set(BlockTripwire.NORTH, Boolean.valueOf(false))).set(BlockTripwire.EAST, Boolean.valueOf(false))).set(BlockTripwire.SOUTH, Boolean.valueOf(false))).set(BlockTripwire.WEST, Boolean.valueOf(false)));
        this.w = blocktripwirehook;
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return ((Boolean) iblockdata.get(BlockTripwire.ATTACHED)).booleanValue() ? BlockTripwire.t : BlockTripwire.u;
    }

    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();

        return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockTripwire.NORTH, Boolean.valueOf(this.a(world.getType(blockposition.north()), EnumDirection.NORTH)))).set(BlockTripwire.EAST, Boolean.valueOf(this.a(world.getType(blockposition.east()), EnumDirection.EAST)))).set(BlockTripwire.SOUTH, Boolean.valueOf(this.a(world.getType(blockposition.south()), EnumDirection.SOUTH)))).set(BlockTripwire.WEST, Boolean.valueOf(this.a(world.getType(blockposition.west()), EnumDirection.WEST)));
    }

    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        return enumdirection.k().c() ? (IBlockData) iblockdata.set((IBlockState) BlockTripwire.v.get(enumdirection), Boolean.valueOf(this.a(iblockdata1, enumdirection))) : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    public boolean a(IBlockData iblockdata) {
        return false;
    }

    public TextureType c() {
        return TextureType.TRANSLUCENT;
    }

    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1) {
        if (iblockdata1.getBlock() != iblockdata.getBlock()) {
            this.a(world, blockposition, iblockdata);
        }
    }

    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!flag && iblockdata.getBlock() != iblockdata1.getBlock()) {
            this.a(world, blockposition, (IBlockData) iblockdata.set(BlockTripwire.POWERED, Boolean.valueOf(true)));
        }
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        if (!world.isClientSide && !entityhuman.getItemInMainHand().isEmpty() && entityhuman.getItemInMainHand().getItem() == Items.SHEARS) {
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTripwire.DISARMED, Boolean.valueOf(true)), 4);
        }

        super.a(world, blockposition, iblockdata, entityhuman);
    }

    private void a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection[] aenumdirection = new EnumDirection[] { EnumDirection.SOUTH, EnumDirection.WEST};
        int i = aenumdirection.length;
        int j = 0;

        while (j < i) {
            EnumDirection enumdirection = aenumdirection[j];
            int k = 1;

            while (true) {
                if (k < 42) {
                    BlockPosition blockposition1 = blockposition.shift(enumdirection, k);
                    IBlockData iblockdata1 = world.getType(blockposition1);

                    if (iblockdata1.getBlock() == this.w) {
                        if (iblockdata1.get(BlockTripwireHook.FACING) == enumdirection.opposite()) {
                            this.w.a(world, blockposition1, iblockdata1, false, true, k, iblockdata);
                        }
                    } else if (iblockdata1.getBlock() == this) {
                        ++k;
                        continue;
                    }
                }

                ++j;
                break;
            }
        }

    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (!world.isClientSide) {
            if (!((Boolean) iblockdata.get(BlockTripwire.POWERED)).booleanValue()) {
                this.a(world, blockposition);
            }
        }
    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Random random) {
        if (!world.isClientSide) {
            if (((Boolean) world.getType(blockposition).get(BlockTripwire.POWERED)).booleanValue()) {
                this.a(world, blockposition);
            }
        }
    }

    private void a(World world, BlockPosition blockposition) {
        IBlockData iblockdata = world.getType(blockposition);
        boolean flag = ((Boolean) iblockdata.get(BlockTripwire.POWERED)).booleanValue();
        boolean flag1 = false;
        List list = world.getEntities((Entity) null, iblockdata.g(world, blockposition).a().a(blockposition));

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (!entity.isIgnoreBlockTrigger()) {
                    flag1 = true;
                    break;
                }
            }
        }

        // CraftBukkit start - Call interact even when triggering connected tripwire
        if (flag != flag1 && flag1 && (Boolean)iblockdata.get(ATTACHED)) {
            org.bukkit.World bworld = world.getWorld();
            org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
            org.bukkit.block.Block block = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            boolean allowed = false;

            // If all of the events are cancelled block the tripwire trigger, else allow
            for (Object object : list) {
                if (object != null) {
                    org.bukkit.event.Cancellable cancellable;

                    if (object instanceof EntityHuman) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) object, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
                    } else if (object instanceof Entity) {
                        cancellable = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                        manager.callEvent((EntityInteractEvent) cancellable);
                    } else {
                        continue;
                    }

                    if (!cancellable.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag1 != flag) {
            iblockdata = (IBlockData) iblockdata.set(BlockTripwire.POWERED, Boolean.valueOf(flag1));
            world.setTypeAndData(blockposition, iblockdata, 3);
            this.a(world, blockposition, iblockdata);
        }

        if (flag1) {
            world.I().a(new BlockPosition(blockposition), this, this.a((IWorldReader) world));
        }

    }

    public boolean a(IBlockData iblockdata, EnumDirection enumdirection) {
        Block block = iblockdata.getBlock();

        return block == this.w ? iblockdata.get(BlockTripwireHook.FACING) == enumdirection.opposite() : block == this;
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockTripwire.NORTH, iblockdata.get(BlockTripwire.SOUTH))).set(BlockTripwire.EAST, iblockdata.get(BlockTripwire.WEST))).set(BlockTripwire.SOUTH, iblockdata.get(BlockTripwire.NORTH))).set(BlockTripwire.WEST, iblockdata.get(BlockTripwire.EAST));

        case COUNTERCLOCKWISE_90:
            return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockTripwire.NORTH, iblockdata.get(BlockTripwire.EAST))).set(BlockTripwire.EAST, iblockdata.get(BlockTripwire.SOUTH))).set(BlockTripwire.SOUTH, iblockdata.get(BlockTripwire.WEST))).set(BlockTripwire.WEST, iblockdata.get(BlockTripwire.NORTH));

        case CLOCKWISE_90:
            return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockTripwire.NORTH, iblockdata.get(BlockTripwire.WEST))).set(BlockTripwire.EAST, iblockdata.get(BlockTripwire.NORTH))).set(BlockTripwire.SOUTH, iblockdata.get(BlockTripwire.EAST))).set(BlockTripwire.WEST, iblockdata.get(BlockTripwire.SOUTH));

        default:
            return iblockdata;
        }
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return (IBlockData) ((IBlockData) iblockdata.set(BlockTripwire.NORTH, iblockdata.get(BlockTripwire.SOUTH))).set(BlockTripwire.SOUTH, iblockdata.get(BlockTripwire.NORTH));

        case FRONT_BACK:
            return (IBlockData) ((IBlockData) iblockdata.set(BlockTripwire.EAST, iblockdata.get(BlockTripwire.WEST))).set(BlockTripwire.WEST, iblockdata.get(BlockTripwire.EAST));

        default:
            return super.a(iblockdata, enumblockmirror);
        }
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockTripwire.POWERED, BlockTripwire.ATTACHED, BlockTripwire.DISARMED, BlockTripwire.NORTH, BlockTripwire.EAST, BlockTripwire.WEST, BlockTripwire.SOUTH});
    }

    public EnumBlockFaceShape a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, EnumDirection enumdirection) {
        return EnumBlockFaceShape.UNDEFINED;
    }
}
