package com.gmail.necnionch.mymod.legacyplayervisibility;

import net.fabricmc.api.ClientModInitializer;
import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.LiteralText;

public class PlayerVisibilityMod implements ClientModInitializer {
//	private static final Logger LOGGER = Logger.get("PlayerVisibilityMod");
	private static boolean renderPlayer = true;

	public static boolean isRenderPlayer() {
		return renderPlayer;
	}

	@Override
	public void onInitializeClient() {
		KeyBinding renderPlayerKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("Toggle Visibility", 44, "PlayerVisibility"
				));

		ClientTickEvents.END_CLIENT_TICK.register(unused -> {
			if (renderPlayerKey.wasPressed()) {
				renderPlayer = !renderPlayer;
				String state = renderPlayer ? "§aON" : "§cOFF";
				MinecraftClient.getInstance().player.sendMessage(new LiteralText("§eToggled player visibility: " + state));
			}
		});
	}
}
