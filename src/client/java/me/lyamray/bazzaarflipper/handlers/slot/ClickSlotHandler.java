/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.slot;

import lombok.Getter;
import me.lyamray.bazzaarflipper.mixin.client.HandledScreenAccessor;
import me.lyamray.bazzaarflipper.mixin.client.MouseAccessor;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class ClickSlotHandler {

    @Getter
    private static final ClickSlotHandler instance = new ClickSlotHandler();
    private final Random random = new Random();

    private double lastCursorX = 0;
    private double lastCursorY = 0;

    public void clickSlotSimulated(MinecraftClient client, int slotIndex, ScreenHandler screenHandler) {
        if (client.player == null || !(client.currentScreen instanceof HandledScreen<?> gui)) return;

        Slot slot = screenHandler.getSlot(slotIndex);
        if (slot == null) return;

        try {
            int guiLeft = ((HandledScreenAccessor) gui).getX();
            int guiTop = ((HandledScreenAccessor) gui).getY();

            int targetX = guiLeft + slot.x + 8 + randomOffset();
            int targetY = guiTop + slot.y + 8 + randomOffset();

            double scaleFactor = client.getWindow().getScaleFactor();
            double endX = targetX * scaleFactor;
            double endY = targetY * scaleFactor;
            long windowHandle = client.getWindow().getHandle();

            double startX = lastCursorX;
            double startY = lastCursorY;

            if (startX == 0 && startY == 0) {
                startX = client.mouse.getX() * scaleFactor;
                startY = client.mouse.getY() * scaleFactor;
            }

            smoothMoveCursor(windowHandle, startX, startY, endX, endY);

            gui.mouseClicked(targetX, targetY, 0);

            lastCursorX = endX;
            lastCursorY = endY;

            Thread.sleep(100 + random.nextInt(200));

        } catch (InterruptedException e) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[ERROR] Exception in clickSlotSimulated: " + e));
            e.printStackTrace();
        }
    }

    private void smoothMoveCursor(long window, double startX, double startY, double endX, double endY) {
        int steps = 15 + random.nextInt(10);
        double prevX = startX;
        double prevY = startY;

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        for (int i = 1; i <= steps; i++) {
            double t = i / (double) steps;

            double progress = 0.5 - 0.5 * Math.cos(Math.PI * t + random.nextGaussian() * 0.05);

            double targetX = startX + (endX - startX) * progress;
            double targetY = startY + (endY - startY) * progress;

            double stepX = prevX + (targetX - prevX) * (0.7 + random.nextDouble() * 0.3) + random.nextGaussian() * 0.5;
            double stepY = prevY + (targetY - prevY) * (0.7 + random.nextDouble() * 0.3) + random.nextGaussian() * 0.5;

            GLFW.glfwSetCursorPos(window, stepX, stepY);
            ((MouseAccessor) MinecraftClient.getInstance().mouse).callOnCursorPos(window, stepX, stepY);

            prevX = stepX;
            prevY = stepY;

            try {
                Thread.sleep(5 + random.nextInt(8));
            } catch (InterruptedException ignored) {}
        }

        GLFW.glfwSetCursorPos(window, endX, endY);
        ((MouseAccessor) MinecraftClient.getInstance().mouse).callOnCursorPos(window, endX, endY);

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    private int randomOffset() {
        return random.nextInt(7) - 3;
    }
}
