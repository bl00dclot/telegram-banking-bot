package com.discryptment.commands;

import com.discryptment.bot.BankingBot;
import com.discryptment.service.UserService;

public class CommandContext {
    public final BankingBot bot;
    public final UserService userService;

    public CommandContext(BankingBot bot,
                          UserService userService){
        this.bot = bot;
        this.userService = userService;
    }
}
