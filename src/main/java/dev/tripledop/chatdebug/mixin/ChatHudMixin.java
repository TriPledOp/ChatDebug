package dev.tripledop.chatdebug.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.tripledop.chatdebug.ChatDebugMod;
import dev.tripledop.chatdebug.client.ChatDebugModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.text.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I", shift = At.Shift.AFTER))
    private void renderChat(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci, @Local(ordinal = 19) int y, @Local ChatHudLine.Visible visible, @Local(ordinal = 15) int u) {
        int dx = context.drawTextWithShadow(client.textRenderer, visible.content(), 0, y, ColorHelper.withAlpha(u, Colors.WHITE));

        if (focused && ChatDebugModClient.isEnabled() && ChatDebugMod.getTexts().containsKey(visible.content().hashCode())) context.drawTexture(
            RenderLayer::getGuiTextured,
            Identifier.of("chatdebug", "textures/gui/json_btn.png"),
            dx + 4, y - 1, 0f, 0f, 21, 9, 21, 9
        );
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    public int dontDrawTextHere(DrawContext instance, TextRenderer textRenderer, OrderedText text, int x, int y, int color) {
        return x;
    }

    @Redirect(method = "getTextStyleAt(DD)Lnet/minecraft/text/Style;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextHandler;getStyleAt(Lnet/minecraft/text/OrderedText;I)Lnet/minecraft/text/Style;"))
    public Style handleTooltip(TextHandler instance, OrderedText text, int x, @Local ChatHudLine.Visible visible) {
        Style s = instance.getStyleAt(text, x);
        float len = instance.getWidth(text);
        float lenp = x - len;
        if (s == null && (lenp > 4) && (lenp <= 21 + 4) && ChatDebugModClient.isEnabled() && ChatDebugMod.getTexts().containsKey(visible.content().hashCode()))
            return Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(Text.translatable("text.chatdebug.json_icon.hover"))).withClickEvent(new ClickEvent.OpenUrl(URI.create("view-msg-details:" + visible.content().hashCode())));
        return s;
    }

    @Inject(method = "addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;isChatFocused()Z", shift = At.Shift.AFTER))
    private void addVisibleChatLine(ChatHudLine message, CallbackInfo ci, @Local List<OrderedText> list) {
        if (!list.isEmpty()) {
            ChatDebugMod.getTexts().put(list.getLast().hashCode(), message.content());
        }
    }

    @Redirect(method = "addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/ChatMessages;breakRenderedChatMessageLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;"))
    private List<OrderedText> redirectChatMsgSplit(StringVisitable message, int width, TextRenderer textRenderer) {
        return ChatMessages.breakRenderedChatMessageLines(message, width - (ChatDebugModClient.isEnabled() ? (4 + 21) : 0), textRenderer);
    }
}
