package net.minestom.server.world;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import net.minestom.server.registry.ResourceGatherer;
import net.minestom.server.utils.NamespaceID;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * https://minecraft.gamepedia.com/Custom_dimension
 */
@Data
@Builder(builderMethodName = "hiddenBuilder", access = AccessLevel.PRIVATE)
public class DimensionType {

    public static final DimensionType OVERWORLD = DimensionType.silentFromData(NamespaceID.from("minecraft:overworld"));

    public static final DimensionType NETHER = DimensionType.silentFromData(NamespaceID.from("minecraft:the_nether"));

    public static final DimensionType END = DimensionType.silentFromData(NamespaceID.from("minecraft:the_end"));

    /**
     * Creates a DimensionType from a json file inside minecraft_data/data/&lt;id domain&gt;/dimension_type/&lt;id path&gt;.json
     * @param id
     * @return
     * @throws java.io.IOException if an error occurred during reading (or the file is missing)
     */
    public static DimensionType fromData(NamespaceID id) throws IOException {
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(new File(ResourceGatherer.DATA_FOLDER, "data/"+id.getDomain()+"/dimension_type/"+id.getPath()+".json"))) {
            JsonObject obj = gson.fromJson(reader, JsonObject.class);
            DimensionTypeBuilder builder = DimensionType.builder(id);
            builder.raidCapable(obj.get("has_raids").getAsBoolean());
            builder.logicalHeight(obj.get("logical_height").getAsInt());
            builder.infiniburn(NamespaceID.from(obj.get("infiniburn").getAsString()));
            builder.ambientLight(obj.get("ambient_light").getAsFloat());
            builder.piglinSafe(obj.get("piglin_safe").getAsBoolean());
            builder.bedSafe(obj.get("bed_works").getAsBoolean());
            builder.respawnAnchorSafe(obj.get("respawn_anchor_works").getAsBoolean());
            builder.ultrawarm(obj.get("ultrawarm").getAsBoolean());
            builder.natural(obj.get("natural").getAsBoolean());
            builder.shrunk(obj.get("shrunk").getAsBoolean());
            builder.skylightEnabled(obj.get("has_skylight").getAsBoolean());
            builder.ceilingEnabled(obj.get("has_ceiling").getAsBoolean());
            if(obj.has("fixed_time")) {
                builder.fixedTime(Optional.of(obj.get("fixed_time").getAsLong()));
            }
            return builder.build();
        }
    }

    /**
     * Silent version of {@link DimensionType#fromData(NamespaceID)}, will print the error stacktrace and return null on errors
     * @param id
     * @return
     */
    public static DimensionType silentFromData(NamespaceID id) {
        try {
            return fromData(id);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final NamespaceID name;
    private final boolean natural;
    private final float ambientLight;
    private final boolean ceilingEnabled;
    private final boolean skylightEnabled;
    @Builder.Default private final Optional<Long> fixedTime = Optional.empty();
    private final boolean shrunk;
    private final boolean raidCapable;
    private final boolean respawnAnchorSafe;
    private final boolean ultrawarm;
    @Builder.Default private final boolean bedSafe = true;
    private final boolean piglinSafe;
    @Builder.Default private final int logicalHeight = 256;
    @Builder.Default private final NamespaceID infiniburn = NamespaceID.from("minecraft:infiniburn_overworld");

    public NBTCompound toNBT() {
        NBTCompound nbt = new NBTCompound()
                .setString("name", name.toString())
                .setFloat("ambient_light", ambientLight)
                .setString("infiniburn", infiniburn.toString())
                .setByte("natural", (byte) (natural ? 0x01 : 0x00))
                .setByte("has_ceiling", (byte) (ceilingEnabled ? 0x01 : 0x00))
                .setByte("has_skylight", (byte) (skylightEnabled ? 0x01 : 0x00))
                .setByte("shrunk", (byte) (shrunk ? 0x01 : 0x00))
                .setByte("ultrawarm", (byte) (ultrawarm ? 0x01 : 0x00))
                .setByte("has_raids", (byte) (raidCapable ? 0x01 : 0x00))
                .setByte("respawn_anchor_works", (byte) (respawnAnchorSafe ? 0x01 : 0x00))
                .setByte("bed_works", (byte) (bedSafe ? 0x01 : 0x00))
                .setByte("piglin_safe", (byte) (piglinSafe ? 0x01 : 0x00))
                .setInt("logical_height", logicalHeight)
        ;
        fixedTime.ifPresent(time -> nbt.setLong("fixed_time", time));
        return nbt;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public static DimensionTypeBuilder builder(NamespaceID name) {
        return hiddenBuilder().name(name);
    }

}
