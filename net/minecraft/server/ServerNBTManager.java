package net.minecraft.server;

import com.mojang.datafixers.DataFixer;
import java.io.File;
import javax.annotation.Nullable;

public class ServerNBTManager extends WorldNBTStorage {

    public ServerNBTManager(File file, String s, @Nullable MinecraftServer minecraftserver, DataFixer datafixer) {
        super(file, s, minecraftserver, datafixer);
    }

    public IChunkLoader createChunkLoader(WorldProvider worldprovider) {
        File file = this.getDirectory();
        File file1;

        if (worldprovider instanceof WorldProviderHell) {
            file1 = new File(file, "DIM-1");
            file1.mkdirs();
            return new ChunkRegionLoader(file1, this.a);
        } else if (worldprovider instanceof WorldProviderTheEnd) {
            file1 = new File(file, "DIM1");
            file1.mkdirs();
            return new ChunkRegionLoader(file1, this.a);
        } else {
            return new ChunkRegionLoader(file, this.a);
        }
    }

    public void saveWorldData(WorldData worlddata, @Nullable NBTTagCompound nbttagcompound) {
        worlddata.d(19133);
        super.saveWorldData(worlddata, nbttagcompound);
    }

    public void a() {
        try {
            FileIOThread.a().b();
        } catch (InterruptedException interruptedexception) {
            interruptedexception.printStackTrace();
        }

        RegionFileCache.a();
    }
}
