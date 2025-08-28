package com.discryptment.commands.user.profile;

import com.discryptment.commands.BotCommand;
import com.discryptment.commands.CommandContext;
import com.discryptment.model.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class AddGoldCommand implements BotCommand {
    @Override
    public String name() {
        return "/addGold";
    }

    @Override
    public String description() {
        return "Add gold amount";
    }

    @Override
    public void execute(Message message, String[] args, CommandContext ctx) throws Exception {

        long telegramId = message.getFrom().getId();
        String username = message.getFrom().getUserName();
        long chatId = message.getChatId();
        String msg = message.getText();
        String[] parts = msg.split(" ");
        int gold = Integer.valueOf(parts[1]);
        System.out.println(gold);


        //Build User POJO
        User user = new User();
        user.setTelegramId(telegramId);
        user.setUsername(username);
        System.out.println(user.getUsername());


        // service will open/close DB connection internally
        User profile = ctx.userService.getProfile(user);
        int id = profile.getId();
        ctx.userService.addGold(id, gold);

        if (profile == null) {
            ctx.bot.sendText(chatId, "No profile found. Use /start to register.");
            return;
        }
    }
}
