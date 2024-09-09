package de.johnadept.rename;

import de.johnadept.rename.commands.RenameCommand;
import de.johnadept.rename.commands.RenameEntityCommand;
import de.johnadept.rename.commands.ResetEntityNameCommand;
import de.johnadept.rename.commands.ResetNameCommand;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rename implements ModInitializer {
	public static final String MOD_ID = "rename_command";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			RenameCommand.register(dispatcher, registryAccess);
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ResetNameCommand.register(dispatcher, registryAccess);
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			RenameEntityCommand.register(dispatcher, registryAccess);
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ResetEntityNameCommand.register(dispatcher, registryAccess);
		});
	}
}