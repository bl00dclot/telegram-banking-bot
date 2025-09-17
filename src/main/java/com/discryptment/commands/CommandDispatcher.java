package com.discryptment.commands;

import com.discryptment.bot.SessionManager;
import com.discryptment.bot.conversation.Conversation;
import com.discryptment.bot.conversation.ConversationInterface;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.bot.conversation.ConversationRouter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private final Set<Long> adminList; // from config
    private final ConversationManager convMgr;
    private final ConversationRouter convRouter;
    private final SessionManager sessionManager;

    public CommandDispatcher(Set<Long> adminList,
                             ConversationManager convMgr,
                             ConversationRouter convRouter,
                             SessionManager sessionManager) throws Exception {
        this.adminList = adminList;
        System.out.println(adminList);
        this.convRouter = convRouter;
        this.convMgr = convMgr;
        this.sessionManager = sessionManager;
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

        // Quick exit for empty text
        if (text.isEmpty()) return false;

        // Check if user is in an active conversation
        ConversationInterface conv = convMgr.getConversation(tgId);
        if (conv != null) {
            // If the message is a slash command, try to run it (respect adminOnly)
            if (text.startsWith("/")) {
                
            	String[] parts = text.split("\\s+");
                String token = parts[0].toLowerCase();
                
                Set<String> safeCommands = Set.of("/cancel", "/help");
                BotCommand cmd = commands.get(token);
                
                if (cmd != null) {
                    // /start requirement: only allow non-/start commands after session started
                    if (!token.equals("/start") && !sessionManager.hasStarted(tgId)) {
                        ctx.bot.sendText(msg.getChatId(), "👉 Please send /start first to begin.");
                        return true;
                    }
                    
                    if (cmd.adminOnly() && !adminList.contains(msg.getFrom().getId())) {
                        ctx.bot.sendText(msg.getChatId(), "⛔ This command is admin-only.");
                        return true;
                    }
                    String[] args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];
                    try {
                        cmd.execute(msg, args, ctx);
                        if (token.equals("/start")) {
                            sessionManager.markStarted(tgId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ctx.bot.sendText(msg.getChatId(), "Internal error while executing command.");
                    }
                    return true;
                }
                // If it's a slash command but not a registered command, fall through to conversation handling
            }

            // Not a recognized command (or not starting with /) — treat as conversation input
            try {
                // convRouter will decide what to do with this message (password attempt, invoice step, etc.)
                // Signature assumed: handleMessage(Message message, Conversation conv, CommandContext ctx, ConversationManager convMgr)
                convRouter.handleMessage(msg, conv, ctx, convMgr);
            } catch (Exception e) {
                e.printStackTrace();
                // Inform the user of a generic error but do not leak internals
                ctx.bot.sendText(msg.getChatId(), e.getMessage() + "\n\nInternal error while processing your reply. Try /cancel and start again.");
                // Optionally end conversation to avoid stuck states
                // convMgr.endConversation(tgId);
            }
            return true; // handled as part of a conversation
        }

        // Not in a conversation — only treat text starting with a slash as a command
        if (!text.startsWith("/")) return false;

        String[] parts = text.split("\\s+");
        String token = parts[0].toLowerCase();
        BotCommand cmd = commands.get(token);
        if (cmd == null) return false;

        // session started check
        if (!token.equals("/start") && !sessionManager.hasStarted(tgId)) {
            ctx.bot.sendText(msg.getChatId(), "👉 Please send /start first to begin.");
            return true;
        }

        // admin check
        if (cmd.adminOnly() && !adminList.contains(msg.getFrom().getId())) {
            ctx.bot.sendText(msg.getChatId(), "⛔ This command is admin-only.");
            return true;
        }

        String[] args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];

        try {
            cmd.execute(msg, args, ctx);
            // mark started if it's /start
            if (token.equals("/start")) {
                sessionManager.markStarted(tgId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.bot.sendText(msg.getChatId(), "Internal error while executing command.");
        }
        return true;
    }
}
