package com.discryptment.commands;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public class StartCommand implements BotCommand {
    @Override public String name() { return "/start"; }
    @Override public String description() { return "Start the bot"; }

    @Override
    public void execute(Message message, String[] args, CommandContext ctx) throws Exception {

        long telegramId = message.getFrom().getId();
        String username = message.getFrom().getUserName();

        //Build User POJO
//        User user = new User();
//        user.setTelegramId(telegramId);
//        user.setUsername(username);
//        user.setGoldBalance(0.0);
//        user.setExpectedUsdTotal(0.0);
//        user.setRealUsdBalance(0.0);
//        user.setAuthorized(true);
//        System.out.println(user.getUsername());


        // service will open/close DB connection internally
//        ctx.userService.registerUser(user);

        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to the Banking Bot").append("\n\n");
        sb.append("Privacy matters").append("\n\n");
        sb.append("To get started please use the login command").append("\n");
        sb.append("                  /login");
        ctx.bot.sendText(message.getChatId(), sb.toString());
        // optionally trigger password prompt flow here
    }
}
