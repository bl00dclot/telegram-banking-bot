package com.discryptment.commands;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface BotCommand {
    /** primary command token, e.g. "/start" */
    String name();

    /** short help for /help listing */
    String description();

    /** whether only the configured admin may run this */
    default boolean adminOnly() { return false; }

    /**
     * Execute the command.
     * @param message the incoming Telegram Message (guaranteed to be text)
     * @param args parsed arguments (split by whitespace, excluding the command token)
     * @param ctx services & bot helpers
     */
    void execute(Message message, String[] args, CommandContext ctx) throws Exception;
}

