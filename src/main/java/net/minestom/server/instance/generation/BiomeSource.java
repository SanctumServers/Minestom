package net.minestom.server.instance.generation;

import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BiomeSource {
    protected final String identifier;
    protected final int seed;
    protected final List<Biome> biomes;

    protected BiomeSource(@NotNull String identifier, int seed, @NotNull List<Biome> biomes) {
        this.identifier = identifier;
        this.seed = seed;
        this.biomes = biomes;
    }

    @NotNull
    public abstract Biome get(int x, int y, int z);

    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    public int getSeed() {
        return seed;
    }

    @NotNull
    public List<Biome> getBiomes() {
        return biomes;
    }
}
