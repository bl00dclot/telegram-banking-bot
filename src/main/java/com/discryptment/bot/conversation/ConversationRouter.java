package com.discryptment.bot.conversation;

import com.discryptment.commands.CommandContext;
import com.discryptment.service.AuthService;
import com.discryptment.service.UserService;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.sql.SQLException;

public class ConversationRouter {
    private final AuthService authService;
    private final UserService userService;
    public ConversationRouter(AuthService authService, UserService userService){
        this.authService = authService;
        this.userService = userService;
    }

    public void handleMessage(Message message, Conversation conv, CommandContext ctx, ConversationManager convMgr) throws Exception {
        if(conv.getState() == ConversationState.AWAITING_PASSWORD){
            handlePassword(message, conv, ctx, convMgr);
            return;
        }
    }
    public void handlePassword(Message message, Conversation conv, CommandContext ctx, ConversationManager convMgr) throws SQLException {
        long tgId = conv.getTelegramId();
        long chatId = message.getChatId();
        String attempt = message.getText().trim();

        boolean ok = ctx.authService.verifyPassword(attempt);

        if (ok) {
            // mark user as authorized in DB
            authService.markUserAuthorizedByTelegramId(tgId, true);
            convMgr.endConversation(tgId);
            ctx.bot.sendText(chatId, "✅ Authorized. You can use protected commands.");
        } else {
            conv.setAttemptsLeft(conv.getAttemptsLeft() - 1);
            if (conv.getAttemptsLeft() <= 0) {
                convMgr.endConversation(tgId);
                // Optional: temporary block recorded in DB
                ctx.bot.sendText(chatId, "Too many failed attempts. Contact admin.");
            } else {
                ctx.bot.sendText(chatId, "Wrong password. Attempts left: " + conv.getAttemptsLeft() + ". Try again or send /cancel.");
                // conversation remains active
            }
        }
    }
}
