package com.discryptment.commands.admin;

import com.discryptment.commands.BotCommand;
import com.discryptment.commands.CommandContext;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class VaultCommand implements BotCommand {
    @Override
    public String name() {
        return "/vault";
    }

    @Override
    public String description() {
        return "Get vault values";
    }
    public void execute(Message message, String[] args, CommandContext ctx) throws Exception {

    }
}
