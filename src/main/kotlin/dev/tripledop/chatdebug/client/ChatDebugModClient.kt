package dev.tripledop.chatdebug.client

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class ChatDebugModClient : ClientModInitializer {
    companion object {
        @JvmStatic
        lateinit var instance: ChatDebugModClient
            private set
    }

    override fun onInitializeClient() {
        instance = this
    }

    fun showTextInfo(text: Text) {
        MinecraftClient.getInstance().setScreen(ChatMessageDetailScreen(text, MinecraftClient.getInstance().currentScreen))
    }
}
