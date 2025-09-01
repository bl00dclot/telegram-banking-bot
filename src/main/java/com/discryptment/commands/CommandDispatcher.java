package com.discryptment.commands;

import com.discryptment.bot.conversation.Conversation;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.bot.conversation.ConversationRouter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches incoming Telegram updates to either:
 * - an active conversation handler (if user is mid-conversation), or
 * - a registered BotCommand (if message is a /command).
 * <p>
 * Constructor requires a ConversationManager and ConversationRouter so
 * follow-up messages (e.g. password input) are routed before usual command parsing.
 */
public class CommandDispatcher {
    private final Map<String, BotCommand> commands = new HashMap<>();
    private final long adminTelegramId; // from config
    private final ConversationManager convMgr;
    private final ConversationRouter convRouter;

    public CommandDispatcher(long adminTelegramId, ConversationManager convMgr, ConversationRouter convRouter) throws Exception {
        this.adminTelegramId = adminTelegramId;
        this.convRouter = convRouter;
        this.convMgr = convMgr;
    }

    /**
     * Register a command instance (token from cmd.name() will be normalized to lower-case)
     */

    public void register(BotCommand cmd) {
        commands.put(cmd.name().toLowerCase(), cmd);
    }

    /**
     * Dispatch an Update.
     * Returns true if the update was handled (either routed to a conversation or a command).
     * Returns false if it was not a command (so higher-level code can handle non-command updates).
     */
    public boolean dispatch(Update update, CommandContext ctx) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return false;
        Message msg = update.getMessage();
        String text = msg.getText().trim();
        long tgId = msg.getFrom().getId();

        // 1) If user is currently in a conversation flow, handle that first
        Conversation conv = convMgr.getConversation(tgId);
        if (conv != null) {
            try {
                // convRouter will decide what to do with this message (password attempt, invoice step, etc.)
                // Signature assumed: handleMessage(Message message, Conversation conv, CommandContext ctx, ConversationManager convMgr)
                convRouter.handleMessage(msg, conv, ctx, convMgr);
            } catch (Exception e) {
                e.printStackTrace();
                // Inform the user of a generic error but do not leak internals
                ctx.bot.sendText(msg.getChatId(), "Internal error while processing your reply. Try /cancel and start again.");
                // End conversation to avoid stuck states if appropriate (optional)
                // convMgr.endConversation(tgId);

            }
            return true; // we've handled the message as part of a conversation
        }

        // 2) Not in a conversation — only treat text starting with a slash as a command
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