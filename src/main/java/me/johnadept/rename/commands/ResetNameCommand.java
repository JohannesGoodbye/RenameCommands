package me.johnadept.rename.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Iterator;

public class ResetNameCommand {
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType((entityName) -> {
        return Text.translatable("commands.rename.failed.entity", new Object[]{entityName});
    });
    private static final DynamicCommandExceptionType FAILED_ITEMLESS_EXCEPTION = new DynamicCommandExceptionType((entityName) -> {
        return Text.literal("Entity " + entityName + " has no item in hand.");
    });

    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType((Text.literal("Failed to rename item.")));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("rename").requires((source) -> {
            return source.hasPermissionLevel(2);
        })).then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> {
            return execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"));
        })));
    }

    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        int renamedCount = 0;
        Iterator<? extends Entity> var6 = targets.iterator();

        while (var6.hasNext()) {
            Entity entity = var6.next();
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                ItemStack itemStack = livingEntity.getMainHandStack();

                if (!itemStack.isEmpty()) {
                    itemStack.remove(DataComponentTypes.CUSTOM_NAME);
                    renamedCount++;
                } else if (targets.size() == 1) {
                    throw FAILED_ITEMLESS_EXCEPTION.create(livingEntity.getName().getString());
                }
            } else if (targets.size() == 1) {
                throw FAILED_ENTITY_EXCEPTION.create(entity.getName().getString());
            }
        }

        if (renamedCount == 0) {
            throw FAILED_EXCEPTION.create();
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> {
                return Text.literal("Reset the hand item name" + targets.iterator().next().getName().getString());
            }, true);
        } else {
            source.sendFeedback(() -> {
                return Text.literal("Reset the hand item names of " + targets.size() + " entities");
            }, true);
        }

        return renamedCount;
    }
}