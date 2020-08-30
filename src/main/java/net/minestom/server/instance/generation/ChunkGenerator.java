package net.minestom.server.instance.generation;

import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.metadata.MetadataField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class ChunkGenerator {
    protected final long seed;
    protected final BiomeSource biomeSource;
    private final Map<NamespaceID, MetadataField<?>> metadata = new HashMap<>();

    protected ChunkGenerator(long seed, @NotNull BiomeSource biomeSource) {
        this.seed = seed;
        this.biomeSource = biomeSource;
    }

    public abstract void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ);

    public long getSeed() {
        return seed;
    }

    @NotNull
    public BiomeSource getBiomeSource() {
        return biomeSource;
    }

    public void addMetadataField(@NotNull String key, @Nullable Object value) {
        addMetadataField(new MetadataField<>(key, value));
    }

    public void addMetadataField(@NotNull MetadataField<?> metadataField) {
        metadata.put(metadataField.getKey(), metadataField);
    }

    @Nullable
    public MetadataField<?> getMetadataField(@NotNull String key) {
        return getMetadataField(NamespaceID.from(key));
    }

    @Nullable
    public MetadataField<?> getMetadataField(@NotNull NamespaceID key) {
        return metadata.get(key);
    }
}
