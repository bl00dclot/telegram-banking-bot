package com.discryptment.bot.conversation;

import com.discryptment.commands.CommandContext;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.sql.SQLException;

public class ConversationRouter {
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

//        boolean ok = ctx.configService.verifyPassword(attempt);

//        if (ok) {
//            // mark user as authorized in DB
//            ctx.userService.setAuthorizedByTelegramId(tgId, true);
//            convMgr.endConversation(tgId);
//            ctx.bot.sendText(chatId, "✅ Authorized. You can use protected commands.");
//            return;
//        } else {
//            conv.setAttemptsLeft(conv.getAttemptsLeft() - 1);
//            if (conv.getAttemptsLeft() <= 0) {
//                convMgr.endConversation(tgId);
//                // Optional: temporary block recorded in DB
//                ctx.bot.sendText(chatId, "Too many failed attempts. Contact admin.");
//                return;
//            } else {
//                ctx.bot.sendText(chatId, "Wrong password. Attempts left: " + conv.getAttemptsLeft() + ". Try again or send /cancel.");
//                // conversation remains active
//                return;
//            }
//        }
    }
}
