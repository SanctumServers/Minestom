package net.minestom.server.instance.generation;

import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BiomeSource {
    protected final int seed;
    protected final List<Biome> biomes;

    protected BiomeSource(int seed, @NotNull List<Biome> biomes) {
        this.seed = seed;
        this.biomes = biomes;
    }

    @NotNull
    public abstract Biome get(int x, int y, int z);

    public int getSeed() {
        return seed;
    }

    @NotNull
    public List<Biome> getBiomes() {
        return biomes;
    }
}
