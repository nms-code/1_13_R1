package net.minecraft.server;

import java.util.Random;

public class BlockBubbleColumn extends Block implements IFluidSource {

    public static final BlockStateBoolean a = BlockProperties.d;

    public BlockBubbleColumn(Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockBubbleColumn.a, Boolean.valueOf(true)));
    }

    public boolean a(IBlockData iblockdata) {
        return false;
    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        IBlockData iblockdata1 = world.getType(blockposition.up());

        if (iblockdata1.isAir()) {
            entity.j(((Boolean) iblockdata.get(BlockBubbleColumn.a)).booleanValue());
            if (!world.isClientSide) {
                WorldServer worldserver = (WorldServer) world;

                for (int i = 0; i < 2; ++i) {
                    worldserver.a(Particles.R, (double) ((float) blockposition.getX() + world.random.nextFloat()), (double) (blockposition.getY() + 1), (double) ((float) blockposition.getZ() + world.random.nextFloat()), 1, 0.0D, 0.0D, 0.0D, 1.0D);
                    worldserver.a(Particles.e, (double) ((float) blockposition.getX() + world.random.nextFloat()), (double) (blockposition.getY() + 1), (double) ((float) blockposition.getZ() + world.random.nextFloat()), 1, 0.0D, 0.01D, 0.0D, 0.2D);
                }
            }
        } else {
            entity.k(((Boolean) iblockdata.get(BlockBubbleColumn.a)).booleanValue());
        }

    }

    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1) {
        a(world, blockposition.up(), a((IBlockAccess) world, blockposition.down()));
    }

    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Random random) {
        a(world, blockposition.up(), a((IBlockAccess) world, blockposition));
    }

    public Fluid h(IBlockData iblockdata) {
        return FluidTypes.c.a(false);
    }

    public static void a(GeneratorAccess generatoraccess, BlockPosition blockposition, boolean flag) {
        if (a(generatoraccess, blockposition)) {
            generatoraccess.setTypeAndData(blockposition, (IBlockData) Blocks.BUBBLE_COLUMN.getBlockData().set(BlockBubbleColumn.a, Boolean.valueOf(flag)), 2);
        }

    }

    public static boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        Fluid fluid = generatoraccess.b(blockposition);

        return generatoraccess.getType(blockposition).getBlock() == Blocks.WATER && fluid.g() >= 8 && fluid.d();
    }

    private static boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        IBlockData iblockdata = iblockaccess.getType(blockposition);
        Block block = iblockdata.getBlock();

        return block == Blocks.BUBBLE_COLUMN ? ((Boolean) iblockdata.get(BlockBubbleColumn.a)).booleanValue() : block != Blocks.SOUL_SAND;
    }

    public int a(IWorldReader iworldreader) {
        return 5;
    }

    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (!iblockdata.canPlace(generatoraccess, blockposition)) {
            return Blocks.WATER.getBlockData();
        } else {
            if (enumdirection == EnumDirection.DOWN) {
                generatoraccess.setTypeAndData(blockposition, (IBlockData) Blocks.BUBBLE_COLUMN.getBlockData().set(BlockBubbleColumn.a, Boolean.valueOf(a((IBlockAccess) generatoraccess, blockposition1))), 2);
            } else if (enumdirection == EnumDirection.UP && iblockdata1.getBlock() != Blocks.BUBBLE_COLUMN && a(generatoraccess, blockposition1)) {
                generatoraccess.I().a(blockposition, this, this.a((IWorldReader) generatoraccess));
            }

            generatoraccess.H().a(blockposition, FluidTypes.c, FluidTypes.c.a((IWorldReader) generatoraccess));
            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        }
    }

    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        Block block = iworldreader.getType(blockposition.down()).getBlock();

        return block == Blocks.BUBBLE_COLUMN || block == Blocks.MAGMA_BLOCK || block == Blocks.SOUL_SAND;
    }

    public boolean j() {
        return false;
    }

    public int a(IBlockData iblockdata, Random random) {
        return 0;
    }

    public TextureType c() {
        return TextureType.TRANSLUCENT;
    }

    public EnumBlockFaceShape a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition, EnumDirection enumdirection) {
        return EnumBlockFaceShape.UNDEFINED;
    }

    public EnumRenderType c(IBlockData iblockdata) {
        return EnumRenderType.INVISIBLE;
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockBubbleColumn.a});
    }

    public FluidType a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {
        generatoraccess.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 11);
        return FluidTypes.c;
    }
}
