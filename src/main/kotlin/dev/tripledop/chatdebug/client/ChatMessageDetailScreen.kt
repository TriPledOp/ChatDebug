package dev.tripledop.chatdebug.client

import com.fasterxml.jackson.databind.ObjectMapper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ScrollableTextWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.entur.jackson.jsh.SyntaxHighlightingJsonGenerator
import java.io.StringWriter

class ChatMessageDetailScreen(val message: Text, val previousScreen: Screen?) : Screen(Text.literal("Chat Message Detail")) {
    val serialized: String = Text.Serialization.toJsonString(message, MinecraftClient.getInstance().player!!.registryManager)
    val highlightedSerialized = {
        val highlJsonBuf = StringWriter()
        val syntaxHighl = SyntaxHighlightingJsonGenerator(ObjectMapper().createGenerator(highlJsonBuf), MCJsonSyntaxHighlight(), true)
        syntaxHighl.writeTree(ObjectMapper().readTree(serialized.replace("§", "&")))
        highlJsonBuf.toString()
    }()

    override fun init() {
        addDrawableChild(
            ScrollableTextWidget(
                20,
                20,
                width - 50,
                25,
                message,
                textRenderer
            )
        )
        addDrawableChild(
            ScrollableTextWidget(
                20,
                50,
                width - 50,
                height - 20 - 10 - ButtonWidget.DEFAULT_HEIGHT - 10 - 35,
                Text.literal(highlightedSerialized),
                textRenderer
            )
        )
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

//    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
//        super.render(context, mouseX, mouseY, deltaTicks)
//
//        MultilineText.create(textRenderer, message, width - 40).drawCenterWithShadow(context, width / 2, 25)
//    }

    override fun renderBackground(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks)

        context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("popup/background"), 10, 10, width - 20, height - 20)
    }

    override fun close() {
        MinecraftClient.getInstance().setScreen(previousScreen)
    }

    override fun shouldPause() = true
}