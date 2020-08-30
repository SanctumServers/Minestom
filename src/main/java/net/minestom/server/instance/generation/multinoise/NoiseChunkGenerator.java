package net.minestom.server.instance.generation.multinoise;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generation.BiomeSource;
import net.minestom.server.instance.generation.ChunkGenerator;
import net.minestom.server.instance.generation.multinoise.abstraction.SurfaceBuilder;
import net.minestom.server.registry.Registries;
import net.minestom.server.utils.metadata.MetadataField;
import net.minestom.server.utils.thread.MinestomThread;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class NoiseChunkGenerator extends ChunkGenerator {
    // TODO: Noise Settings
    private final JNoise heightNoise = JNoise.newBuilder().openSimplex().setSeed((int) seed).build();
    private final int seaLevel;
    private final int bedrockFloorPosition;
    private final int bedrockRoofPosition;
    private final Block defaultBlock;
    private final Block defaultFluid;

    private NoiseChunkGenerator(
            int seed,
            @NotNull BiomeSource biomeSource,
            int seaLevel,
            int bedrockFloorPosition,
            int bedrockRoofPosition,
            Block defaultBlock,
            Block defaultFluid
    ) {
        super(seed, biomeSource);
        this.seaLevel = seaLevel;
        this.bedrockFloorPosition = bedrockFloorPosition;
        this.bedrockRoofPosition = bedrockRoofPosition;
        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        MinestomThread generatorThread = new MinestomThread(4, "NoiseChunkGenerator-Pool");
        // Bedrock

        // Column (Surface)
        generatorThread.execute(() -> {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    batch.setBlock(x, bedrockFloorPosition, z, Block.BEDROCK);
                    batch.setBlock(x, bedrockRoofPosition, z, Block.BEDROCK);
                    int posX = chunkX * 16 + x;
                    int posZ = chunkZ * 16 + z;

                    Biome biome = getBiome(posX, seaLevel, posZ);
                    double heightDelta = biome.getDepth() + Math.exp(0.5 + Math.abs(biome.getDepth())) *
                            Math.abs(heightNoise.getNoise(posX / 64.0, posZ / 64.0)) * 8 * Math.signum(biome.getDepth());
                    int height = (int) (seaLevel + heightDelta);

                    // Surface
                    MetadataField<?> surfaceBuilderField = biome.getMetadataField("minecraft:multi_noise_surface_builder");
                    SurfaceBuilder surfaceBuilder = (surfaceBuilderField == null || !surfaceBuilderField.hasValue()) ? null : (SurfaceBuilder) surfaceBuilderField.getValue();

                    Block top = Block.GRASS;
                    Block under = Block.DIRT;
                    Block underwater = Block.GRAVEL;
                    if (surfaceBuilder != null) {
                        top = Registries.getBlock(surfaceBuilder.getTopMaterial());
                        under = Registries.getBlock(surfaceBuilder.getUnderMaterial());
                        underwater = Registries.getBlock(surfaceBuilder.getUnderwaterMaterial());
                    }

                    for (int y = bedrockFloorPosition + 1; y < height; y++) {
                        batch.setBlock(x, y, z, defaultBlock);
                    }

                    // Default fluid
                    for (int y = height; y <= seaLevel; y++) {
                        batch.setBlock(x, y, z, defaultFluid);
                    }
                    // Not underwater
                    if (height >= seaLevel) {
                        batch.setBlock(x, height, z, top);
                        for (int i=1; i <= 4; i++) {
                            if (height - i >= seaLevel) {
                               batch.setBlock(x, height - i, z, under);
                            }
                        }
                    } else {
                        batch.setBlock(x, height, z, underwater);
                    }
                    // End of Surface
                }
            }
        });
        generatorThread.shutdown();
        try {
            generatorThread.awaitTermination(1000L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Biome getBiome(int x, int y, int z) {
        return biomeSource.get(x, y, z);
    }

    @NotNull
    public static NoiseChunkGeneratorBuilder newBuilder(int seed, @NotNull BiomeSource biomeSource) {
        return new NoiseChunkGeneratorBuilder(seed, biomeSource);
    }

    public static final class NoiseChunkGeneratorBuilder {
        private int seed;
        private BiomeSource biomeSource;
        private int seaLevel = 63;
        private int bedrockRoofPosition = -1;
        private int bedrockFloorPosition = 0;
        private Block defaultBlock = Block.STONE;
        private Block defaultFluid = Block.WATER;

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
        public NoiseChunkGeneratorBuilder setSeaLevel(int seaLevel) {
            this.seaLevel = seaLevel;
            return this;
        }

        @NotNull
        public NoiseChunkGeneratorBuilder setBedrockFloorPosition(int bedrockFloorPosition) {
            this.bedrockFloorPosition = bedrockFloorPosition;
            return this;
        }

        @NotNull
        public NoiseChunkGeneratorBuilder setBedrockRoofPosition(int bedrockRoofPosition) {
            this.bedrockRoofPosition = bedrockRoofPosition;
            return this;
        }

        @NotNull
        public NoiseChunkGeneratorBuilder setDefaultBlock(@NotNull Block defaultBlock) {
            this.defaultBlock = defaultBlock;
            return this;
        }

        @NotNull
        public NoiseChunkGeneratorBuilder setDefaultFluid(@NotNull Block defaultFluid) {
            this.defaultFluid = defaultFluid;
            return this;
        }

        @NotNull
        public NoiseChunkGenerator build() {
            return new NoiseChunkGenerator(seed, biomeSource, seaLevel, bedrockFloorPosition, bedrockRoofPosition, defaultBlock, defaultFluid);
        }
    }
}
