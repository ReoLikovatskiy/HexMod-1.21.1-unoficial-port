package at.petrak.hexcasting.neoforge.network

import at.petrak.hexcasting.api.HexAPI
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.Channel
import net.neoforged.neoforge.network.ChannelDirection
import net.neoforged.neoforge.network.SimpleChannel

object NeoForgePacketHandler {
    private val PROTOCOL_VERSION = "1"
    val CHANNEL: SimpleChannel = SimpleChannel(
        ResourceLocation(HexAPI.MOD_ID, "main"),
        { PROTOCOL_VERSION },
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    )

    fun init() {
        var id = 0
        // Register packets here as they are created
        // Example:
        // CHANNEL.messageBuilder(YourPacket::class.java, id++, ChannelDirection.PLAY_TO_CLIENT)
        //     .decoder { ... }
        //     .encoder { ... }
        //     .consumerMainThread { ... }
        //     .add()
    }
}
