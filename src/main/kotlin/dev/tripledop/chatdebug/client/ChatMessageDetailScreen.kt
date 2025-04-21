package dev.tripledop.chatdebug.client

import com.google.gson.GsonBuilder
import com.mojang.serialization.JsonOps
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.MultilineText
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ScrollableTextWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.Identifier

class ChatMessageDetailScreen(val message: Text) : Screen(Text.literal("Chat Message Detail")) {
    val serialized: String = GsonBuilder().setPrettyPrinting().create().toJson(
        TextCodecs.CODEC!!.encodeStart(
            MinecraftClient.getInstance().player!!.registryManager.getOps(JsonOps.INSTANCE),
            message
        )!!.orThrow
    )
    val previousScreen = MinecraftClient.getInstance().currentScreen

    override fun init() {
        addDrawableChild(ScrollableTextWidget(20, 50, width - 50, height - 20 - 10 - ButtonWidget.DEFAULT_HEIGHT - 10 - 35, Text.literal(serialized), textRenderer))
        addDrawableChild(
            ButtonWidget.builder(Text.literal("Copy to clipboard")) {
                MinecraftClient.getInstance().keyboard.clipboard = serialized
            }.position(20, height - 20 - 10 - ButtonWidget.DEFAULT_HEIGHT / 2).build()
        )
        addDrawableChild(
            ButtonWidget.builder(Text.literal("Close")) {
                close()
            }.position(20 + ButtonWidget.DEFAULT_WIDTH + 10, height - 20 - 10 - ButtonWidget.DEFAULT_HEIGHT / 2).build()
        )
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)

        MultilineText.create(textRenderer, message, width - 40).drawCenterWithShadow(context, width / 2, 25)
    }

    override fun renderBackground(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks)

        context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("popup/background"), 10, 10, width - 20, height - 20)
    }

    override fun close() {
        MinecraftClient.getInstance().setScreen(previousScreen)
    }

    override fun shouldPause() = true
}