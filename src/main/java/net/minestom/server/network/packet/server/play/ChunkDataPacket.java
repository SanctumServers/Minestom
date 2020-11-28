package net.minestom.server.network.packet.server.play;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minestom.server.MinecraftServer;
import net.minestom.server.data.Data;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.instance.block.CustomBlock;
import net.minestom.server.instance.palette.PaletteStorage;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Utils;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.cache.CacheablePacket;
import net.minestom.server.utils.cache.TemporaryPacketCache;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.UUID;

public class ChunkDataPacket implements ServerPacket, CacheablePacket {

    private static final BlockManager BLOCK_MANAGER = MinecraftServer.getBlockManager();
    private static final TemporaryPacketCache CACHE = new TemporaryPacketCache(10000L);

    public boolean fullChunk;
    public Biome[] biomes;
    public int chunkX, chunkZ;

    public PaletteStorage paletteStorage;
    public PaletteStorage customBlockPaletteStorage;

    public IntSet blockEntities;
    public Int2ObjectMap<Data> blocksData;

    public int[] sections;

    private static final byte CHUNK_SECTION_COUNT = Chunk.CHUNK_SECTION_COUNT;

    // Cacheable data
    private final UUID identifier;
    private final long lastUpdate;

    public ChunkDataPacket(@Nullable UUID identifier, long lastUpdate) {
        this.identifier = identifier;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        int mask = computeMask();

        writer.writeInt(chunkX);
        writer.writeInt(chunkZ);
        writer.writeBoolean(fullChunk);

        writer.writeVarInt(mask);

        // Heightmap
        int[] motionBlocking = new int[16 * 16];
        int[] worldSurface = new int[16 * 16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                motionBlocking[x + z * 16] = 4;
                worldSurface[x + z * 16] = 5;
            }
        }

        {
            writer.writeNBT("",
                    new NBTCompound()
                            .setLongArray("MOTION_BLOCKING", Utils.encodeBlocks(motionBlocking, 9))
                            .setLongArray("WORLD_SURFACE", Utils.encodeBlocks(worldSurface, 9))
            );
        }

        // Biome data
        if (fullChunk) {
            writer.writeVarInt(biomes.length);
            for (Biome biome : biomes) {
                writer.writeVarInt(biome.getId());
            }
        }

        // Data
        writeBlocks(writer.getBuffer(), mask);

        // Block entities
        writer.writeVarInt(blockEntities.size());

        for (int index : blockEntities) {
            final BlockPosition blockPosition = ChunkUtils.getBlockPosition(index, chunkX, chunkZ);

            NBTCompound nbt = new NBTCompound()
                    .setInt("x", blockPosition.getX())
                    .setInt("y", blockPosition.getY())
                    .setInt("z", blockPosition.getZ());

            if (customBlockPaletteStorage != null) {
                final short customBlockId = customBlockPaletteStorage.getBlockAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
                final CustomBlock customBlock = BLOCK_MANAGER.getCustomBlock(customBlockId);
                if (customBlock != null) {
                    final Data data = blocksData.get(index);
                    customBlock.writeBlockEntity(blockPosition, data, nbt);
                }
            }
            writer.writeNBT("", nbt);
        }
    }

    private void writeBlocks(ByteBuf buffer, int mask) {
        //store the location of size field
        final int sizeLoc = buffer.writerIndex();
        //skip the amount of bytes the size field wil use and clear them
        buffer.writeByte(0);
        buffer.writeByte(0);
        buffer.writeByte(0);
        //write blocks
        for (byte i = 0; i < CHUNK_SECTION_COUNT; i++) {
            //if the section is selected in the mask
            if (fullChunk || ((mask >> i) & 1) != 0) {
                final long[] section = paletteStorage.getSectionBlocks()[i];
                if (section.length > 0) { // section contains at least one block
                    Utils.writeBlocks(buffer, paletteStorage.getPalette(i), section, paletteStorage.getBitsPerEntry());
                }
            }
        }

        //Write size field
        //get the size of the block data
        final int size = buffer.writerIndex() - sizeLoc - 3;

        Utils.writeSizedVarInt(buffer, sizeLoc, size, 3);
    }

    private int computeMask() {
        int mask = 0b1111111111111111;
        for (byte i = 0; i < CHUNK_SECTION_COUNT; i++)
            //Section is unwanted
            if (!fullChunk && sections[i] == 0)
                mask ^= 1 << i;
            else
                //Section contains nothing
                if (paletteStorage.getSectionBlocks()[i].length == 0)
                    mask ^= 1 << i;
        return mask;
    }

    @Override
    public int getId() {
        return ServerPacketIdentifier.CHUNK_DATA;
    }

    @NotNull
    @Override
    public TemporaryPacketCache getCache() {
        return CACHE;
    }

    @Override
    public UUID getIdentifier() {
        return identifier;
    }

    @Override
    public long getLastUpdateTime() {
        return lastUpdate;
    }
}