package com.example.addon.mixin;

import com.example.addon.modules.NoChatNormalisation;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @Redirect(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;normalize(Ljava/lang/String;)Ljava/lang/String;"))
    String onChatMessageNormalize(ChatScreen instance, String chatText) {
        if (!chatText.isEmpty()) MinecraftClient.getInstance().inGameHud.getChatHud()
            .addToMessageHistory(chatText);

        String text = chatText;
        NoChatNormalisation noChatNormalisation = Modules.get().get(NoChatNormalisation.class);
        if (!noChatNormalisation.isActive()) text = instance.normalize(chatText);
        return text;
    }
}