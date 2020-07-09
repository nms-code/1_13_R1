package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.LinkedList;
// CraftBukkit start
import java.util.List;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.block.SpongeAbsorbEvent;
// CraftBukkit end

public class BlockSponge extends Block {

    protected BlockSponge(Block.Info block_info) {
        super(block_info);
    }

    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1) {
        if (iblockdata1.getBlock() != iblockdata.getBlock()) {
            this.a(world, blockposition);
        }
    }

    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1) {
        this.a(world, blockposition);
        super.doPhysics(iblockdata, world, blockposition, block, blockposition1);
    }

    protected void a(World world, BlockPosition blockposition) {
        if (this.b(world, blockposition)) {
            world.setTypeAndData(blockposition, Blocks.WET_SPONGE.getBlockData(), 2);
            world.triggerEffect(2001, blockposition, Block.getCombinedId(Blocks.WATER.getBlockData()));
        }

    }

    private boolean b(World world, BlockPosition blockposition) {
        LinkedList linkedlist = Lists.newLinkedList();

        linkedlist.add(new Tuple(blockposition, Integer.valueOf(0)));
        int i = 0;
        BlockStateListPopulator blockList = new BlockStateListPopulator(world); // CraftBukkit - Use BlockStateListPopulator

        while (!linkedlist.isEmpty()) {
            Tuple tuple = (Tuple) linkedlist.poll();
            BlockPosition blockposition1 = (BlockPosition) tuple.a();
            int j = ((Integer) tuple.b()).intValue();
            EnumDirection[] aenumdirection = EnumDirection.values();
            int k = aenumdirection.length;

            for (int l = 0; l < k; ++l) {
                EnumDirection enumdirection = aenumdirection[l];
                BlockPosition blockposition2 = blockposition1.shift(enumdirection);
                IBlockData iblockdata = world.getType(blockposition2);
                Fluid fluid = world.b(blockposition2);
                Material material = iblockdata.getMaterial();

                if (fluid.a(TagsFluid.a)) {
                    if (iblockdata.getBlock() instanceof IFluidSource && ((IFluidSource) iblockdata.getBlock()).a(blockList, blockposition2, iblockdata) != FluidTypes.a) { // CraftBukkit
                        ++i;
                        if (j < 6) {
                            linkedlist.add(new Tuple(blockposition2, Integer.valueOf(j + 1)));
                        }
                    } else if (iblockdata.getBlock() instanceof BlockFluids) {
                        blockList.setTypeAndData(blockposition2, Blocks.AIR.getBlockData(), 3); // CraftBukkit
                        ++i;
                        if (j < 6) {
                            linkedlist.add(new Tuple(blockposition2, Integer.valueOf(j + 1)));
                        }
                    } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                        // iblockdata.a(world, blockposition2, 0);
                        blockList.setTypeAndData(blockposition2, Blocks.AIR.getBlockData(), 3); // CraftBukkit
                        ++i;
                        if (j < 6) {
                            linkedlist.add(new Tuple(blockposition2, Integer.valueOf(j + 1)));
                        }
                    }
                }
            }

            if (i > 64) {
                break;
            }
        }
        // CraftBukkit start
        List<CraftBlockState> blocks = blockList.getList();
        if (!blocks.isEmpty()) {
            final org.bukkit.block.Block bblock = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            SpongeAbsorbEvent event = new SpongeAbsorbEvent(bblock, (List<org.bukkit.block.BlockState>) (List) blocks);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }

            for (CraftBlockState block : blocks) {
                BlockPosition blockposition2 = new BlockPosition(block.getX(), block.getY(), block.getZ());
                IBlockData iblockdata = world.getType(blockposition2);
                Material material = iblockdata.getMaterial();

                if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                    iblockdata.a(world, blockposition2, 0);
                }

                world.setTypeAndData(blockposition2, block.getHandle(), block.getFlag());
            }
        }
        // CraftBukkit end

        return i > 0;
    }
}
