package fr.themode.demo;

import fr.themode.demo.generator.ChunkGenDemo;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.utils.Position;

public class MainDemo {

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        // Set the ChunkGenerator
        instanceContainer.setChunkGenerator(new ChunkGenDemo());
        // Enable the auto chunk loading (when players come close)
        instanceContainer.enableAutoChunkLoad(true);

        // Add event listeners
        ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
        connectionManager.addPlayerInitialization(player -> {
            // Set the spawning instance
            player.addEventCallback(PlayerLoginEvent.class, event -> {
                event.setSpawningInstance(instanceContainer);
                player.setRespawnPoint(new Position(0,45,0));
            });

            // Teleport the player at spawn
            player.addEventCallback(PlayerSpawnEvent.class, event -> {
                player.teleport(new Position(0, 45, 0));
                player.setGameMode(GameMode.CREATIVE);
            });
        });

        // Start the server
        minecraftServer.start("localhost", 25565);
    }

}
