package net.minecraft.server;

import it.unimi.dsi.fastutil.doubles.DoubleList;

public class VoxelShapeSlice extends VoxelShape {

    private final VoxelShape b;
    private final EnumDirection.EnumAxis c;
    private final DoubleList d = new VoxelShapeCubePoint(1);

    public VoxelShapeSlice(VoxelShape voxelshape, EnumDirection.EnumAxis enumdirection_enumaxis, int i) {
        super(a(voxelshape.a, enumdirection_enumaxis, i));
        this.b = voxelshape;
        this.c = enumdirection_enumaxis;
    }

    private static VoxelShapeDiscrete a(VoxelShapeDiscrete voxelshapediscrete, EnumDirection.EnumAxis enumdirection_enumaxis, int i) {
        return new VoxelShapeDiscreteSlice(voxelshapediscrete, enumdirection_enumaxis.a(i, 0, 0), enumdirection_enumaxis.a(0, i, 0), enumdirection_enumaxis.a(0, 0, i), enumdirection_enumaxis.a(i + 1, voxelshapediscrete.b, voxelshapediscrete.b), enumdirection_enumaxis.a(voxelshapediscrete.c, i + 1, voxelshapediscrete.c), enumdirection_enumaxis.a(voxelshapediscrete.d, voxelshapediscrete.d, i + 1));
    }

    protected DoubleList a(EnumDirection.EnumAxis enumdirection_enumaxis) {
        return enumdirection_enumaxis == this.c ? this.d : this.b.a(enumdirection_enumaxis);
    }
}
