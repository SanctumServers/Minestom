package net.minestom.server.instance.generation.multinoise.abstraction;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class SurfaceBuilder {
    private final NamespaceID identifier;
    private NamespaceID topMaterial;
    private NamespaceID underMaterial;
    private NamespaceID underwaterMaterial;

    public SurfaceBuilder(
            @NotNull NamespaceID identifier,
            @NotNull NamespaceID topMaterial,
            @NotNull NamespaceID underMaterial,
            @NotNull NamespaceID underwaterMaterial
    ) {
        this.identifier = identifier;
        this.topMaterial = topMaterial;
        this.underMaterial = underMaterial;
        this.underwaterMaterial = underwaterMaterial;
    }

    @NotNull
    public NamespaceID getIdentifier() {
        return identifier;
    }

    @NotNull
    public NamespaceID getTopMaterial() {
        return topMaterial;
    }

    public void setTopMaterial(@NotNull NamespaceID topMaterial) {
        this.topMaterial = topMaterial;
    }

    @NotNull
    public NamespaceID getUnderMaterial() {
        return underMaterial;
    }

    public void setUnderMaterial(@NotNull NamespaceID underMaterial) {
        this.underMaterial = underMaterial;
    }

    @NotNull
    public NamespaceID getUnderwaterMaterial() {
        return underwaterMaterial;
    }

    public void setUnderwaterMaterial(@NotNull NamespaceID underwaterMaterial) {
        this.underwaterMaterial = underwaterMaterial;
    }
}
