package com.discryptment.commands.user.profile;

import com.discryptment.commands.BotCommand;
import com.discryptment.commands.CommandContext;
import com.discryptment.model.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.text.NumberFormat;
import java.util.Locale;

public class ProfileCommand implements BotCommand {
    @Override public String name() { return "/profile"; }
    @Override public String description() { return "View profile"; }

    @Override
    public void execute(Message message, String[] args, CommandContext ctx) throws Exception {

        long telegramId = message.getFrom().getId();
        String username = message.getFrom().getUserName();
        long chatId = message.getChatId();


        //Build User POJO
        User user = new User();
        user.setTelegramId(telegramId);
        user.setUsername(username);
        System.out.println(user.getUsername());


        // service will open/close DB connection internally
        User profile = ctx.userService.getProfile(user);

        if(profile == null){
            ctx.bot.sendText(chatId, "No profile found. Use /start to register.");
            return;
        }
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(2);

        StringBuilder sb = new StringBuilder();
        sb.append("*Profile for* ").append(profile.getUsername()).append("\n\n");
        sb.append("• *Gold:* ").append(nf.format(profile.getGoldBalance())).append("\n");
        sb.append("• *Real USD:* $").append(nf.format(profile.getRealUsdBalance())).append("\n");
        sb.append("• *Expected USD Total:* $").append(nf.format(profile.getExpectedUsdTotal())).append("\n\n");


        ctx.bot.sendText(chatId, sb.toString());
        // optionally trigger password prompt flow here
    }
}
