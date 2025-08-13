package me.lyamray.bazzaarflipper.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class MessageUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Component makeComponent(String message) {
        return Component.empty()
                .appendNewline()
                .append(MiniMessage.miniMessage().deserialize(message))
                .appendNewline();
    }

    public Component makeComponent(String message, String message2) {
        return Component.empty()
                .appendNewline()
                .append(MessageUtil.deserialize(message))
                .appendNewline()
                .appendNewline()
                .append(MessageUtil.deserialize(message2)
                .appendNewline());
    }

    public void sendMessage(Component component) {
        Audience audience = MinecraftClientAudiences.of().audience();
        audience.sendMessage(component);
    }


    public Component deserialize(String miniMessageText) {
        return MINI_MESSAGE.deserialize(miniMessageText);
    }
}