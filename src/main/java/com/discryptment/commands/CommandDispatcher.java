package com.discryptment.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<String, BotCommand> commands = new HashMap<>();
    private final long adminTelegramId; // from config

    public CommandDispatcher(long adminTelegramId) {
        this.adminTelegramId = adminTelegramId;
    }

    public void register(BotCommand cmd) {
        commands.put(cmd.name().toLowerCase(), cmd);
    }

    public boolean dispatch(Update update, CommandContext ctx) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return false;
        Message msg = update.getMessage();
        String text = msg.getText().trim();
        if (text.isEmpty() || !text.startsWith("/")) return false;

        String[] parts = text.split("\\s+");
        String token = parts[0].toLowerCase();
        BotCommand cmd = commands.get(token);
        if (cmd == null) return false;

        // admin check
        if (cmd.adminOnly() && msg.getFrom().getId() != adminTelegramId) {
            ctx.bot.sendText(msg.getChatId(), "⛔ This command is admin-only.");
            return true;
        }

        String[] args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];

        try {
            cmd.execute(msg, args, ctx);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.bot.sendText(msg.getChatId(), "Internal error while executing command.");
        }
        return true;
    }
}
