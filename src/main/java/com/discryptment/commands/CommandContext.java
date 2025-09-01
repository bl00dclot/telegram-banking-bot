package com.discryptment.commands;

import com.discryptment.bot.BankingBot;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.service.AuthService;
import com.discryptment.service.UserService;

public class CommandContext {
    public final BankingBot bot;
    public final ConversationManager convMgr;
    public final UserService userService;
    public final AuthService authService;

    public CommandContext(BankingBot bot,
                          ConversationManager convMgr,
                          UserService userService,
                          AuthService authService){
        this.bot = bot;
        this.convMgr = convMgr;
        this.userService = userService;
        this.authService = authService;
    }
    public ConversationManager getConversationManager() {
        return convMgr;
    }
}
