package me.johnadept.rename;

import me.johnadept.rename.commands.RenameCommand;
import me.johnadept.rename.commands.ResetNameCommand;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rename implements ModInitializer {
	public static final String MOD_ID = "rename_command";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			RenameCommand.register(dispatcher, registryAccess);
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ResetNameCommand.register(dispatcher, registryAccess);
		});
	}
}