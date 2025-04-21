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
            ButtonWidget.builder(Text.translatable("chat.copy")) {
                MinecraftClient.getInstance().keyboard.clipboard = serialized
            }
                .position(20, height - 20 - 10 - ButtonWidget.DEFAULT_HEIGHT / 2)
                .width((width - 40) / 2 - 4)
                .build()
        )
        addDrawableChild(
            ButtonWidget.builder(Text.translatable("mco.selectServer.close")) {
                close()
            }
                .position(20 + (width - 40) / 2 + 2, height - 20 - 10 - ButtonWidget.DEFAULT_HEIGHT / 2)
                .width((width - 40) / 2 - 4)
                .build()
        )
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