/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.utils.messages;

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