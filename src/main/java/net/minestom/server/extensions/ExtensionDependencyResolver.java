package net.minestom.server.extensions;

import net.minestom.dependencies.DependencyResolver;
import net.minestom.dependencies.ResolvedDependency;
import net.minestom.dependencies.UnresolvedDependencyException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Does NOT relocate extensions
 */
public class ExtensionDependencyResolver implements DependencyResolver {

    private Map<String, DiscoveredExtension> extensionMap = new HashMap<>();

    public ExtensionDependencyResolver(List<DiscoveredExtension> extensions) {
        for(DiscoveredExtension ext : extensions) {
            extensionMap.put(ext.getName(), ext);
        }
    }

    @NotNull
    @Override
    public ResolvedDependency resolve(@NotNull String extensionName, @NotNull File file) throws UnresolvedDependencyException {
        if(extensionMap.containsKey(extensionName)) {
            DiscoveredExtension ext = extensionMap.get(extensionName);
            // convert extension URLs to subdependencies
            List<ResolvedDependency> deps = new LinkedList<>();
            for(URL u : ext.files) {
                deps.add(new ResolvedDependency(u.toExternalForm(), u.toExternalForm(), "", u, new LinkedList<>()));
            }
            return new ResolvedDependency(ext.getName(), ext.getName(), ext.getVersion(), ext.files.get(0), deps);
        }
        throw new UnresolvedDependencyException("No extension named "+extensionName);
    }

    @Override
    public String toString() {
        String list = extensionMap.values().stream().map(entry -> entry.getName()).collect(Collectors.joining(", "));
        return "ExtensionDependencyResolver[" + list + "]";
    }
}