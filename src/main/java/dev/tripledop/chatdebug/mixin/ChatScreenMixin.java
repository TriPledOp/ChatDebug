package dev.tripledop.chatdebug.mixin;

import dev.tripledop.chatdebug.ChatDebugMod;
import dev.tripledop.chatdebug.client.ChatDebugModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URI;

@Mixin(Screen.class)
public class ChatScreenMixin {
    @Inject(method = "handleTextClick(Lnet/minecraft/text/Style;)Z", at = @At("HEAD"), cancellable = true)
    public void handleClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style == null || !ChatDebugModClient.isEnabled()) return;
        if (Screen.hasShiftDown()) return;
        final var evt = style.getClickEvent();
        if (evt == null) return;
        if (evt instanceof ClickEvent.OpenUrl(URI uri)) {
            if (uri.getScheme().equals("view-msg-details")) {
                final var textHash = Integer.parseInt(uri.getRawSchemeSpecificPart());
                final var text = ChatDebugMod.getTexts().get(textHash);
                if (text != null) ChatDebugModClient.getInstance().showTextInfo(text);
                else MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.translatable("text.chatdebug.error.notfound").styled(s -> s.withColor(TextColor.fromRgb(0xFFFF0000))));
                cir.setReturnValue(true);
            }
        }
    }
}
