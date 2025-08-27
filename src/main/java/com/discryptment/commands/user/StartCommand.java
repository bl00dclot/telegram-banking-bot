package com.discryptment.commands.user;

import com.discryptment.commands.BotCommand;
import com.discryptment.commands.CommandContext;
import com.discryptment.model.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class StartCommand implements BotCommand {
    @Override public String name() { return "/start"; }
    @Override public String description() { return "Register and login"; }

    @Override
    public void execute(Message message, String[] args, CommandContext ctx) throws Exception {

        long telegramId = message.getFrom().getId();
        String username = message.getFrom().getUserName();

        //Build User POJO
        User user = new User();
        user.setTelegramId(telegramId);
        user.setUsername(username);
        user.setGoldBalance(0.0);
        user.setExpectedUsdTotal(0.0);
        user.setRealUsdBalance(0.0);
        user.setAuthorized(true);
        System.out.println(user.getUsername());


        // service will open/close DB connection internally
        ctx.userService.registerUser(user);
        ctx.bot.sendText(message.getChatId(), "Welcome, " + (user.getUsername() == null ? "friend" : user.getUsername()));
        // optionally trigger password prompt flow here
    }
}
