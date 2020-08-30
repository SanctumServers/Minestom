package net.minestom.server.instance.generation.multinoise.abstraction;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class Carver {
    private final NamespaceID identifier;
    // TODO: CarverTypes!
    private NamespaceID carverType;
    private double probability;

    public Carver(@NotNull NamespaceID identifier, @NotNull NamespaceID carverType, double probability) {
        this.identifier = identifier;
        this.carverType = carverType;
        this.probability = probability;
    }

    @NotNull
    public NamespaceID getCarverType() {
        return carverType;
    }

    public void setCarverType(@NotNull NamespaceID carverType) {
        this.carverType = carverType;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @NotNull
    public NamespaceID getIdentifier() {
        return identifier;
    }
}
