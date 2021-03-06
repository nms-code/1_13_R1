package net.minecraft.server;

public class WorldGenFeatureJunglePyramid extends WorldGenFeatureRandomScattered<WorldGenFeatureJunglePyramidConfiguration> {

    public WorldGenFeatureJunglePyramid() {}

    protected String a() {
        return "Jungle_Pyramid";
    }

    public int b() {
        return 3;
    }

    protected StructureStart a(GeneratorAccess generatoraccess, ChunkGenerator<?> chunkgenerator, SeededRandom seededrandom, int i, int j) {
        BiomeBase biomebase = chunkgenerator.getWorldChunkManager().getBiome(new BlockPosition((i << 4) + 9, 0, (j << 4) + 9), Biomes.c);

        return new WorldGenFeatureJunglePyramid.a(generatoraccess, seededrandom, i, j, biomebase);
    }

    // Spigot start
    protected int c(World world) {
        return world.spigotConfig.jungleSeed;
        // Spigot end
    }

    public static class a extends StructureStart {

        public a() {}

        public a(GeneratorAccess generatoraccess, SeededRandom seededrandom, int i, int j, BiomeBase biomebase) {
            super(i, j, biomebase, seededrandom, generatoraccess.getSeed());
            WorldGenJunglePyramidPiece worldgenjunglepyramidpiece = new WorldGenJunglePyramidPiece(seededrandom, i * 16, j * 16);

            this.a.add(worldgenjunglepyramidpiece);
            this.a((IBlockAccess) generatoraccess);
        }
    }
}
