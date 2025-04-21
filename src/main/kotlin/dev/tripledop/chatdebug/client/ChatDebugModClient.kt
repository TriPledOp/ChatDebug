package dev.tripledop.chatdebug.client

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class ChatDebugModClient : ClientModInitializer {
    companion object {
        @JvmStatic
        lateinit var instance: ChatDebugModClient
            private set
        @JvmStatic
        val isEnabled: Boolean
            get() = AutoConfig.getConfigHolder(ModConfig::class.java).config.isEnabled
    }

    override fun onInitializeClient() {
        instance = this

        AutoConfig.register(ModConfig::class.java) { a, b -> GsonConfigSerializer(a, b) }
    }

    fun showTextInfo(text: Text) {
        MinecraftClient.getInstance().setScreen(ChatMessageDetailScreen(text, MinecraftClient.getInstance().currentScreen))
    }
}
