package com.discryptment.commands.user.auth;

import com.discryptment.bot.conversation.Conversation;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.bot.conversation.ConversationState;
import com.discryptment.commands.BotCommand;
import com.discryptment.commands.CommandContext;
import com.discryptment.model.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.HashMap;

public class LoginCommand implements BotCommand {
    private final ConversationManager convMgr;
    private final int maxAttempts;

    public LoginCommand(ConversationManager convMgr, int maxAttempts){
        this.convMgr = convMgr;
        this.maxAttempts = maxAttempts;
    }
    @Override public String name() { return "/login"; }
    @Override public String description() { return "Login - start login flow"; }

    @Override
    public void execute(Message message, String[] args, CommandContext ctx) throws Exception {

        long telegramId = message.getFrom().getId();
        String username = message.getFrom().getUserName();
        long chatId = message.getChatId();
//
//        //Build User POJO
//        User user = new User();
//        user.setTelegramId(telegramId);
//        user.setUsername(username);
//        user.setGoldBalance(0.0);
//        user.setExpectedUsdTotal(0.0);
//        user.setRealUsdBalance(0.0);
//        user.setAuthorized(true);
//        System.out.println(user.getUsername());

        Conversation conv = new Conversation();
        conv.setTelegramId(telegramId);
        conv.setState(ConversationState.AWAITING_PASSWORD);
        conv.setAttemptsLeft(maxAttempts);
        conv.setStartedAt(System.currentTimeMillis());
        conv.setData(new HashMap<>());

        convMgr.startConversation(telegramId, conv);


        // service will open/close DB connection internally
//        ctx.userService.registerUser(user);
        StringBuilder sb = new StringBuilder();
        sb.append("Enter password (you have ").append(maxAttempts).append(" attempts");

        ctx.bot.sendText(chatId, sb.toString());
        // optionally trigger password prompt flow here
    }
}
