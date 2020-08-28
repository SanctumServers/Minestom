package net.minestom.server.instance.generation.multinoise;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.generation.BiomeSource;
import net.minestom.server.instance.generation.multinoise.util.FiveDimensionalVoronoiMap;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MultiNoiseBiomeSource extends BiomeSource {
    private final JNoise temperatureNoise = JNoise.newBuilder().perlin().setSeed(seed).build();
    private final JNoise altitudeNoise = JNoise.newBuilder().perlin().setSeed(seed).build();
    private final JNoise humidityNoise = JNoise.newBuilder().perlin().setSeed(seed + 10).build();
    private final JNoise weirdnessNoise = JNoise.newBuilder().perlin().setSeed(seed - 10).build();
    private final FiveDimensionalVoronoiMap<Biome> biomeMap = new FiveDimensionalVoronoiMap<>();

    protected MultiNoiseBiomeSource(@NotNull String identifier, int seed, List<Biome> biomes) {
        super("minecraft:multi_noise", seed, biomes);
        for (Biome b : biomes) {
            MultiNoiseBiomeParameters parameters = b.getMultiNoiseBiomeParameters();
            biomeMap.add(new FiveDimensionalVoronoiMap.Vec5D<>(
                    parameters.getTemperature(), parameters.getAltitude(),
                    parameters.getHumidity(), parameters.getWeirdness(),
                    parameters.getOffset(), b
            ));
        }
    }

    @NotNull
    @Override
    public Biome get(int x, int y, int z) {
        double nx = x / 80.0;
        double ny = y / 160.0;
        double nz = z / 80.0;
        double temperature = temperatureNoise.getNoise(nx, ny, nz) * 2.0;
        double altitude = altitudeNoise.getNoise(nx/16.0, ny/16.0, nz/16.0)*2.0;
        double humidity = humidityNoise.getNoise(nx*2.0, ny*2.0, nz*2.0)*2.0;
        double weirdness = weirdnessNoise.getNoise(nx/32.0, ny/32.0, nz/32.0)*2.0;
        return biomeMap.get(temperature, altitude, humidity, weirdness, 0.0).getInstance();
    }
}
