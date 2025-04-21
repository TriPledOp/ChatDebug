package dev.tripledop.chatdebug

import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class ChatDebugMod : ModInitializer {
    companion object {
        @JvmStatic
        val texts: HashMap<Int?, Text?> = HashMap()
    }

    override fun onInitialize() {
    }
}
