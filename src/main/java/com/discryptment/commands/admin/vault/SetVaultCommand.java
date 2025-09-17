package com.discryptment.commands.admin.vault;

import com.discryptment.bot.conversation.Conversation;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.bot.conversation.ConversationState;
import com.discryptment.commands.BotCommand;
import com.discryptment.commands.CommandContext;
import com.discryptment.service.AdminService;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.sql.SQLException;
import java.util.HashMap;

public class SetVaultCommand implements BotCommand {
    private final ConversationManager convMgr;

    public SetVaultCommand(ConversationManager convMgr) {
        this.convMgr = convMgr;
    }

    @Override
    public String name() {
        return "/setVault";
    }
    @Override
    public String description() {
        return "SetVault - Set vault values";
    }

    @Override
    public boolean adminOnly() {
        return true;
    }

    @Override
    public void execute(Message message, String[] args, CommandContext ctx) throws SQLException {
        long tgId = message.getFrom().getId();
        String username = message.getFrom().getUserName();
        long chatId = message.getChatId();

        Conversation conv = new Conversation();
        conv.setTelegramId(tgId);
        conv.setState(ConversationState.SETTING_VAULT);
        conv.setStartedAt(System.currentTimeMillis());
        conv.setData(new HashMap<>());

        convMgr.startConversation(tgId, conv);
        System.out.println(username + " starting to set vault");


    }
}
