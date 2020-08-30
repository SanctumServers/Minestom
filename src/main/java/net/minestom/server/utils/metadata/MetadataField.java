package net.minestom.server.utils.metadata;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Mansour
 */
public class MetadataField<Type> {
    private final NamespaceID key;
    private Type value;

    public MetadataField(@NotNull NamespaceID key, @Nullable Type value) {
        this.key = key;
        this.value = value;
    }

    public MetadataField(@NotNull String key, @Nullable Type value) {
        this(NamespaceID.from(key), value);
    }

    @NotNull
    public NamespaceID getKey() {
        return key;
    }

    public boolean hasValue() {
        return value != null;
    }

    @Nullable
    public Type getValue() {
        return value;
    }

    public void setValue(@Nullable Type value) {
        this.value = value;
    }

}
