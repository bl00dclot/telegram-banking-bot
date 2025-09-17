package com.discryptment.bot.conversation;

import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.discryptment.commands.CommandContext;

public abstract class StepConversation implements ConversationInterface {
    protected final long tgId;
    protected int step = 0;
    protected ConversationInterface.Status status = ConversationInterface.Status.ACTIVE;

    public StepConversation(long ownerTg) { this.tgId = ownerTg; }

    @Override
    public long getOwnerTelegramId() { return tgId; }
    @Override
    public ConversationInterface.Status getStatus() { return status; }

    @Override
    public boolean handleMessage(Message msg, CommandContext ctx, ConversationManager convMgr) {
        String text = msg.getText().trim();
        switch(step) {
            case 0: return handleStep0(text, msg, ctx, convMgr);
            case 1: return handleStep1(text, msg, ctx, convMgr);
            // ...
            default:
                ctx.bot.sendText(msg.getChatId(), "Conversation finished.");
                status = ConversationInterface.Status.COMPLETED;
                return true;
        }
    }

    protected abstract boolean handleStep0(String text, Message msg, CommandContext ctx, ConversationManager convMgr);
    protected abstract boolean handleStep1(String text, Message msg, CommandContext ctx, ConversationManager convMgr);
}
