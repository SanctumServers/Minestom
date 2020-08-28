package net.minestom.server.instance.generation;

import net.minestom.server.instance.batch.ChunkBatch;
import org.jetbrains.annotations.NotNull;

public abstract class ChunkGenerator {
    protected final String identifier;
    protected final long seed;
    protected final BiomeSource biomeSource;
    protected String settings;

    protected ChunkGenerator(@NotNull String identifier, long seed, @NotNull BiomeSource biomeSource, String settings) {
        this.identifier = identifier;
        this.seed = seed;
        this.biomeSource = biomeSource;
        this.settings = settings;
    }

    public abstract void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ);

    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    public long getSeed() {
        return seed;
    }

    @NotNull
    public BiomeSource getBiomeSource() {
        return biomeSource;
    }

    @NotNull
    public String getSettings() {
        return settings;
    }

    public void setSettings(@NotNull String settings) {
        this.settings = settings;
    }
}
