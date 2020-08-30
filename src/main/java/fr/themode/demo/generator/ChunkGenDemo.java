package fr.themode.demo.generator;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generation.BiomeSource;
import net.minestom.server.instance.generation.ChunkGenerator;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ChunkGenDemo extends ChunkGenerator {

    public ChunkGenDemo() {
        super(0, new BiomeSource(0, Collections.singletonList(Biome.PLAINS)) {
            @NotNull
            @Override
            public Biome get(int x, int y, int z) {
                return Biome.PLAINS;
            }
        });
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        for (byte x = 0; x < Chunk.CHUNK_SIZE_X; x++)
            for (byte z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                for (byte y = 0; y < 40; y++) {
                    batch.setBlock(x, y, z, Block.STONE);
                }
            }
    }
}
