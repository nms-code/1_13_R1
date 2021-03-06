package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;

public class BlockWallSign extends BlockSign {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    private static final Map<EnumDirection, VoxelShape> p = Maps.newEnumMap(ImmutableMap.of(EnumDirection.NORTH, Block.a(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), EnumDirection.SOUTH, Block.a(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), EnumDirection.EAST, Block.a(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), EnumDirection.WEST, Block.a(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));

    public BlockWallSign(Block.Info block_info) {
        super(block_info);
        this.v((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockWallSign.FACING, EnumDirection.NORTH)).set(BlockWallSign.a, Boolean.valueOf(false)));
    }

    public String m() {
        return this.getItem().getName();
    }

    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return (VoxelShape) BlockWallSign.p.get(iblockdata.get(BlockWallSign.FACING));
    }

    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return iworldreader.getType(blockposition.shift(((EnumDirection) iblockdata.get(BlockWallSign.FACING)).opposite())).getMaterial().isBuildable();
    }

    @Nullable
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = this.getBlockData();
        Fluid fluid = blockactioncontext.getWorld().b(blockactioncontext.getClickPosition());
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        EnumDirection[] aenumdirection = blockactioncontext.e();
        EnumDirection[] aenumdirection1 = aenumdirection;
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection1[j];

            if (enumdirection.k().c()) {
                EnumDirection enumdirection1 = enumdirection.opposite();

                iblockdata = (IBlockData) iblockdata.set(BlockWallSign.FACING, enumdirection1);
                if (iblockdata.canPlace(world, blockposition)) {
                    return (IBlockData) iblockdata.set(BlockWallSign.a, Boolean.valueOf(fluid.c() == FluidTypes.c));
                }
            }
        }

        return null;
    }

    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        return enumdirection.opposite() == iblockdata.get(BlockWallSign.FACING) && !iblockdata.canPlace(generatoraccess, blockposition) ? Blocks.AIR.getBlockData() : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockWallSign.FACING, enumblockrotation.a((EnumDirection) iblockdata.get(BlockWallSign.FACING)));
    }

    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockWallSign.FACING)));
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(new IBlockState[] { BlockWallSign.FACING, BlockWallSign.a});
    }
}
