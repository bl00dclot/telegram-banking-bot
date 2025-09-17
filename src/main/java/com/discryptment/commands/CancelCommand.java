package com.discryptment.commands;

import com.discryptment.bot.conversation.Conversation;
import com.discryptment.bot.conversation.ConversationInterface;
import com.discryptment.bot.conversation.ConversationManager;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class CancelCommand implements BotCommand {
    @Override
    public String name() {
        return "/cancel";
    }

    @Override
    public String description() {
        return "Cancel current action";
    }

    @Override
    public void execute(Message message, String[] args, CommandContext ctx) throws Exception {

        long tgId = message.getFrom().getId();
        long chatId = message.getChatId();

        ConversationManager convMgr = ctx.getConversationManager();

        ConversationInterface conv = convMgr.getConversation(tgId);
        if (conv == null) {
            ctx.bot.sendText(chatId, "There is no active operation to cancel.");
            return;
        }

        // Optional: do any cleanup based on conv.getState() or conv.getData()
        // e.g. if there are temporary DB rows, remove them here (call service methods)

        convMgr.endConversation(tgId);
        ctx.bot.sendText(chatId, "✅ Operation cancelled.");
    }
}
