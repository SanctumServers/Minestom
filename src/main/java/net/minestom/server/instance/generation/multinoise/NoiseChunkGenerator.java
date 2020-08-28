package net.minestom.server.instance.generation.multinoise;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generation.BiomeSource;
import net.minestom.server.instance.generation.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public final class NoiseChunkGenerator extends ChunkGenerator {
    private JNoise heightNoise = JNoise.newBuilder().openSimplex().build();

    private NoiseChunkGenerator(int seed, @NotNull BiomeSource biomeSource, @NotNull String settings) {
        super("minestom:noise", seed, biomeSource, settings);
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        // Bedrock
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                batch.setBlock(x, 0, z, Block.BEDROCK);
            }
        }

        // Surface

    }


    @NotNull
    public static NoiseChunkGeneratorBuilder newBuilder(int seed, @NotNull BiomeSource biomeSource) {
        return new NoiseChunkGeneratorBuilder(seed, biomeSource);
    }

    public static final class NoiseChunkGeneratorBuilder {
        private int seed;
        private BiomeSource biomeSource;
        private String settings;

        private NoiseChunkGeneratorBuilder(int seed, @NotNull BiomeSource biomeSource) {
            this.seed = seed;
            this.biomeSource = biomeSource;
        }

        @NotNull
        public NoiseChunkGeneratorBuilder setSeed(int seed) {
            this.seed = seed;
            return this;
        }

        @NotNull
        public NoiseChunkGeneratorBuilder setBiomeSource(@NotNull BiomeSource biomeSource) {
            this.biomeSource = biomeSource;
            return this;
        }

        @NotNull
        public NoiseChunkGeneratorBuilder setSettings(@NotNull String settings) {
            this.settings = settings;
            return this;
        }

        @NotNull
        public NoiseChunkGenerator build() {
            return new NoiseChunkGenerator(seed, biomeSource, settings);
        }
    }
}
